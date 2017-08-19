package com.aniov.service;

import com.aniov.model.*;
import com.aniov.model.dto.UserRegisterDTO;
import com.aniov.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User service
 */
@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private VerificationTokenService verificationTokenService;

    @Value("${account.activated}")
    private boolean isEnabled;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);
        if (user == null) {
            return null;
        }
        return new SiteUserDetails(user);
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findUserByUserName(String username) {
        return userRepository.findByUsername(username);
    }

    public User findUserByUserNameIgnoreCase(String username) {
        return userRepository.findByUsernameIgnoreCase(username);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    /**
     * Register a new User
     *
     * @param userRegisterDTO user data
     * @return saved user
     */
    public User registerNewUser(UserRegisterDTO userRegisterDTO) {

        //User
        User newUser = new User();
        //Set email, username, password to out User
        newUser.setEmail(userRegisterDTO.getEmail());
        newUser.setUsername(userRegisterDTO.getUsername());
        //Encrypt password before saving it to DB
        newUser.setHashedPassword(new BCryptPasswordEncoder().encode(userRegisterDTO.getPlainPassword()));

        //Account
        Account newAccount = new Account();
        if (isEnabled) {
            newAccount.setEnabled(true);
        }
        //Profile
        Profile newProfile = new Profile();

        //Account and Profile must have a user set
        newAccount.setUser(newUser);
        newProfile.setUser(newUser);

        //We also set Account and Profile in our new User and then save it
        newUser.setAccount(newAccount);
        newUser.setProfile(newProfile);

        User savedUser = userRepository.save(newUser);

        return savedUser;
    }

    /**
     * Change user password
     *
     * @param user          User
     * @param plainPassword new password
     */
    public void changeUserPassword(User user, String plainPassword) {

        User retrievedUser = userRepository.findByEmail(user.getEmail());
        if (retrievedUser == null) {
            return;
        }
        retrievedUser.setHashedPassword(new BCryptPasswordEncoder().encode(plainPassword));
        userRepository.save(retrievedUser);
    }

    /**
     * Run every 1h, and clean up data base from expired && un-activated accounts
     */
    @Scheduled(cron = "0 0 * * * *")
    public void deleteExpiredAccounts() {

        List<VerificationToken> allTokens = verificationTokenService.getAll();

        for (VerificationToken token : allTokens) {
            deleteExpiredAccountsAndTokens(token);
        }
    }

    /**
     * Search for Users with username containing string
     *
     * @param str string to be searched after
     * @return List<User>
     */
    public List<User> findByUserNameContaining(String str) {

        List<User> users = userRepository.findByUsernameIgnoreCaseContaining(str);

        List<Account> accounts = accountService.findAllAccountOk();
        List<User> usersAccountsOk = new ArrayList<>();

        for (Account account : accounts) {
            usersAccountsOk.add(account.getUser());
        }
        users.retainAll(usersAccountsOk);
        return users;
    }

    private void deleteExpiredAccountsAndTokens(VerificationToken token) {

        Date todayDate = new Date();
        //Delete account & all related
        if (token.getExpiryDate().before(todayDate) && token.getTokenType().equals(TokenType.ACTIVATION)) {
            verificationTokenService.deleteToken(token);
            User userToBeDeleted = token.getUser();
            if (!userToBeDeleted.getAccount().isEnabled()) {
                userRepository.delete(userToBeDeleted);
            }

            //Delete just token
        } else if (token.getExpiryDate().before(todayDate) && token.getTokenType().equals(TokenType.PASSWORD_RESET)) {
            verificationTokenService.deleteToken(token);
        }
    }

}
