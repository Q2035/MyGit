package top.hellooooo.jobsubmission.service;

import top.hellooooo.jobsubmission.pojo.User;

public interface UserService {
    User getUserByUsername(String username);

    User getUserAndClassById(Integer id);

    User getUserWithClazzAndRoleByUsername(String username);
}
