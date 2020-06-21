package top.hellooooo.jobsubmission.mapper;

import top.hellooooo.jobsubmission.pojo.BlackList;
import top.hellooooo.jobsubmission.pojo.User;

import java.util.List;

public interface BlackListMapper {
    void setBlackList(BlackList blackLis);

    void updateUserAccountStatus(Integer userId,Integer accountStatus);

    List<BlackList> getBlackListByIP(String ip);
}
