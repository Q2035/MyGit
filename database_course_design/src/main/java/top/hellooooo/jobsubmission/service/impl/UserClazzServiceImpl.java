package top.hellooooo.jobsubmission.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.hellooooo.jobsubmission.mapper.UserClazzMapper;
import top.hellooooo.jobsubmission.pojo.Clazz;
import top.hellooooo.jobsubmission.service.UserClazzService;

import java.util.List;

@Service
public class UserClazzServiceImpl implements UserClazzService {

    @Autowired
    private UserClazzMapper userClazzMapper;


    @Override
    public List<Clazz> getAllClazz() {
        return userClazzMapper.getAllClazz();
    }
}
