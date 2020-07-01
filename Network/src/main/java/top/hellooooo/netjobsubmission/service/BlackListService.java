package top.hellooooo.netjobsubmission.service;

import top.hellooooo.netjobsubmission.pojo.BlackList;
import top.hellooooo.netjobsubmission.pojo.User;

import java.util.List;

public interface BlackListService {
    void setBlackListByUser(User user);

    void updateUserAccountStatus(Integer userId,Integer accountStatus);

    void updateBlackListByIP(String ip);

    List<BlackList> getBlackListByIP(String ip);
}
