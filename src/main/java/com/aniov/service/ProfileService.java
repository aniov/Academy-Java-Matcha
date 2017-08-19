package com.aniov.service;

import com.aniov.model.*;
import com.aniov.model.dto.ProfileDTO;
import com.aniov.repository.InterestRepository;
import com.aniov.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
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

    private final int RESULTS_PER_PAGE = 6;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private InterestRepository interestRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private SessionRegistry sessionRegistry;

    public Profile findByUserName(String username) {

        User user = userService.findUserByUserName(username);
        Profile profile = profileRepository.findOne(user.getId());
        profile = setOnlineProfile(profile);
        return profile;
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

    /**
     * Find all matching profile by our criteria
     *
     * @param pageNumber page requested
     * @return Page<ProfileDTO>
     */
    public Page<ProfileDTO> getMatchingProfiles(int pageNumber) {

        Pageable request = new PageRequest(pageNumber, RESULTS_PER_PAGE, Sort.Direction.ASC, "id");
        Page<Profile> profiles = profileRepository.findDistinctProfilesByUserAccountEnabledIsTrueAndIdNot(getLoggedUserProfileId(), request);
        profiles = setProfilesOnLine(profiles);
        return profiles.map(ProfileDTO::new);
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
     * @param page     requested page number
     * @return Page<ProfileDTO>
     */
    public Page<ProfileDTO> findProfilesByInterest(String interest, int page) {

        Pageable request = new PageRequest(page, RESULTS_PER_PAGE, Sort.Direction.ASC, "id");
        Page<Profile> profiles = profileRepository.findDistinctProfilesByUserAccountEnabledIsTrueAndInterestsInterestIgnoreCaseContainingAndIdNot(interest, getLoggedUserProfileId(), request);
        profiles = setProfilesOnLine(profiles);

        return profiles.map(ProfileDTO::new);
    }

    /**
     * Find profiles containing location (address)
     *
     * @param locationlike address (city, country)
     * @param page         requested page number
     * @return Page<ProfileDTO>
     */
    public Page<ProfileDTO> findProfilesByLocation(String locationlike, int page) {

        Pageable request = new PageRequest(page, RESULTS_PER_PAGE, Sort.Direction.ASC, "id");
        Page<Profile> profiles = profileRepository.findDistinctProfilesByUserAccountEnabledIsTrueAndAddressIgnoreCaseContainingAndIdNot(locationlike, getLoggedUserProfileId(), request);
        profiles = setProfilesOnLine(profiles);

        return profiles.map(ProfileDTO::new);
    }

    /**
     * Find profiles by username
     *
     * @param namecontaining username
     * @param page           requested page number
     * @return Page<ProfileDTO>
     */
    public Page<ProfileDTO> finByNameContaining(String namecontaining, int page) {

        Pageable request = new PageRequest(page, RESULTS_PER_PAGE, Sort.Direction.ASC, "id");
        Page<Profile> profiles = profileRepository.findDistinctProfilesByUserAccountEnabledIsTrueAndUserUsernameIgnoreCaseContainingAndIdNot(namecontaining, getLoggedUserProfileId(), request);
        profiles = setProfilesOnLine(profiles);

        return profiles.map(ProfileDTO::new);
    }

    private Profile setOnlineProfile(Profile profile) {

        List<Object> principals = sessionRegistry.getAllPrincipals();
        Set<Profile> loggedProfiles = new LinkedHashSet<>();

        for (Object principal : principals) {
            if (principal instanceof SiteUserDetails) {
                loggedProfiles.add(((SiteUserDetails) principal).getUser().getProfile());
            }
        }
        if (loggedProfiles.contains(profile)) {
            profile.setOnline(true);
        }
        return profile;
    }

    private Page<Profile> setProfilesOnLine(Page<Profile> profiles) {

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

    private Long getLoggedUserProfileId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String authUsername = auth.getName();
        return userService.findUserByUserName(authUsername).getId();
    }

}
