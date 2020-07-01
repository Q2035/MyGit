package top.hellooooo.netjobsubmission.service.impl;

import org.springframework.stereotype.Service;
import top.hellooooo.netjobsubmission.mapper.BlackListMapper;
import top.hellooooo.netjobsubmission.pojo.BlackList;
import top.hellooooo.netjobsubmission.pojo.User;
import top.hellooooo.netjobsubmission.service.BlackListService;

import java.util.Date;
import java.util.List;

@Service
public class BlackListServiceImpl implements BlackListService {

    private final BlackListMapper blackListMapper;

    public BlackListServiceImpl(BlackListMapper blackListMapper) {
        this.blackListMapper = blackListMapper;
    }

    @Override
    public void setBlackListByUser(User user) {
        BlackList blackList = new BlackList();
        blackList.setHappenTime(new Date());
        blackList.setUserId(user.getId());
        blackListMapper.setBlackList(blackList);
    }

    @Override
    public void updateUserAccountStatus(Integer userId, Integer accountStatus) {
        blackListMapper.updateUserAccountStatus(userId,accountStatus);
    }

    @Override
    public void updateBlackListByIP(String ip) {
        BlackList blackList = new BlackList();
        blackList.setIp(ip);
        blackList.setHappenTime(new Date());
        blackListMapper.setBlackList(blackList);
    }

    @Override
    public List<BlackList> getBlackListByIP(String ip) {
        return blackListMapper.getBlackListByIP(ip);
    }
}
