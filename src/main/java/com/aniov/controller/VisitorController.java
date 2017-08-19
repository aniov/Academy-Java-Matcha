package com.aniov.controller;

import com.aniov.model.dto.VisitorDTO;
import com.aniov.service.VisitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Visitor controller
 */
@RestController
public class VisitorController {

    @Autowired
    private VisitorService visitorService;

    /**
     * Find profiles who visited auth user page
     *
     * @return List<Visitor>
     */
    @GetMapping(path = "/visitors")
    public ResponseEntity<?> getMyVisitors() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String authUsername = auth.getName();
        List<VisitorDTO> visitors = visitorService.findByUsername(authUsername);

        System.out.println(visitors.size());

        return new ResponseEntity<>(visitors, HttpStatus.OK);
    }

}
