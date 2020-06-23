package com.lambda.watermyplants.repositories;

import com.lambda.watermyplants.models.Role;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface RoleRepository extends CrudRepository<Role, Long> {

    Role findByRolenameIgnoreCase(String rolename);

    @Transactional
    @Modifying
    @Query(value = "UPDATE roles SET rolename = :rolename, last_modified_by = :uname, last_modified_date = CURRENT_TIMESTAMP WHERE roleid = :roleid", nativeQuery = true)
    void updateRoleName(String uname, long roleid, String rolename);
}
