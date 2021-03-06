package top.hellooooo.netjobsubmission.mapper;

import org.apache.ibatis.annotations.Param;
import top.hellooooo.netjobsubmission.pojo.User;

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
}
