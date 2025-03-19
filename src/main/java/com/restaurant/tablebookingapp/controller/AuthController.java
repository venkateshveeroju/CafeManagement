package com.restaurant.tablebookingapp.controller;

import com.restaurant.tablebookingapp.JWT.CustomerUserDetailsService;
import com.restaurant.tablebookingapp.JWT.JwtIssuer;
import com.restaurant.tablebookingapp.JWT.JwtUtil;
import com.restaurant.tablebookingapp.JWT.UserPrinciple;
import com.restaurant.tablebookingapp.model.AuthenticationRequest;
import com.restaurant.tablebookingapp.utils.AuthenticationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private CustomerUserDetailsService customerUserDetailsService;
    @Autowired
    private final JwtIssuer jwtIssuer;
    @Autowired
    private com.restaurant.tablebookingapp.repository.UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    public AuthController(JwtIssuer jwtIssuer) {
        this.jwtIssuer = jwtIssuer;
    }

    @PostMapping(path = "/login")
    public AuthenticationResponse authentification(@RequestBody @NotNull AuthenticationRequest authenticationRequest) {

        String email = authenticationRequest.getEmail();
        String password = authenticationRequest.getPassword();
        if (email == null || password == null) {
            throw new IllegalArgumentException("Email and password are must to login");
        }
        AuthenticationResponse loginResponse = null;
        try {
            if (userRepository.findByEmail(email).isEmpty()) {
                logger.error("User does not exist in the system with the email :  " + email);
                throw new UsernameNotFoundException("User does not exist in the system ");
            }
            var authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            var principle = (UserPrinciple) authentication.getPrincipal();
            var roles = principle.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            var token = jwtIssuer.issue(principle.userId(), principle.email(), roles);
            loginResponse = new AuthenticationResponse(token);

        } catch (BadCredentialsException bcEx) {
            logger.error("Credentials you provided were wrong, please check");
        } catch (NullPointerException e) {
            logger.error("User does not exist ", e.getMessage());
        } catch (NoSuchElementException ex) {
            logger.error("Cannot set user authentication with userId: {}", ex.getMessage());
            throw new NoSuchElementException("Cannot set user authentication with userId: " + ex.getMessage());
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e.getMessage());
        }
        return loginResponse;
    }

    @PostMapping(path = "/auth/admin")
    public ResponseEntity<?> authentificationAdmin(@RequestBody @NotNull AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword())

            );
        } catch (BadCredentialsException bce) {
            throw new BadCredentialsException("Incorrect username or password");
        }

        //return CafeUtils.getResponseEntity("Authentification Success", HttpStatus.OK);
        final UserDetails userDetails = customerUserDetailsService.loadUserByUsername(authenticationRequest.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails.getUsername(), "admin");
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    @PostMapping(path = "/auth/manager")
    public ResponseEntity<?> authentificationManager(@RequestBody @NotNull AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword())

            );
        } catch (BadCredentialsException bce) {
            throw new BadCredentialsException("Incorrect username or password");
        }

        //return CafeUtils.getResponseEntity("Authentification Success", HttpStatus.OK);
        final UserDetails userDetails = customerUserDetailsService.loadUserByUsername(authenticationRequest.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails.getUsername(), "manager");
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    @PostMapping(path = "/auth/waiter")
    public ResponseEntity<?> authentificationWaiter(@RequestBody @NotNull AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword())

            );
        } catch (BadCredentialsException bce) {
            throw new BadCredentialsException("Incorrect username or password");
        }

        //return CafeUtils.getResponseEntity("Authentification Success", HttpStatus.OK);
        final UserDetails userDetails = customerUserDetailsService.loadUserByUsername(authenticationRequest.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails.getUsername(), "waiter");
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    @PostMapping(path = "/hello")
    public ResponseEntity<?> authentificationChef(@RequestBody @NotNull AuthenticationRequest authenticationRequest) {

        return ResponseEntity.ok("hello world");
    }

}
