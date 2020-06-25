package com.lambda.watermyplants.services;

import com.lambda.watermyplants.WatermyplantsApplication;
import com.lambda.watermyplants.models.Role;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WatermyplantsApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RoleServiceImplTest {

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
    public void a_findAllRoles() {
        assertEquals(2, roleService.findAllRoles().size());
    }

    @Test
    public void b_findRoleById() {
        assertEquals("admin", roleService.findRoleById(1).getRolename().toLowerCase());
    }

    @Test
    public void c_findByRoleName() {
        assertEquals(1, roleService.findByRoleName("admin").getRoleid());
    }

    @Test
    public void e_save() {
        Role role = new Role("newRole");
        role.setRoleid(3);
        roleService.save(role);

        assertEquals(3, roleService.findAllRoles().size());
    }

    @Test
    public void f_deleteById() {
        roleService.deleteById(1);

        assertEquals(2, roleService.findAllRoles().size());
    }

    @Test
    public void d_update() {
        Role role = new Role("ad");
        roleService.update(role, 1);

        assertEquals("ad", roleService.findRoleById(1).getRolename().toLowerCase());
    }
}