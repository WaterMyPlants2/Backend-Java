package com.lambda.watermyplants.services;

import com.lambda.watermyplants.exceptions.ResourceFoundException;
import com.lambda.watermyplants.exceptions.ResourceNotFoundException;
import com.lambda.watermyplants.models.Role;
import com.lambda.watermyplants.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service(value = "roleService")
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserAuditing userAuditing;

    @Override
    public List<Role> findAllRoles() {

        List<Role> roleList = new ArrayList<>();
        roleRepository.findAll().iterator().forEachRemaining(roleList::add);
        return roleList;
    }

    @Override
    public Role findRoleById(long id) {

        return roleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Role id " + id + " Not Found"));
    }

    @Override
    public Role findByRoleName(String rolename) {
        Role role = roleRepository.findByRolenameIgnoreCase(rolename);

        if (role != null) {
            return role;
        } else {
            throw new ResourceNotFoundException(rolename);
        }
    }

    @Transactional
    @Override
    public Role save(Role role) {

        if (role.getUsers().size() > 0) {
            throw new ResourceFoundException("User Roles are not updated through Role.");
        }

        return roleRepository.save(role);
    }

    @Transactional
    @Override
    public void deleteById(long id) {

        roleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Role id " + id + " not found!"));
        roleRepository.deleteById(id);
    }

    @Override
    public Role update(Role role, long id) {

        if (role.getRolename() == null) {
            throw new ResourceNotFoundException("No role name found to update!");
        }

        if (role.getUsers()
                .size() > 0) {
            throw new ResourceFoundException("User Roles are not updated through Role. See endpoint POST: users/user/{userid}/role/{roleid}");
        }

        Role newRole = findRoleById(id); // see if id exists

        roleRepository.updateRoleName(userAuditing.getCurrentAuditor().get(), id, role.getRolename());
        return findRoleById(id);
    }
}
