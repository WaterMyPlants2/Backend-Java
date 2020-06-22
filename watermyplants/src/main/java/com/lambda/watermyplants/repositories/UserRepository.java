package com.lambda.watermyplants.repositories;

import com.lambda.watermyplants.models.User;
import com.lambda.watermyplants.views.JustTheCount;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);

    @Query(value = "SELECT COUNT(*) as count FROM userroles WHERE userid = :userid AND roleid = :roleid", nativeQuery = true)
    JustTheCount checkUserRolesCombo(long userid, long roleid);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM userroles WHERE userid = :userid AND roleid = :roleid", nativeQuery = true)
    void deleteUserRoles(long userid, long roleid);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO userroles(userid, roleid, created_by, created_date, last_modified_by, last_modified_date) VALUES (:userid, :roleid, :uname, CURRENT_TIMESTAMP, :uname, CURRENT_TIMESTAMP)", nativeQuery = true)
    void insertUserRoles(String uname, long userid, long roleid);
}
