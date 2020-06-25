package com.lambda.watermyplants.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lambda.watermyplants.models.Role;
import com.lambda.watermyplants.models.User;
import com.lambda.watermyplants.models.UserRole;
import com.lambda.watermyplants.services.UserService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
public class UserControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private List<User> userList;

    @Before
    public void setUp() throws Exception {
        userList = new ArrayList<>();

        Role r1 = new Role("admin");
        r1.setRoleid(1);
        Role r2 = new Role("user");
        r2.setRoleid(2);

        // admin, user1
        ArrayList<UserRole> admins = new ArrayList<>();
        admins.add(new UserRole(new User(), r1));
        admins.add(new UserRole(new User(), r2));

        User u1 = new User("admin", "password", "", admins);
        u1.setUserid(1);
        userList.add(u1);

        //  user2
        ArrayList<UserRole> users = new ArrayList<>();
        users.add(new UserRole(new User(), r2));

        User u2 = new User("testuser", "password", "000-000-0000", users);
        u2.setUserid(2);
        userList.add(u2);

        // user3
        users = new ArrayList<>();
        users.add(new UserRole(new User(), r2));

        User u3 = new User("lambda", "school", "000-000-0000", users);
        u3.setUserid(3);
        userList.add(u3);

        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getAllUsers() throws Exception {
        String apiUrl = "/api/users";

        Mockito.when(userService.findAllUsers())
                .thenReturn(userList);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(apiUrl)
                .accept(MediaType.APPLICATION_JSON);

        // the following actually performs a real controller call
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andReturn(); // this could throw an exception
        String actual = mvcResult.getResponse()
                .getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String expected = mapper.writeValueAsString(userList);

        System.out.println("Expect: " + expected);
        System.out.println("Actual: " + actual);

        assertEquals("Rest API Returns List", expected, actual);
    }

    @Test
    public void getUserById() throws Exception {
        String apiUrl = "/api/users/1";

        Mockito.when(userService.findUserById(1))
                .thenReturn(userList.get(1));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(apiUrl)
                .accept(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andReturn(); // this could throw an exception
        String actual = mvcResult.getResponse()
                .getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String expected = mapper.writeValueAsString(userList.get(1));

        System.out.println("Expect: " + expected);
        System.out.println("Actual: " + actual);

        assertEquals("Rest API Returns List", expected, actual);
    }

    @Test
    public void getCurrentUserInfo() throws Exception {
        String apiUrl = "/api/users/info";

        Mockito.when(userService.findUserByUserName(anyString()))
                .thenReturn(userList.get(0));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(apiUrl)
                .accept(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andReturn(); // this could throw an exception
        String actual = mvcResult.getResponse()
                .getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String expected = mapper.writeValueAsString(userList.get(0));

        System.out.println("Expect: " + expected);
        System.out.println("Actual: " + actual);

        assertEquals("Rest API Returns List", expected, actual);
    }

    @Test
    public void addNewUser() throws Exception {
        String apiUrl = "/api/users/user";

        // build a user
        Role r2 = new Role("user");
        r2.setRoleid(2);
        ArrayList<UserRole> users = new ArrayList<>();
        users.add(new UserRole(new User(), r2));
        User u1 = new User();
        u1.setUserid(5);
        u1.setUsername("tiger");
        u1.setPassword("ILuvM4th!");

        ObjectMapper mapper = new ObjectMapper();
        String userString = mapper.writeValueAsString(u1);

        Mockito.when(userService.save(any(User.class)))
                .thenReturn(u1);

        RequestBuilder rb = MockMvcRequestBuilders.post(apiUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(userString);

        mockMvc.perform(rb)
                .andExpect(status().isCreated())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void updateFullUser() throws Exception {
        String apiUrl = "/api/users/1";

        Role r2 = new Role("user");
        r2.setRoleid(2);
        ArrayList<UserRole> users = new ArrayList<>();
        users.add(new UserRole(new User(), r2));
        User update = new User("test", "password", "000-000-0000", users);
        update.setUserid(2);

        Mockito.when(userService.save(update))
                .thenReturn(userList.get(1));

        ObjectMapper mapper = new ObjectMapper();
        String userString = mapper.writeValueAsString(update);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put(apiUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(userString);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is2xxSuccessful())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void updateUser() throws Exception {
        String apiUrl = "/api/users/1";

        Role r2 = new Role("user");
        r2.setRoleid(2);
        ArrayList<UserRole> users = new ArrayList<>();
        users.add(new UserRole(new User(), r2));
        User update = new User("testtest", "password", "000-000-0000", users);
        update.setUserid(2);

        Mockito.when(userService.update(update, 2))
                .thenReturn(userList.get(1));

        ObjectMapper mapper = new ObjectMapper();
        String userString = mapper.writeValueAsString(update);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.patch(apiUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(userString);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is2xxSuccessful())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void deleteUserById() throws Exception {
        String apiUrl = "/api/users/3";

        RequestBuilder rb = MockMvcRequestBuilders.delete(apiUrl);

        mockMvc.perform(rb)
                .andExpect(status().is2xxSuccessful())
                .andDo(MockMvcResultHandlers.print());
    }
}