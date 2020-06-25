package com.lambda.watermyplants.controllers;

import com.lambda.watermyplants.models.User;
import com.lambda.watermyplants.models.UserMinimum;
import com.lambda.watermyplants.models.UserRole;
import com.lambda.watermyplants.services.RoleService;
import com.lambda.watermyplants.services.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * The class allows access to endpoints that are open to all users regardless of authentication status.
 * Its most important function is to allow a person to create their own username
 */
@RestController
public class OpenController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @ApiOperation(value = "create a new user, return an access token", response = String.class, responseContainer = "List")
    @PostMapping(value = "/api/auth/register",
            consumes = {"application/json"},
            produces = {"application/json"})
    public ResponseEntity<?> addSelf(HttpServletRequest httpServletRequest, @Valid @RequestBody UserMinimum user) throws URISyntaxException {
        // Create the user
        User newUser = new User();

        newUser.setUsername(user.getUsername());
        newUser.setPassword(user.getPassword());
        newUser.setPhonenumber(user.getPhonenumber());

        // add the default role of user
        List<UserRole> newRoles = new ArrayList<>();
        newRoles.add(new UserRole(newUser,
                roleService.findByRoleName("user")));
        newUser.setRoles(newRoles);

        newUser = userService.save(newUser);

        // set the location header for the newly created resource
        // The location comes from a different controller!
        HttpHeaders responseHeaders = new HttpHeaders();
        URI newUserURI = ServletUriComponentsBuilder.fromUriString(httpServletRequest.getServerName() + ":" + httpServletRequest.getLocalPort() + "/users/user/{userId}")
                .buildAndExpand(newUser.getUserid())
                .toUri();
        responseHeaders.setLocation(newUserURI);

        // return the access token
        // To get the access token, surf to the endpoint /login just as if a client had done this.
        RestTemplate restTemplate = new RestTemplate();
        String port = "";
        if (httpServletRequest.getServerName().equalsIgnoreCase("localhost")) {
            port = ":" + httpServletRequest.getLocalPort();
        }
        String requestURI = "http://" + httpServletRequest.getServerName() + port + "/api/auth/login";

        List<MediaType> acceptableMediaTypes = new ArrayList<>();
        acceptableMediaTypes.add(MediaType.APPLICATION_JSON);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(acceptableMediaTypes);
        headers.setBasicAuth(System.getenv("OAUTHCLIENTID"),
                System.getenv("OAUTHCLIENTSECRET"));

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type",
                "password");
        map.add("scope",
                "read write trust");
        map.add("username",
                user.getUsername());
        map.add("password",
                user.getPassword());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map,
                headers);

        String theToken = restTemplate.postForObject(requestURI,
                request,
                String.class);

        return new ResponseEntity<>(theToken,
                responseHeaders,
                HttpStatus.CREATED);

    }

    /**
     * Prevents no favicon.ico warning from appearing in the logs. @ApiIgnore tells Swagger to ignore documenting this as an endpoint.
     */
    @ApiIgnore
    @GetMapping("favicon.ico")
    public void returnNoFavicon() {
    }

}
