package com.lambda.watermyplants.services;

import com.lambda.watermyplants.WatermyplantsApplication;
import com.lambda.watermyplants.models.User;
import com.lambda.watermyplants.models.UserRole;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WatermyplantsApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void a_findAllUsers() {
        assertEquals(5, userService.findAllUsers().size());
    }

    @Test
    public void b_findUserById() {
        assertEquals("admin", userService.findUserById(3).getUsername());
    }

    @Test
    public void c_findUserByUserName() {
        assertEquals(3, userService.findUserByUserName("admin").getUserid());
    }

    @Test
    public void e_save() {
        ArrayList<UserRole> admins = new ArrayList<>();
        admins.add(new UserRole(new User(), roleService.findByRoleName("admin")));
        User newUser = new User("new", "password", "special", admins);
        newUser.setUserid(0);

        userService.save(newUser);
        assertEquals("special", userService.findUserByUserName("new").getPhonenumber());
    }

    @Test
    @WithUserDetails("admin test")
    public void f_saveput() {
        ArrayList<UserRole> admins = new ArrayList<>();
        admins.add(new UserRole(new User(), roleService.findByRoleName("admin")));
        User updateUser = new User("newadmin", "password", "special", admins);
        updateUser.setUserid(3);

        userService.save(updateUser);
        assertEquals(3, userService.findUserByUserName("newadmin").getUserid());
    }

    @Test
    public void g_deleteById() {
        userService.deleteById(3);
        assertEquals(5, userService.findAllUsers().size());
    }

    @Test
    @WithUserDetails("admin")
    public void d_update() {
        ArrayList<UserRole> admins = new ArrayList<>();
        admins.add(new UserRole(new User(), roleService.findByRoleName("admin")));
        User update = new User("admin test", "password", "000-000-0000", admins);

        userService.update(update, 3);
        assertEquals("admin test", userService.findUserById(3).getUsername());
    }

    @Test
    public void addUserRole() {

    }

    @Test
    public void deleteUserRole() {
    }
}