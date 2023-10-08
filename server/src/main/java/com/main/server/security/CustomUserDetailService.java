package com.main.server.security;

import com.main.server.entity.User;
import com.main.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String gmail) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByGmail(gmail);
        return user.map(CustomUserDetail::new)
                .orElseThrow(() -> new UsernameNotFoundException("user " + gmail + " not found"));
    }
}
