package com.lambda.watermyplants.controllers;

import com.lambda.watermyplants.models.ErrorDetail;
import com.lambda.watermyplants.models.User;
import com.lambda.watermyplants.services.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "return a list of all users (**ADMIN**)", response = User.class, responseContainer = "List")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping(value = "/api/users", produces = {"application/json"})
    public ResponseEntity<?> getAllUsers() {

        List<User> userList = userService.findAllUsers();
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @ApiOperation(value = "retrieve a user based off user id (**ADMIN**)", response = User.class)
    @ApiResponses(value = {@ApiResponse(code = 200,
            message = "User Found",
            response = User.class), @ApiResponse(code = 404,
            message = "User Not Found",
            response = ErrorDetail.class)})
    @GetMapping(value = "/api/users/{userid}", produces = {"application/json"})
    public ResponseEntity<?> getUserById(@PathVariable long userid) {

        User user = userService.findUserById(userid);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @ApiOperation(value = "return the currently authenticated user", response = User.class)
    @GetMapping(value = "/api/users/info", produces = {"application/json"})
    public ResponseEntity<?> getCurrentUserInfo(Authentication authentication) {

        User user = userService.findUserByUserName(authentication.getName());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @ApiOperation(value = "add a user given in the request body (**ADMIN**)", response = Void.class)
    @PostMapping(value = "/api/users/user", consumes = {"application/json"})
    public ResponseEntity<?> addNewUser(@Valid @RequestBody User newUser) {

        newUser.setUserid(0);
        newUser = userService.save(newUser);

        HttpHeaders responseHeaders = new HttpHeaders();
        URI newUserURI = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{userid}")
                .buildAndExpand(newUser.getUserid())
                .toUri();
        responseHeaders.setLocation(newUserURI);

        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }

    @ApiOperation(value = "update a user given in the request body", response = Void.class)
    @ApiResponses(value = {@ApiResponse(code = 200,
            message = "User Found",
            response = User.class), @ApiResponse(code = 404,
            message = "User Not Found",
            response = ErrorDetail.class)})
    @PutMapping(value = "/api/users/{userid}", consumes = {"application/json"})
    public ResponseEntity<?> updateFullUser(@Valid @RequestBody User updateUser, @PathVariable long userid) {

        updateUser.setUserid(userid);
        userService.save(updateUser);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "update a user with the information given in the request body", response = Void.class)
    @ApiResponses(value = {@ApiResponse(code = 200,
            message = "User Found",
            response = User.class), @ApiResponse(code = 404,
            message = "User Not Found",
            response = ErrorDetail.class)})
    @PatchMapping(value = "/api/users/{userid}", consumes = {"application/json"})
    public ResponseEntity<?> updateUser(@RequestBody User updateUser, @PathVariable long userid) {

        userService.update(updateUser, userid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "delete the given user", response = Void.class)
    @ApiResponses(value = {@ApiResponse(code = 200,
            message = "User Found",
            response = User.class), @ApiResponse(code = 404,
            message = "User Not Found",
            response = ErrorDetail.class)})
    @DeleteMapping(value = "/api/users/{userid}")
    public ResponseEntity<?> deleteUserById(@PathVariable long userid) {

        userService.deleteById(userid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}


