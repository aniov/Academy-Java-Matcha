package com.aniov.service;

import com.aniov.model.Profile;
import com.aniov.model.User;
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

   /* public Profile findByUserName(String username) {
        System.out.println("Here");
        User user = userService.findUserByUserName(username);
        System.out.println("after " + user);
        System.out.println("Profile: " + profileRepository.findByUser(user));
        return profileRepository.findByUser(user);
    }*/
}
