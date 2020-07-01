package top.hellooooo.netjobsubmission.mapper;

import top.hellooooo.netjobsubmission.pojo.BlackList;

import java.util.List;

public interface BlackListMapper {
    void setBlackList(BlackList blackLis);

    void updateUserAccountStatus(Integer userId,Integer accountStatus);

    List<BlackList> getBlackListByIP(String ip);

    void deleteBlackListByUserId(Integer id);
}
