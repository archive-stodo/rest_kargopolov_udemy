package com.appsdeveloperblog.app.ws.controller;

import com.appsdeveloperblog.app.ws.exception.UserServiceException;
import com.appsdeveloperblog.app.ws.model.request.UserDetailsRequestModel;
import com.appsdeveloperblog.app.ws.model.response.*;
import com.appsdeveloperblog.app.ws.service.UserService;
import com.appsdeveloperblog.app.ws.shared.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.appsdeveloperblog.app.ws.model.response.RequestOperationName.DELETE;
import static com.appsdeveloperblog.app.ws.model.response.RequestOperationStatus.SUCCESS;
import static java.util.stream.Collectors.toList;
import static org.springframework.beans.BeanUtils.copyProperties;

@RestController
@RequestMapping("users") //http://localhost:8080/users
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping(path="/{id}", produces = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE
    })
    public UserRestResponse getUser(@PathVariable String id) {
        UserRestResponse userRestResponse = new UserRestResponse();
        UserDto userDto = userService.getUserByUserId(id);

        copyProperties(userDto, userRestResponse);

        return userRestResponse;
    }

    @PostMapping(consumes = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE},
    produces = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE})
    public UserRestResponse createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {
        if(userDetails.getFirstName().isEmpty()){
            throw new Exception(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
        }

        UserRestResponse userRestResponse = new UserRestResponse();

        ModelMapper modelMapper = new ModelMapper();
        UserDto userDto = modelMapper.map(userDetails, UserDto.class);

        UserDto createdUser = userService.createUser(userDto);
        userRestResponse = modelMapper.map(createdUser, UserRestResponse.class);

        return userRestResponse;
    }

    @PutMapping(path = "/{id}",
        consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
        produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public UserRestResponse updateUser(@PathVariable String id, @RequestBody UserDetailsRequestModel userDetails) {
        UserRestResponse userRestResponse = new UserRestResponse();

        UserDto userDto = new UserDto();
        copyProperties(userDetails, userDto);

        UserDto createdUser = userService.updateUser(id, userDto);
        copyProperties(createdUser, userRestResponse);

        return userRestResponse;
    }

    @DeleteMapping(path = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public OperationStatusModel deleteUser(@PathVariable String id) {
        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName(DELETE.name());

        userService.deleteUser(id);
        returnValue.setOperationResult(SUCCESS.name());

        return returnValue;
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<UserRestResponse> getUsers(@RequestParam(value="page", defaultValue = "0") int page,
                                           @RequestParam(value="limit", defaultValue = "25") int limit)
    {
        List<UserDto> users = userService.getUsers(page, limit);

        return users.stream()
                .map(user -> {
                    UserRestResponse userRestResponse = new UserRestResponse();
                    copyProperties(user, userRestResponse);
                    return userRestResponse;
                }).collect(toList());
    }


}
