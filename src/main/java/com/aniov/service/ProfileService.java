package com.aniov.service;

import com.aniov.model.Profile;
import com.aniov.model.User;
import com.aniov.model.dto.ProfileDTO;
import com.aniov.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
       return profileRepository.save(profile);
   }
}
