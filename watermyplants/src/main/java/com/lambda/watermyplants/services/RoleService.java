package com.lambda.watermyplants.services;

import com.lambda.watermyplants.models.Role;

import java.util.List;

public interface RoleService {

    List<Role> findAllRoles();

    Role findRoleById(long id);

    Role findByRoleName(String rolename);

    Role save(Role role);

    void deleteById(long id);

    Role update(Role role, long id);
}
