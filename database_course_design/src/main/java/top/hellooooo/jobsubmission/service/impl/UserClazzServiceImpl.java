package top.hellooooo.jobsubmission.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.hellooooo.jobsubmission.mapper.UserClazzMapper;
import top.hellooooo.jobsubmission.pojo.Clazz;
import top.hellooooo.jobsubmission.pojo.SubmitPerson;
import top.hellooooo.jobsubmission.pojo.User;
import top.hellooooo.jobsubmission.service.UserClazzService;

import java.util.List;

@Service
public class UserClazzServiceImpl implements UserClazzService {

    private UserClazzMapper userClazzMapper;

    public UserClazzServiceImpl(@Autowired UserClazzMapper userClazzMapper) {
        this.userClazzMapper = userClazzMapper;
    }

    @Override
    public List<Clazz> getAllClazz() {
        return userClazzMapper.getAllClazz();
    }

    @Override
    public List<Clazz> getClazzById(String ids) {
        return userClazzMapper.getClazzById(ids);
    }

    @Override
    public List<User> getUserByClazzId(String ids) {
        return userClazzMapper.getUserByClazzId(ids);
    }
}
