package com.example.demo.services.security;

import com.example.demo.controllers.dto.UserInfo;
import com.example.demo.entities.User;
import com.example.demo.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserInfoService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserInfoService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userDeteail = userRepository.findByEmail(email);
        return userDeteail.map(UserInfoDetail::new).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public UserInfo addUser(UserInfo userInfo) {
        User user = new User(null, userInfo.name(),userInfo.email(),passwordEncoder.encode(userInfo.password()), userInfo.roles());
        user = userRepository.save(user);
        return new UserInfo(user.getName(),user.getEmail(), userInfo.password(), userInfo.roles());
    }
}
