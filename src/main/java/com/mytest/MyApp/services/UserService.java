package com.mytest.MyApp.services;

import com.mytest.MyApp.dto.UserDTO;
import com.mytest.MyApp.models.Role;
import com.mytest.MyApp.models.User;
import com.mytest.MyApp.repositorys.RoleRepository;
import com.mytest.MyApp.repositorys.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(String.format("User %s not found", username)));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), mapRolesToAutorites(user.getRoles()));

    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    private Collection<? extends GrantedAuthority> mapRolesToAutorites(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void createUser(UserDTO userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());
        user.setCreatedAt(LocalDateTime.now());
        user.setRoles(roleRepository.findAllById(1l));
        userRepository.save(user);
    }
}
