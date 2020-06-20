package top.hellooooo.jobsubmission.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.hellooooo.jobsubmission.mapper.BlackListMapper;
import top.hellooooo.jobsubmission.pojo.BlackList;
import top.hellooooo.jobsubmission.service.BlackListService;

@Service
public class BlackListServiceImpl implements BlackListService {

    @Autowired
    private BlackListMapper blackListMapper;

    @Override
    public void setBlackListMap(BlackList blackList) {
        blackListMapper.setBlackListMap(blackList);
    }
}
