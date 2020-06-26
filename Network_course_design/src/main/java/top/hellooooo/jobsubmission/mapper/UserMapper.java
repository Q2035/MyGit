package main.java.top.hellooooo.jobsubmission.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import top.hellooooo.jobsubmission.pojo.User;

import java.util.List;

public interface UserMapper {

    User getUserByUsername(@Param("username") String username);

    User getUserAndClassById(Integer id);

    User getUserWithClazzAndRoleByUsername(String username);

    List<User> getUnSubmitPersonWithJobId(Integer jobId);

    User ifTheAccountIsFrozen(Integer userId);

    List<User> getAllUsers();

    void deleteUserByUserId(Integer id);

    void deleteTheUserRoleByUserId(Integer id);

    User getUserById(Integer id);
}
