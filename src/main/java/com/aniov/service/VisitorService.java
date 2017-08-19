package com.aniov.service;

import com.aniov.model.Profile;
import com.aniov.model.Visitor;
import com.aniov.model.dto.VisitorDTO;
import com.aniov.repository.VisitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VisitorService {

    @Autowired
    private VisitorRepository visitorRepository;

    @Autowired
    private ProfileService profileService;

    /**
     * Finds visitors for username
     *
     * @param username to found Visitors after
     * @return List<VisitorDTO>
     */
    public List<VisitorDTO> findByUsername(String username) {
        List<Visitor> visitors = visitorRepository.findDistinctByProfileUserUsernameOrderByVisitDateDesc(username);
        List<VisitorDTO> visitorDTOS = new ArrayList<>();

        for (Visitor visitor : visitors) {
            Profile profile = profileService.findByUserName(visitor.getWhoVisit());
            visitorDTOS.add(new VisitorDTO(profile, visitor));
        }
        return visitorDTOS;
    }

    /**
     * Create a new Visitor entry intro visited user
     *
     * @param myUserName    username of who is visiting
     * @param whoImVisiting Profile of who is visited
     */
    public void addNewVisit(String myUserName, String whoImVisiting) {

        Profile whoIsVisited = profileService.findByUserName(whoImVisiting);

        Visitor visitor = new Visitor(whoIsVisited, myUserName);
        visitorRepository.save(visitor);
    }
}
