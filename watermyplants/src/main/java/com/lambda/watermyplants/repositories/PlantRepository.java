package com.lambda.watermyplants.repositories;

import com.lambda.watermyplants.models.Plant;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PlantRepository extends CrudRepository<Plant, Long> {

    List<Plant> findPlantsByUser_Userid(long id);

    Plant findPlantByNicknameIgnoreCase(String nickname);
}
