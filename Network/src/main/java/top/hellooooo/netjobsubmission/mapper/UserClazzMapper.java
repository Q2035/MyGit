package top.hellooooo.netjobsubmission.mapper;

import org.apache.ibatis.annotations.Param;
import top.hellooooo.netjobsubmission.pojo.Clazz;
import top.hellooooo.netjobsubmission.pojo.SubmitPerson;

import java.util.List;

public interface UserClazzMapper {
    List<Clazz> getAllClazz();

    List<Clazz> getClazzById(@Param("ids") String ids);

    List<SubmitPerson> getUserByClazzId(@Param("ids") String ids);

    void deleteUserClazzByUserId(Integer id);

    void setClazz(@Param("userId") Integer id, @Param("clazzId") int clazzId);
}
