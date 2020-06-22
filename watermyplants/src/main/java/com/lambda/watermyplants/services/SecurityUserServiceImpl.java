package com.lambda.watermyplants.services;

import com.lambda.watermyplants.exceptions.ResourceNotFoundException;
import com.lambda.watermyplants.models.User;
import com.lambda.watermyplants.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * This implements User Details Service that allows us to authenticate a user.
 */
@Service(value = "securityUserService")
public class SecurityUserServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String s)
            throws
            ResourceNotFoundException {
        User user = userRepository.findByUsername(s.toLowerCase());
        if (user == null) {
            throw new ResourceNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.getAuthority());
    }
}
