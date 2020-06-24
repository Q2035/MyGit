package top.hellooooo.jobsubmission.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.hellooooo.jobsubmission.mapper.UserMapper;
import top.hellooooo.jobsubmission.pojo.User;
import top.hellooooo.jobsubmission.service.UserService;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User getUserByUsername(String username) {

        return userMapper.getUserByUsername(username);
    }

    @Override
    public User getUserAndClassById(Integer id) {
        return userMapper.getUserAndClassById(id);
    }

    @Override
    public User getUserWithClazzAndRoleByUsername(String username) {
        return userMapper.getUserWithClazzAndRoleByUsername(username);
    }

    @Override
    public List<User> getUnSubmitPersonWithJobId(Integer jobId) {
        return userMapper.getUnSubmitPersonWithJobId(jobId);
    }

    @Override
    public User ifTheAccountIsFrozen(Integer userId) {
        return userMapper.ifTheAccountIsFrozen(userId);
    }

    @Override
    public List<User> getAllUsers() {
        return userMapper.getAllUsers();
    }

    @Override
    public void deleteUser(Integer id) {

    }
}
