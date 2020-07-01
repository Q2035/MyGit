package top.hellooooo.netjobsubmission.service;

import top.hellooooo.netjobsubmission.pojo.User;

import java.util.List;

public interface UserService {
    User getUserByUsername(String username);

    User getUserAndClassById(Integer id);

    User getUserWithClazzAndRoleByUsername(String username);

    List<User> getUnSubmitPersonWithJobId(Integer jobId);

    User ifTheAccountIsFrozen(Integer userId);

    List<User> getAllUsers();

    void deleteUser(Integer id);

    User getUserById(Integer id);

    void setUserWithRole(User user, Integer roleId);

    void updateUserWithNickname(String username,String nickname);

    void setClazz(Integer id, int clazzId);
}
