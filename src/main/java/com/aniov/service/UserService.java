package com.aniov.service;

import com.aniov.model.Account;
import com.aniov.model.Profile;
import com.aniov.model.SiteUserDetails;
import com.aniov.model.User;
import com.aniov.model.dto.UserRegisterDTO;
import com.aniov.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

/**
 * User service
 */
@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenService verificationTokenService;

    @Autowired
    private EmailService emailService;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(userName);
        return new SiteUserDetails(user);
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findUserByUserName(String userName) {
        return userRepository.findByUsername(userName);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    /**
     * Register a new User
     *
     * @param userRegisterDTO user data
     * @return saved user
     */
    public boolean registerNewUser(UserRegisterDTO userRegisterDTO) {

        //User
        User newUser = new User();
        //Set email, username, password to out User
        newUser.setEmail(userRegisterDTO.getEmail());
        newUser.setUsername(userRegisterDTO.getUsername());
        //Encrypt password before saving it to DB
        newUser.setHashedPassword(new BCryptPasswordEncoder().encode(userRegisterDTO.getPlainPassword()));

        //Account
        Account newAccount = new Account();
        //Profile
        Profile newProfile = new Profile();

        //Account and Profile must have a user set
        newAccount.setUser(newUser);
        newProfile.setUser(newUser);

        //We also set Account and Profile in our new User and then save it
        newUser.setAccount(newAccount);
        newUser.setProfile(newProfile);

        User savedUser = userRepository.save(newUser);

        if (savedUser != null) {
            try {
                emailService.sendRegistrationToken(savedUser.getEmail(), verificationTokenService.createNewUserVerificationToken(savedUser));
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
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


}
