package top.hellooooo.jobsubmission.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.hellooooo.jobsubmission.pojo.User;

@Mapper
public interface UserMapper {
    User getUserByID(int id);
}
