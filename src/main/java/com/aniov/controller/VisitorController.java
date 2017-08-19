package com.aniov.controller;

import com.aniov.model.dto.GenericResponseDTO;
import com.aniov.model.dto.VisitorDTO;
import com.aniov.service.UserService;
import com.aniov.service.VisitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Visitor controller
 */
@RestController
public class VisitorController {

    @Autowired
    private VisitorService visitorService;

    @Autowired
    private UserService userService;

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

        return new ResponseEntity<>(visitors, HttpStatus.OK);
    }

    @DeleteMapping(path = "/visitor")
    public ResponseEntity<?> deleteVisitor(@RequestParam(name = "id") Long id) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String authUsername = auth.getName();

        if (visitorService.deleteVisitorFromMyList(authUsername, id)){
            return new ResponseEntity<Object>(new GenericResponseDTO("Visitor was deleted"),HttpStatus.OK);
        }
        return new ResponseEntity<Object>(new GenericResponseDTO("Bad request"),HttpStatus.BAD_REQUEST);
    }

}
