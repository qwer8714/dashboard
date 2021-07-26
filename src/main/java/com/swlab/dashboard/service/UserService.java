package com.swlab.dashboard.service;

import com.swlab.dashboard.dto.UserDto;
import com.swlab.dashboard.model.user.User;
import com.swlab.dashboard.model.user.UserRole;
import com.swlab.dashboard.repository.Jpa.JpaUserRepository;
import com.swlab.dashboard.repository.UserRepository;
import com.swlab.dashboard.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final JpaUserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public User save(UserDto userDto) {
        User user = User.builder()
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .name(userDto.getName())
                .phoneNo(userDto.getPhoneNo())
                .build();
        return userRepository.save(user);
    }

    public User join(UserDto userDto) {
        User user = save(userDto);
        saveUserRole(user);
        return user;
    }

    private UserRole saveUserRole(User user) {
        return userRoleRepository.save(UserRole.builder().user(user).roleType(UserRole.RoleType.USER).build());
    }
}
