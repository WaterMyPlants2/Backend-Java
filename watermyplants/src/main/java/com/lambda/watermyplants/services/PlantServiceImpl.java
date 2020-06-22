package com.lambda.watermyplants.services;

import com.lambda.watermyplants.exceptions.ResourceNotFoundException;
import com.lambda.watermyplants.handlers.HelperFunctions;
import com.lambda.watermyplants.models.Plant;
import com.lambda.watermyplants.models.User;
import com.lambda.watermyplants.repositories.PlantRepository;
import com.lambda.watermyplants.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service(value = "plantService")
public class PlantServiceImpl implements PlantService {

    @Autowired
    private PlantRepository plantRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HelperFunctions helperFunctions;

    @Override
    public List<Plant> findAllPlants() {

        List<Plant> plantList = new ArrayList<>();
        plantRepository.findAll().iterator().forEachRemaining(plantList::add);
        return plantList;
    }

    @Override
    public List<Plant> findAllPlantsByUserId(long userid) {

        List<Plant> plantList = new ArrayList<>();
        plantRepository.findPlantsByUser_Userid(userid).iterator().forEachRemaining(plantList::add);
        return plantList;
    }

    @Override
    public Plant findPlantById(long plantid) {

        return plantRepository.findById(plantid).orElseThrow(() -> new ResourceNotFoundException("Plant id " + plantid + " Not Found"));
    }

    @Override
    public Plant findPlantByNickname(String nickname) {
        return plantRepository.findPlantByNicknameIgnoreCase(nickname);
    }

    @Transactional
    @Override
    public Plant save(User user, Plant plant) {

        Plant newPlant = new Plant();

        if (plant.getPlantid() != 0) {
            plantRepository.findById(plant.getPlantid()).orElseThrow(() -> new ResourceNotFoundException("Plant id " + plant.getPlantid() + " Not Found"));
            newPlant.setPlantid(plant.getPlantid());
        }

        newPlant.setNickname(plant.getNickname());
        newPlant.setSpecies(plant.getSpecies());
        newPlant.setH2ofrequency(plant.getH2ofrequency());
        newPlant.setImage(plant.getImage());
        newPlant.setUser(user);

        return plantRepository.save(newPlant);
    }

    @Transactional
    @Override
    public void deleteById(long plantid) {

        if (helperFunctions.isAuthorizedToMakeChange(findPlantById(plantid).getUser().getUsername())) {
            if (plantRepository.findById(plantid).isPresent()) {
                plantRepository.deleteById(plantid);
            } else {
                throw new ResourceNotFoundException("Plant id " + plantid + " Not Found");
            }
        }
    }

    @Transactional
    @Override
    public void deleteByNickname(String nickname) {

        if (helperFunctions.isAuthorizedToMakeChange(plantRepository.findPlantByNicknameIgnoreCase(nickname).getUser().getUsername())) {
            if (plantRepository.findPlantByNicknameIgnoreCase(nickname) != null) {
                plantRepository.deleteById(plantRepository.findPlantByNicknameIgnoreCase(nickname).getPlantid());
            } else {
                throw new ResourceNotFoundException("Plant with nickname: " + nickname + " Not Found");
            }
        }
    }

    @Override
    public List<Plant> findAllPlantsByAuth() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            User currentUser = userRepository.findByUsername(authentication.getName());
            if (currentUser == null) {
                throw new ResourceNotFoundException("User id " + currentUser.getUserid() + " Not Found");
            }
            return plantRepository.findPlantsByUser_Userid(currentUser.getUserid());
        } else {
            throw new ResourceNotFoundException("No valid token is found");
        }
    }
}
