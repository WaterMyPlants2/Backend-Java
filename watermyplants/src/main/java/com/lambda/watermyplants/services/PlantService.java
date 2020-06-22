package com.lambda.watermyplants.services;

import com.lambda.watermyplants.models.Plant;
import com.lambda.watermyplants.models.User;

import java.util.List;

public interface PlantService {

    List<Plant> findAllPlants();

    List<Plant> findAllPlantsByUserId(long userid);

    Plant findPlantById(long plantid);

    Plant findPlantByNickname(String nickname);

    Plant save(User user, Plant plant);

    void deleteById(long plantid);

    void deleteByNickname(String nickname);

    List<Plant> findAllPlantsByAuth();
}
