package com.restaurant.tablebookingapp.restImpl;

import com.restaurant.tablebookingapp.rest.UserRest;
import com.restaurant.tablebookingapp.service.UserService;
import com.restaurant.tablebookingapp.serviceImpl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static com.restaurant.tablebookingapp.constants.CafeConstants.SOMETHING_WENT_WRONG;
import static com.restaurant.tablebookingapp.utils.CafeUtils.getResponseEntity;

@RestController
@Slf4j
public class UserRestImpl implements UserRest {
    @Autowired
    UserService userService;
    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        try{

            return userService.signUp(requestMap);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return getResponseEntity(SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<String> test(Map<String, String> requestMap) {
        try{
            log.info("inside test method");
            return userService.signUp(requestMap);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return getResponseEntity(SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    }
}
