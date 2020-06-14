package top.hellooooo.jobsubmission.mapper;

import org.apache.ibatis.annotations.Param;
import top.hellooooo.jobsubmission.pojo.User;

public interface UserMapper {
    User getUserByUsername(@Param("username") String username);
}
