package com.itananina.weblamp.weblamp.controllers;

import com.itananina.weblamp.weblamp.converters.DtoConverter;
import com.itananina.weblamp.weblamp.dto.JwtRequest;
import com.itananina.weblamp.weblamp.dto.JwtResponse;
import com.itananina.weblamp.weblamp.exceptions.IncorrectUsernameOrPasswordException;
import com.itananina.weblamp.weblamp.services.JwtTokenUtil;
import com.itananina.weblamp.weblamp.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    private final DtoConverter converter;

    //ЗДЕСЬ ВЫДАЕМ ТОКЕН
    @PostMapping("/auth")
    public JwtResponse createAuthToken(@RequestBody JwtRequest authRequest) {
        try {
            //МАНАГЕР ЧЕРЕЗ UserDetailsService сверяет есть ли такое с логином-паролем
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new IncorrectUsernameOrPasswordException("Incorrect username or password");
        }
        UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());
        String token = jwtTokenUtil.generateToken(userDetails);
        return new JwtResponse(token);
    }

    @PostMapping("/sign-up")
    public JwtResponse createUser(@RequestBody JwtRequest userRequest) {
        UserDetails userDetails = userService.createUser(converter.jwtRequestToUser(userRequest));
        String token = jwtTokenUtil.generateToken(userDetails);
        return new JwtResponse(token);
    }
}
