package com.appsdeveloperblog.app.ws.controller;

import com.appsdeveloperblog.app.ws.model.request.UserDetailsRequestModel;
import com.appsdeveloperblog.app.ws.model.response.UserRestResponse;
import com.appsdeveloperblog.app.ws.service.UserService;
import com.appsdeveloperblog.app.ws.shared.dto.UserDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users") //http://localhost:8080/users
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping(path="/{id}")
    public UserRestResponse getUser(@PathVariable String id) {
        UserRestResponse userRestResponse = new UserRestResponse();
        UserDto userDto = userService.getUserByUserId(id);

        BeanUtils.copyProperties(userDto, userRestResponse);

        return userRestResponse;
    }

    @PostMapping
    public UserRestResponse createUser(@RequestBody UserDetailsRequestModel userDetails) {
        UserRestResponse userRestResponse = new UserRestResponse();

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails, userDto);

        UserDto createdUser = userService.createUser(userDto);
        BeanUtils.copyProperties(createdUser, userRestResponse);

        return userRestResponse;
    }

    @PutMapping
    public String updateUser() {
        return "update user was called";
    }

    @DeleteMapping
    public String deleteUser() {
        return "delete user was called";
    }
}
