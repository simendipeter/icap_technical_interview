package com.icap.api.rest.controllers;

import com.icap.api.rest.converters.DtoConverter;
import com.icap.api.rest.requests.UpdateUserRequest;
import com.icap.api.rest.responses.UserResponse;
import com.icap.axon.common.domain.User;
import com.icap.users.services.UsersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@Api(tags = "Users")
public class UserController {

    private final DtoConverter dtoConverter;
    private final UsersService usersService;

    @Autowired
    public UserController(DtoConverter dtoConverter, UsersService usersService) {
        this.dtoConverter = dtoConverter;
        this.usersService = usersService;
    }

    @GetMapping
    @ApiOperation("Get currently authenticated user information")
    public UserResponse getUser(User user) {
        return dtoConverter.convert(user);
    }

    @PutMapping
    @ApiOperation("Update currently authenticated user information")
    public void updateUser(User user, @RequestBody UpdateUserRequest updateUserRequest) {
        usersService.updateUser(user.getId(), user.getId(),
                updateUserRequest.getEmail(), updateUserRequest.getDisplayName(), updateUserRequest.getPassword());
    }

}
