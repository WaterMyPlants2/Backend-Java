package com.lambda.watermyplants.services;

import com.lambda.watermyplants.WatermyplantsApplication;
import com.lambda.watermyplants.models.Plant;
import com.lambda.watermyplants.repositories.UserRepository;
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

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WatermyplantsApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PlantServiceImplTest {

    @Autowired
    private PlantService plantService;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void a_findAllPlants() {
        assertEquals(10, plantService.findAllPlants().size());
    }

    @Test
    public void b_findAllPlantsByUserId() {
        assertEquals(5, plantService.findAllPlantsByUserId(6).size());
    }

    @Test
    public void c_findPlantById() {
        assertEquals("douglas", plantService.findPlantById(8).getNickname().toLowerCase());
    }

    @Test
    public void d_findPlantByNickname() {
        assertEquals(9, plantService.findPlantByNickname("klaus").getPlantid());
    }

    @Test
    public void f_save() {
        Plant plant = new Plant("new", "unknown", "mid", "");
        plantService.save(userRepository.findByUsername("test"), plant);
        assertEquals(11, plantService.findAllPlants().size());
    }

    @Test
    public void f_saveput() {
        Plant plant = new Plant("newplant", "unknown", "mid", "");
        plant.setPlantid(18);
        plantService.save(userRepository.findByUsername("test"), plant);
        assertEquals("newplant", plantService.findPlantById(18).getNickname());
    }

    @Test
    @WithUserDetails("test")
    public void g_deleteById() {
        plantService.deleteById(10);
        assertEquals(10, plantService.findAllPlants().size());
    }

    @Test
    @WithUserDetails("test")
    public void h_deleteByNickname() {
        plantService.deleteByNickname("newplant");
        assertEquals(9, plantService.findAllPlants().size());
    }

    @Test
    @WithUserDetails("test")
    public void e_findAllPlantsByAuth() {
        assertEquals(5, plantService.findAllPlantsByAuth().size());
    }
}