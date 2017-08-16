package com.aniov.service;

import com.aniov.repository.InterestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Interest service class
 */
@Service
public class InterestService {

    @Autowired
    private InterestRepository interestRepository;

}
