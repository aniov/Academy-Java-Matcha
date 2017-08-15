package com.aniov.service;

import com.aniov.model.Profile;
import com.aniov.model.SiteUserDetails;
import com.aniov.model.User;
import com.aniov.model.dto.ProfileDTO;
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
    private UserService userService;

    @Autowired
    @Qualifier("sessionRegistry")
    private SessionRegistry sessionRegistry;

    public Profile findByUserName(String username) {

        User user = userService.findUserByUserName(username);
        return profileRepository.findByUserId(user.getId());
    }

   public Profile saveProfile(ProfileDTO profileDTO, String username){

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

       List<Profile> profiles = profileRepository.findAll();
       profiles = setOnlineProfiles(profiles);

       List<ProfileDTO> profileDTOS = new ArrayList<>();

       for (Profile profile : profiles) {
           if (!profile.getId().equals(authUserProfile.getId())) {
               profileDTOS.add(new ProfileDTO(profile));
           }
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

       System.out.println("SIZEEEE: " + loggedProfiles.size() + " " + profiles.size());
       for (Profile profile : profiles) {
           if (loggedProfiles.contains(profile)) {

               System.out.println("CONTANES: " );

               profile.setOnline(true);
           }
       }
       return profiles;
   }

}
