package com.restaurant.tablebookingapp.JWT;

import com.restaurant.tablebookingapp.entity.Privilege;
import com.restaurant.tablebookingapp.entity.Role;
import com.restaurant.tablebookingapp.entity.User;
import com.restaurant.tablebookingapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class CustomerUserDetailsService implements UserDetailsService {

Logger logger = Logger.getLogger(CustomerUserDetailsService.class.getName());
    @Autowired
    UserRepository userRepository;

    /*@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Inside loadUserByUsername ");
        com.restaurant.tablebookingapp.entity.User userDetail = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new User(userDetail.getEmail(), userDetail.getPassword(), new ArrayList<>());

    }*/

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final Optional<User> user = userRepository.findByEmail(username);
        if (user.isEmpty()) {
           // logger.warn("user not found: {}", username);
            throw new UsernameNotFoundException("User " + username + " not found");
        }
        return UserPrinciple.builder()
                .userId(user.get().getId())
                .email(user.get().getEmail())
                .authorities(getAuthorities(user.get().getRoles()))
                .password(user.get().getPassword())
                .build();
    }
    private Collection<? extends GrantedAuthority> getAuthorities(
            Collection<Role> roles) {

        return getGrantedAuthorities(getPrivileges(roles));
    }

    private List<String> getPrivileges(Collection<Role> roles) {

        List<String> privileges = new ArrayList<>();
        List<Privilege> collection = new ArrayList<>();
        for (Role role : roles) {
            privileges.add(role.getName());
            collection.addAll(role.getPrivileges());
        }
        for (Privilege item : collection) {
            privileges.add(item.getName());
        }
        return privileges;
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }


}
