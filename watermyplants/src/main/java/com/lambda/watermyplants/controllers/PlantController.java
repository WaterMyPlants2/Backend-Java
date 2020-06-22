package com.lambda.watermyplants.controllers;

import com.lambda.watermyplants.models.ErrorDetail;
import com.lambda.watermyplants.models.Plant;
import com.lambda.watermyplants.models.User;
import com.lambda.watermyplants.repositories.UserRepository;
import com.lambda.watermyplants.services.PlantService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
public class PlantController {

    @Autowired
    private PlantService plantService;

    @Autowired
    private UserRepository userRepository;

    @ApiOperation(value = "return a list of all plants (**ADMIN**)", response = Plant.class, responseContainer = "List")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping(value = "/api/plants/admin", produces = {"application/json"})
    public ResponseEntity<?> getAllPlants() {

        List<Plant> plantList = plantService.findAllPlants();
        return new ResponseEntity<>(plantList, HttpStatus.OK);
    }

    @ApiOperation(value = "retrieve a plant based off plant id", response = Plant.class)
    @ApiResponses(value = {@ApiResponse(code = 200,
            message = "Plant Found",
            response = Plant.class), @ApiResponse(code = 404,
            message = "Plant Not Found",
            response = ErrorDetail.class)})
    @GetMapping(value = "/api/plants/{plantid}", produces = {"application/json"})
    public ResponseEntity<?> getPlantById(@PathVariable long plantid) {

        Plant plant = plantService.findPlantById(plantid);
        return new ResponseEntity<>(plant, HttpStatus.OK);
    }

    @ApiOperation(value = "return a list plants of current user", response = Plant.class, responseContainer = "List")
    @GetMapping(value = "/api/plants", produces = {"application/json"})
    public ResponseEntity<?> getPlantsByAuth() {

        List<Plant> plantList = plantService.findAllPlantsByAuth();
        return new ResponseEntity<>(plantList, HttpStatus.OK);
    }

    @ApiOperation(value = "retrieve a plant based off plant nickname", response = Plant.class)
    @ApiResponses(value = {@ApiResponse(code = 200,
            message = "Plant Found",
            response = Plant.class), @ApiResponse(code = 404,
            message = "Plant Not Found",
            response = ErrorDetail.class)})
    @GetMapping(value = "/api/plants/{nickname}", produces = {"application/json"})
    public ResponseEntity<?> getPlantByNickname(@PathVariable String nickname) {

        Plant plant = plantService.findPlantByNickname(nickname);
        return new ResponseEntity<>(plant, HttpStatus.OK);
    }

    @ApiOperation(value = "add a plant given in the request body to current user", response = Void.class)
    @PostMapping(value = "/api/plants", consumes = {"application/json"})
    public ResponseEntity<?> addNewPlant(@Valid @RequestBody Plant plant) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName());

        plant.setPlantid(0);
        Plant newPlant = plantService.save(user, plant);

        HttpHeaders responseHeaders = new HttpHeaders();
        URI newPlantURI = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{plantid}")
                .buildAndExpand(newPlant.getPlantid())
                .toUri();
        responseHeaders.setLocation(newPlantURI);

        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }

    @ApiOperation(value = "update a plant given in the request body", response = Void.class)
    @ApiResponses(value = {@ApiResponse(code = 200,
            message = "Plant Found",
            response = Plant.class), @ApiResponse(code = 404,
            message = "Plant Not Found",
            response = ErrorDetail.class)})
    @PutMapping(value = "/api/plants/{plantid}", consumes = {"application/json"})
    public ResponseEntity<?> updatePlant(@Valid @RequestBody Plant plant, @PathVariable long plantid) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName());

        plant.setPlantid(plantid);
        plantService.save(user, plant);

        HttpHeaders responseHeaders = new HttpHeaders();
        URI updatePlantURI = ServletUriComponentsBuilder.fromCurrentRequest()
                .buildAndExpand(plant.getPlantid())
                .toUri();
        responseHeaders.setLocation(updatePlantURI);

        return new ResponseEntity<>(null, responseHeaders, HttpStatus.OK);
    }

    @ApiOperation(value = "Delete the plant by plant id", response = Void.class)
    @ApiResponses(value = {@ApiResponse(code = 200,
            message = "Plant Found",
            response = Plant.class), @ApiResponse(code = 404,
            message = "Plant Not Found",
            response = ErrorDetail.class)})
    @DeleteMapping(value = "/api/plants/{plantid}")
    public ResponseEntity<?> deletePlantByPlantId(@PathVariable long plantid) {

        plantService.deleteById(plantid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Delete the plant by plant nickname", response = Void.class)
    @ApiResponses(value = {@ApiResponse(code = 200,
            message = "Plant Found",
            response = Plant.class), @ApiResponse(code = 404,
            message = "Plant Not Found",
            response = ErrorDetail.class)})
    @DeleteMapping(value = "/api/plants/nickname/{nickname}")
    public ResponseEntity<?> deletePlantByNickname(@PathVariable String nickname) {

        plantService.deleteByNickname(nickname);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
