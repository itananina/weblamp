package com.itananina.weblamp.weblamp.services;

import com.itananina.weblamp.weblamp.entities.Role;
import com.itananina.weblamp.weblamp.entities.User;
import com.itananina.weblamp.weblamp.exceptions.ResourceNotFoundException;
import com.itananina.weblamp.weblamp.exceptions.UserAlreadyExistsException;
import com.itananina.weblamp.weblamp.repositories.RoleRepository;
import com.itananina.weblamp.weblamp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder encoder;

    @Override
    @Transactional //чтобы не было ошибки лэйзи лоада на user.getRoles()
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(String.format("User '%s' not found", username)));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }

    public Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        Set<SimpleGrantedAuthority> authorities = roles.stream()
                .map(role -> role.getAuthorities())
                .flatMap(auths->auths.stream())
                .map(auth->new SimpleGrantedAuthority(auth.getName()))
                .collect(Collectors.toSet());
        List<SimpleGrantedAuthority> rolesList = roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
        return Stream.concat(authorities.stream(), rolesList.stream())
                .collect(Collectors.toList());
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    @Transactional
    public UserDetails createUser(User newUser) throws UserAlreadyExistsException {
        Optional<User> user = findByUsername(newUser.getUsername());
        if(user.isPresent()) {
            throw new UserAlreadyExistsException(String.format("User '%s' already exists", newUser.getUsername()));
        }
        String newPass = encoder.encode(newUser.getPassword());
        newUser.setPassword(newPass);
        Role userRole = roleRepository.findByName("ROLE_USER").orElseThrow(()->new ResourceNotFoundException("ROLE_USER not found"));
        newUser.getRoles().add(userRole);
        newUser = userRepository.save(newUser);
        return new org.springframework.security.core.userdetails.User(newUser.getUsername(), newUser.getPassword(), mapRolesToAuthorities(newUser.getRoles()));
    }
}
