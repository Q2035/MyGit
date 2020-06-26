package top.hellooooo.jobsubmission.mapper;

import org.apache.ibatis.annotations.Param;
import top.hellooooo.jobsubmission.pojo.Clazz;
import top.hellooooo.jobsubmission.pojo.SubmitPerson;
import top.hellooooo.jobsubmission.pojo.User;

import java.util.List;

public interface UserClazzMapper {
    List<Clazz> getAllClazz();

    List<Clazz> getClazzById(@Param("ids") String ids);

    List<SubmitPerson> getUserByClazzId(@Param("ids") String ids);

    void deleteUserClazzByUserId(Integer id);

    void setClazz(@Param("userId") Integer id, @Param("clazzId") int clazzId);
}
