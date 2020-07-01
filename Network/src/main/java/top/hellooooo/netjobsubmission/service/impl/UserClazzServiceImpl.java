package top.hellooooo.netjobsubmission.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.hellooooo.netjobsubmission.mapper.UserClazzMapper;
import top.hellooooo.netjobsubmission.pojo.Clazz;
import top.hellooooo.netjobsubmission.pojo.SubmitPerson;
import top.hellooooo.netjobsubmission.service.UserClazzService;

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
    public List<SubmitPerson> getUserByClazzId(String ids) {
        return userClazzMapper.getUserByClazzId(ids);
    }
}
