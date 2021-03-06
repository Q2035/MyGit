package top.hellooooo.jobsubmission.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.hellooooo.jobsubmission.mapper.BlackListMapper;
import top.hellooooo.jobsubmission.mapper.JobMapper;
import top.hellooooo.jobsubmission.mapper.UserClazzMapper;
import top.hellooooo.jobsubmission.mapper.UserMapper;
import top.hellooooo.jobsubmission.pojo.Job;
import top.hellooooo.jobsubmission.pojo.SubmitPerson;
import top.hellooooo.jobsubmission.pojo.User;
import top.hellooooo.jobsubmission.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    private final JobMapper jobMapper;

    private final BlackListMapper blackListMapper;

    private final UserClazzMapper userClazzMapper;

    public UserServiceImpl(UserMapper userMapper, JobMapper jobMapper, BlackListMapper blackListMapper, UserClazzMapper userClazzMapper) {
        this.userMapper = userMapper;
        this.jobMapper = jobMapper;
        this.blackListMapper = blackListMapper;
        this.userClazzMapper = userClazzMapper;
    }

    @Override
    public User getUserByUsername(String username) {

        return userMapper.getUserByUsername(username);
    }

    @Override
    public User getUserAndClassById(Integer id) {
        return userMapper.getUserAndClassById(id);
    }

    @Override
    public User getUserWithClazzAndRoleByUsername(String username) {
        return userMapper.getUserWithClazzAndRoleByUsername(username);
    }

    @Override
    public List<User> getUnSubmitPersonWithJobId(Integer jobId) {
        return userMapper.getUnSubmitPersonWithJobId(jobId);
    }

    @Override
    public User ifTheAccountIsFrozen(Integer userId) {
        return userMapper.ifTheAccountIsFrozen(userId);
    }

    @Override
    public List<User> getAllUsers() {
        return userMapper.getAllUsers();
    }

    @Override
    public void deleteUser(Integer id) {
//        将数据表中所有有关用户的数据删除
        userMapper.deleteTheUserRoleByUserId(id);
        jobMapper.dropSubmitPersonByUserId(id);
        userClazzMapper.deleteUserClazzByUserId(id);
        blackListMapper.deleteBlackListByUserId(id);
        userMapper.deleteUserByUserId(id);
    }

    @Override
    public User getUserById(Integer id) {
        return userMapper.getUserById(id);
    }

    @Override
    public void setUserWithRole(User user, Integer roleId) {
//        只插入用户名和密码还有昵称
        userMapper.setUser(user);
        User userByUsername = userMapper.getUserByUsername(user.getUsername());
        userMapper.setUserRole(userByUsername.getId(), roleId);
    }

    @Override
    public void updateUserWithNickname(String username, String nickname) {
        userMapper.updateUserWithNickname(username,nickname);
    }

    @Override
    public void setClazz(Integer id, int clazzId) {
        userClazzMapper.setClazz(id,clazzId);
    }

    @Override
    public List<SubmitPerson> getAllExpiredJobSubmitInfoByUserId(Integer id) {
        List<SubmitPerson> resJobs = new ArrayList<>();
        List<SubmitPerson> allJobs = userMapper.getAllSubmitInfoByUserId(id);
        long currentTime = System.currentTimeMillis();
        allJobs.stream().forEach(submitPerson -> {
            if (submitPerson.getJob().getDeadline().getTime() < currentTime) {
                resJobs.add(submitPerson);
            }
        });
        return resJobs;
    }

    @Override
    public List<Job> getAllUnExpireJobByUserId(Integer id) {
        List<Job> resJob = new ArrayList<>();
        List<SubmitPerson> allSubmitInfoByUserId = userMapper.getAllSubmitInfoByUserId(id);
        Long currentTime = System.currentTimeMillis();
        allSubmitInfoByUserId.forEach(submitPerson -> {
            if (submitPerson.getSubmitTime() == null) {
                submitPerson.getJob().setIfSubmit(false);
            } else {
                submitPerson.getJob().setIfSubmit(true);
            }
            if (submitPerson.getJob().getDeadline().getTime() > currentTime) {
                resJob.add(submitPerson.getJob());
            }
        });
        return resJob;
    }

    @Override
    public SubmitPerson getSubmitPersonByJobIdAndUserId(Integer jobId, Integer userId) {
        return userMapper.getSubmitPersonByJobIdAndUserId(jobId,userId);
    }
}
