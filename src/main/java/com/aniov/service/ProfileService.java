package com.aniov.service;

import com.aniov.model.*;
import com.aniov.model.dto.ProfileDTO;
import com.aniov.repository.InterestRepository;
import com.aniov.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;

import java.util.*;

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
        profileRepository.saveAndFlush(profile);
    }

    public Set<Interest> getAllInterest(String username) {
        Profile profile = findByUserName(username);
        return profile.getInterests();
    }

    public void deleteInterest(String interest, String username) {

        Profile profile = findByUserName(username);
        Interest retrievedInterest = interestRepository.findByInterest(interest);

        if (profile.getInterests().contains(retrievedInterest)) {
            profile.removeInterest(retrievedInterest);
            profileRepository.saveAndFlush(profile);
        }
    }

    /**
     * Find profiles containing a common interest
     *
     * @param interest name of interest to be searched after
     * @return list of Profiles DTO's
     */
    public List<ProfileDTO> findProfilesByInterest(String interest) {

        Interest foundInterest = interestRepository.findByInterest(interest);
        if (foundInterest == null) {
            return Collections.emptyList();
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String authUsername = auth.getName();
        Profile authUserProfile = userService.findUserByUserName(authUsername).getProfile();

        List<Account> accounts = accountService.findAllAccountOk();
        List<Profile> profiles = new ArrayList<>();

        for (Account account : accounts) {
            profiles.add(account.getUser().getProfile());
        }

        List<Profile> profilesContainingInterest = profileRepository.findAllByInterestsEquals(foundInterest);

        profilesContainingInterest.retainAll(profiles);

        profilesContainingInterest = setOnlineProfiles(profilesContainingInterest);
        List<ProfileDTO> profileDTOS = new ArrayList<>();

        for (Profile profile : profilesContainingInterest) {
            if (!profile.getId().equals(authUserProfile.getId())) {
                profileDTOS.add(new ProfileDTO(profile));
            }
        }
        return profileDTOS;
    }

    public List<ProfileDTO> findProfilesByLocation(String locationlike) {

        List<Profile> profiles = profileRepository.findByAddressIgnoreCaseContaining(locationlike);
        List<Account> accounts = accountService.findAllAccountOk();

        List<Profile> profilesOk = new ArrayList<>();
        for (Account account : accounts) {
            profilesOk.add(account.getUser().getProfile());
        }

        profiles.retainAll(profilesOk);

        List<ProfileDTO> profileDTOS = new ArrayList<>();
        for (Profile profile : profiles) {
            profileDTOS.add(new ProfileDTO(profile));
        }
        return profileDTOS;
    }

    public List<ProfileDTO> finByNameContaining(String namecontaining) {

        List<User> users = userService.findByUserNameContaining(namecontaining);

        List<ProfileDTO> profileDTOS = new ArrayList<>();

        for (User user : users) {
            profileDTOS.add(new ProfileDTO(user.getProfile()));
        }
        return profileDTOS;
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
