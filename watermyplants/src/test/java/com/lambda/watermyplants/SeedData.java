package com.lambda.watermyplants;

import com.lambda.watermyplants.models.Plant;
import com.lambda.watermyplants.models.Role;
import com.lambda.watermyplants.models.User;
import com.lambda.watermyplants.models.UserRole;
import com.lambda.watermyplants.repositories.UserRepository;
import com.lambda.watermyplants.services.PlantService;
import com.lambda.watermyplants.services.RoleService;
import com.lambda.watermyplants.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

/**
 * SeedData puts both known and random data into the database. It implements CommandLineRunner.
 * <p>
 * CoomandLineRunner: Spring Boot automatically runs the run method once and only once
 * after the application context has been loaded.
 */
@Transactional
@Component
public class SeedData implements CommandLineRunner {

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @Autowired
    private PlantService plantService;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    @Override
    public void run(String[] args) throws Exception {
        /************
         * Seed Users
         ************/
        Role r1 = new Role("admin");
        Role r2 = new Role("user");

        r1 = roleService.save(r1);
        r2 = roleService.save(r2);

        // admin, user1
        ArrayList<UserRole> admins = new ArrayList<>();
        admins.add(new UserRole(new User(), r1));
        admins.add(new UserRole(new User(), r2));

        User u1 = new User("admin", "password", "", admins);
        userService.save(u1);

        //  user2
        ArrayList<UserRole> users = new ArrayList<>();
        users.add(new UserRole(new User(), r2));

        User u2 = new User("testuser", "password", "000-000-0000", users);
        userService.save(u2);

        // user3
        users = new ArrayList<>();
        users.add(new UserRole(new User(), r2));

        User u3 = new User("lambda", "school", "000-000-0000", users);
        userService.save(u3);

        // user4
        users = new ArrayList<>();
        users.add(new UserRole(new User(), r2));

        User u4 = new User("test", "password", "123-456-7890", users);
        userService.save(u4);

        // user4
        users = new ArrayList<>();
        users.add(new UserRole(new User(), r2));

        User u5 = new User("testtest", "password", "123-456-7890", users);
        userService.save(u5);


        /************
         * Seed Plants
         ************/
        Plant p1 = new Plant("Douglas", "unknown", "mid", "");
        plantService.save(userRepository.findByUsername("test"), p1);

        Plant p2 = new Plant("Klaus", "unknown", "high", "");
        plantService.save(userRepository.findByUsername("test"), p2);

        Plant p3 = new Plant("Virginia", "unknown", "low", "");
        plantService.save(userRepository.findByUsername("test"), p3);

        Plant p4 = new Plant("Hattie", "unknown", "mid", "");
        plantService.save(userRepository.findByUsername("test"), p4);

        Plant p5 = new Plant("Trixie", "unknown", "low", "");
        plantService.save(userRepository.findByUsername("testtest"), p5);

        Plant p6 = new Plant("Ronaldo", "unknown", "low", "");
        plantService.save(userRepository.findByUsername("lambda"), p6);

        Plant p7 = new Plant("Elephant", "unknown", "mid", "");
        plantService.save(userRepository.findByUsername("lambda"), p7);

        Plant p8 = new Plant("plantcc", "unknown", "mid", "");
        plantService.save(userRepository.findByUsername("test"), p8);

        Plant p9 = new Plant("CAC", "unknown", "mid", "");
        plantService.save(userRepository.findByUsername("lambda"), p9);

        Plant p10 = new Plant("Plant OK", "unknown", "high", "");
        plantService.save(userRepository.findByUsername("lambda"), p10);

    }
}