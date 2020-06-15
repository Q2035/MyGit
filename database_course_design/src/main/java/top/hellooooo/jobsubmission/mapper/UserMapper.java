package top.hellooooo.jobsubmission.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import top.hellooooo.jobsubmission.pojo.User;

public interface UserMapper {

//    @Select("select user.*,role.* as role from j_user user,j_user_role user_role,j_role role")
    User getUserByUsername(@Param("username") String username);
}
