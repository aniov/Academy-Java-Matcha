package com.aniov.service;

import com.aniov.model.Profile;
import com.aniov.model.User;
import com.aniov.model.dto.ProfileDTO;
import com.aniov.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Profile service for user
 */
@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserService userService;

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
       List<ProfileDTO> profileDTOS = new ArrayList<>();

       for (Profile profile : profiles) {
           if (profile.getId() != authUserProfile.getId()) {
               profileDTOS.add(new ProfileDTO(profile));
           }
       }

       return profileDTOS;

   }
}
