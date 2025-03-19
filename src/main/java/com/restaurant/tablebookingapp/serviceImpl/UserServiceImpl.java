package com.restaurant.tablebookingapp.serviceImpl;

import com.restaurant.tablebookingapp.JWT.JwtAuthenticationFilter;
import com.restaurant.tablebookingapp.entity.Role;
import com.restaurant.tablebookingapp.entity.User;
import com.restaurant.tablebookingapp.repository.PrivilegeRepository;
import com.restaurant.tablebookingapp.repository.RoleRepository;
import com.restaurant.tablebookingapp.repository.UserRepository;
import com.restaurant.tablebookingapp.service.UserService;
import com.restaurant.tablebookingapp.utils.CafeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static com.restaurant.tablebookingapp.constants.CafeConstants.INVALID_DATA;
import static com.restaurant.tablebookingapp.constants.CafeConstants.SOMETHING_WENT_WRONG;

@Service

public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PrivilegeRepository privilegeRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
@Autowired
private JwtAuthenticationFilter  jwtAuthenticationFilter;
    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        // log.info("Inside signUp {}", requestMap);
        try {
            if (validateSignUpMap(requestMap)) {
                Optional<User> userObj = userRepository.findByEmail(requestMap.get("email"));
                if (userObj.isEmpty()) {


                    userRepository.save(getUserFromMap(requestMap));
                    return CafeUtils.getResponseEntity("User Successfully Created.", HttpStatus.OK);
                } else {
                    return CafeUtils.getResponseEntity("Email already Exists. ", HttpStatus.BAD_REQUEST);
                }

            } else {
                return CafeUtils.getResponseEntity(INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateSignUpMap(Map<String, String> requestMap) {
        return requestMap.containsKey("name") && requestMap.containsKey("contactNumber") && requestMap.containsKey("email") && requestMap.containsKey("password");
    }

    private User getUserFromMap(Map<String, String> requestMap) {
        User user = new User();

        user.setName(requestMap.get("name"));
        user.setEmail(requestMap.get("email"));
        user.setContactNumber(requestMap.get("contactNumber"));
        user.setPassword(passwordEncoder.encode(requestMap.get("password")));
        user.setStatus("false");

        Role userRole = roleRepository.findByName("ROLE_USER");
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(userRole);

        user.setRoles(roleSet);
        userRepository.save(user);
        return user;
    }
}
