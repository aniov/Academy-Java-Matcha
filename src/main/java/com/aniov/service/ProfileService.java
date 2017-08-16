package com.aniov.service;

import com.aniov.model.*;
import com.aniov.model.dto.ProfileDTO;
import com.aniov.repository.AccountRepository;
import com.aniov.repository.InterestRepository;
import com.aniov.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Profile service for user
 */
@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private InterestRepository interestRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @Autowired
    @Qualifier("sessionRegistry")
    private SessionRegistry sessionRegistry;

    public Profile findByUserName(String username) {

        User user = userService.findUserByUserName(username);
        return profileRepository.findByUserId(user.getId());
    }

    public Profile saveProfile(ProfileDTO profileDTO, String username) {

        Profile profile = findByUserName(username);
        profile.edit(profileDTO);
        return profileRepository.saveAndFlush(profile);
    }

    public Profile saveProfileEntity(Profile profile) {
        return profileRepository.saveAndFlush(profile);
    }

    public List<ProfileDTO> getAllProfiles() {
        List<Profile> profiles = profileRepository.findAll();
        List<ProfileDTO> profileDTOS = new ArrayList<>();

        for (Profile profile : profiles) {
            profileDTOS.add(new ProfileDTO(profile));
        }
        return profileDTOS;
    }

    public List<ProfileDTO> getMatchingProfiles() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String authUsername = auth.getName();
        Profile authUserProfile = userService.findUserByUserName(authUsername).getProfile();

        List<Account> accounts = accountService.findAllAccountOk();
        List<Profile> profiles = new ArrayList<>();

        for (Account account : accounts) {
            profiles.add(account.getUser().getProfile());
        }

        profiles = setOnlineProfiles(profiles);

        List<ProfileDTO> profileDTOS = new ArrayList<>();

        for (Profile profile : profiles) {
            if (!profile.getId().equals(authUserProfile.getId())) {
                profileDTOS.add(new ProfileDTO(profile));
            }
        }
        return profileDTOS;
    }

    public void addInterest(String interest, String username) {

        Profile profile = findByUserName(username);
        Interest retrievedInterest = interestRepository.findByInterest(interest);

        if (retrievedInterest == null) {
            profile.addInterest(new Interest(interest));
        } else {
            profile.addInterest(retrievedInterest);
        }
        profileRepository.save(profile);
    }

    public Set<Interest> getAllInterest(String username) {
        Profile profile = findByUserName(username);
        return profile.getInterests();
    }

    private List<Profile> setOnlineProfiles(List<Profile> profiles) {

        List<Object> principals = sessionRegistry.getAllPrincipals();
        Set<Profile> loggedProfiles = new LinkedHashSet<>();

        for (Object principal : principals) {
            if (principal instanceof SiteUserDetails) {
                loggedProfiles.add(((SiteUserDetails) principal).getUser().getProfile());
            }
        }

        for (Profile profile : profiles) {
            if (loggedProfiles.contains(profile)) {
                profile.setOnline(true);
            }
        }
        return profiles;
    }

}
