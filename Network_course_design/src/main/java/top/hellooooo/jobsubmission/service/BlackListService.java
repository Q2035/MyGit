package main.java.top.hellooooo.jobsubmission.service;

import top.hellooooo.jobsubmission.pojo.BlackList;
import top.hellooooo.jobsubmission.pojo.User;

import java.util.List;

public interface BlackListService {
    void setBlackListByUser(User user);

    void updateUserAccountStatus(Integer userId,Integer accountStatus);

    void updateBlackListByIP(String ip);

    List<BlackList> getBlackListByIP(String ip);
}
