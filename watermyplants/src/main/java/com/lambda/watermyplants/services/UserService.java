package com.lambda.watermyplants.services;

import com.lambda.watermyplants.models.User;

import java.util.List;

public interface UserService {

    List<User> findAllUsers();

    User findUserById(long id);

    User findUserByUserName(String username);

    User save(User user);

    void deleteById(long id);

    User update(User user, long id);

    void addUserRole(long userid, long roleid);

    void deleteUserRole(long userid, long roleid);
}
