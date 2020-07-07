package top.hellooooo.jobsubmission.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import top.hellooooo.jobsubmission.pojo.SubmitPerson;
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

    void setUser(User user);

    void updateUserWithNickname(@Param("username") String username,@Param("nickname") String nickname);

    void setUserRole(@Param("userId") Integer id, @Param("roleId") Integer roleId);

    //    获取所有用户的提交信息
    List<SubmitPerson> getAllSubmitInfoByUserId(Integer id);

    SubmitPerson getSubmitPersonByJobIdAndUserId(@Param("jobId") Integer jobId, @Param("userId") Integer userId);
}
