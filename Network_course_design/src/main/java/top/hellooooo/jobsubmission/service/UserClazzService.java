package main.java.top.hellooooo.jobsubmission.service;

import top.hellooooo.jobsubmission.pojo.Clazz;
import top.hellooooo.jobsubmission.pojo.SubmitPerson;
import top.hellooooo.jobsubmission.pojo.User;

import java.util.List;

public interface UserClazzService {

    List<Clazz> getAllClazz();

    List<Clazz> getClazzById(String ids);

    List<SubmitPerson> getUserByClazzId(String ids);
}
