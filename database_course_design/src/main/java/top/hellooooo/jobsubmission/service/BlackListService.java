package top.hellooooo.jobsubmission.service;

import top.hellooooo.jobsubmission.pojo.BlackList;
import top.hellooooo.jobsubmission.pojo.User;

public interface BlackListService {
    void setBlackListByUser(User user);

    void updateUserAccountStatus(Integer userId,Integer accountStatus);
}
