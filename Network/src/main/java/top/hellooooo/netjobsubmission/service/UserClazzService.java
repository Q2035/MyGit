package top.hellooooo.netjobsubmission.service;

import top.hellooooo.netjobsubmission.pojo.Clazz;
import top.hellooooo.netjobsubmission.pojo.SubmitPerson;

import java.util.List;

public interface UserClazzService {

    List<Clazz> getAllClazz();

    List<Clazz> getClazzById(String ids);

    List<SubmitPerson> getUserByClazzId(String ids);
}
