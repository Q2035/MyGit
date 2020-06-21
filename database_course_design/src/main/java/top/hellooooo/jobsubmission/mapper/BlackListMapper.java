package top.hellooooo.jobsubmission.mapper;

import top.hellooooo.jobsubmission.pojo.BlackList;
import top.hellooooo.jobsubmission.pojo.User;

public interface BlackListMapper {
    void setBlackList(BlackList blackLis);

    void updateUserAccountStatus(Integer userId,Integer accountStatus);
}
