package com.lambda.watermyplants.services;

import com.lambda.watermyplants.exceptions.ResourceFoundException;
import com.lambda.watermyplants.exceptions.ResourceNotFoundException;
import com.lambda.watermyplants.handlers.HelperFunctions;
import com.lambda.watermyplants.models.Role;
import com.lambda.watermyplants.models.User;
import com.lambda.watermyplants.models.UserRole;
import com.lambda.watermyplants.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service(value = "userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserAuditing userAuditing;

    @Autowired
    private HelperFunctions helperFunctions;

    @Override
    public List<User> findAllUsers() {

        List<User> userList = new ArrayList<>();
        userRepository.findAll().iterator().forEachRemaining(userList::add);
        return userList;
    }

    @Override
    public User findUserById(long id) {

        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User id " + id + " Not Found"));
    }

    @Override
    public User findUserByUserName(String username) {

        User user = userRepository.findByUsername(username.toLowerCase());
        if (user == null) {
            throw new ResourceNotFoundException("User name " + username + " not found!");
        }
        return user;
    }

    @Transactional
    @Override
    public User save(User user) {

        User newUser = new User();

        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new ResourceFoundException("Username " + user.getUsername() + " Already Exists");
        }

        if (user.getUserid() != 0) {
            User oldUser = userRepository.findById(user.getUserid())
                    .orElseThrow(() -> new ResourceNotFoundException("User id " + user.getUserid() + " not found!"));

            // delete the roles for the old user we are replacing
            for (UserRole ur : oldUser.getRoles()) {
                deleteUserRole(ur.getUser().getUserid(), ur.getRole().getRoleid());
            }
            newUser.setUserid(user.getUserid());
        }

        newUser.setUsername(user.getUsername().toLowerCase());
        newUser.setPasswordNoEncrypt(user.getPassword());
        newUser.setPhonenumber(user.getPhonenumber());

        newUser.getRoles()
                .clear();
        if (user.getUserid() == 0) {
            for (UserRole ur : user.getRoles()) {
                Role newRole = roleService.findRoleById(ur.getRole().getRoleid());

                newUser.addRole(newRole);
            }
        } else {
            // add the new roles for the user we are replacing
            for (UserRole ur : user.getRoles()) {
                addUserRole(newUser.getUserid(), ur.getRole().getRoleid());
            }
        }


        return userRepository.save(newUser);
    }

    @Transactional
    @Override
    public void deleteById(long id) {

        userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User id " + id + " not found!"));
        userRepository.deleteById(id);
    }

    @Transactional
    @Override
    public User update(User user, long id) {

        User currentUser = findUserById(id);

        if (helperFunctions.isAuthorizedToMakeChange(currentUser.getUsername())) {
            if (user.getUsername() != null) {
                currentUser.setUsername(user.getUsername().toLowerCase());
            }

            if (user.getPassword() != null) {
                currentUser.setPasswordNoEncrypt(user.getPassword());
            }

            if (user.getPassword() != null) {
                currentUser.setPhonenumber(user.getPhonenumber());
            }

            if (user.getRoles().size() > 0) {
                // delete the roles for the old user we are replacing
                for (UserRole ur : currentUser.getRoles()) {
                    deleteUserRole(ur.getUser().getUserid(), ur.getRole().getRoleid());
                }

                // add the new roles for the user we are replacing
                for (UserRole ur : user.getRoles()) {
                    addUserRole(currentUser.getUserid(), ur.getRole().getRoleid());
                }
            }

            return userRepository.save(currentUser);
        } else {
            throw new ResourceNotFoundException("This user is not authorized to make change");
        }
    }

    @Transactional
    @Override
    public void addUserRole(long userid, long roleid) {

        userRepository.findById(userid)
                .orElseThrow(() -> new ResourceNotFoundException("User id " + userid + " not found!"));
        roleService.findRoleById(roleid);

        if (userRepository.checkUserRolesCombo(userid, roleid).getCount() <= 0) {
            userRepository.insertUserRoles(userAuditing.getCurrentAuditor().get(), userid, roleid);
        } else {
            throw new ResourceFoundException("Role and User Combination Already Exists");
        }
    }

    @Transactional
    @Override
    public void deleteUserRole(long userid, long roleid) {

        userRepository.findById(userid)
                .orElseThrow(() -> new ResourceNotFoundException("User id " + userid + " not found!"));
        roleService.findRoleById(roleid);

        if (userRepository.checkUserRolesCombo(userid, roleid).getCount() > 0) {
            userRepository.deleteUserRoles(userid, roleid);
        } else {
            throw new ResourceNotFoundException("Role and User Combination Does Not Exists");
        }
    }
}
