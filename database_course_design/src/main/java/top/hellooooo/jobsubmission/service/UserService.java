package top.hellooooo.jobsubmission.service;

import org.apache.ibatis.annotations.Param;
import top.hellooooo.jobsubmission.pojo.Job;
import top.hellooooo.jobsubmission.pojo.SubmitPerson;
import top.hellooooo.jobsubmission.pojo.User;

import java.util.List;

public interface UserService {
    User getUserByUsername(String username);

    User getUserAndClassById(Integer id);

    User getUserWithClazzAndRoleByUsername(String username);

    List<User> getUnSubmitPersonWithJobId(Integer jobId);

    User ifTheAccountIsFrozen(Integer userId);

    List<User> getAllUsers();

    void deleteUser(Integer id);

    User getUserById(Integer id);

    void setUserWithRole(User user, Integer roleId);

    void updateUserWithNickname(String username, String nickname);

    void setClazz(Integer id, int clazzId);

    /**
     * 获取所有已经过期的作业
     *
     * @param id
     * @return
     */
    List<SubmitPerson> getAllExpiredJobSubmitInfoByUserId(Integer id);

    List<Job> getAllUnExpireJobByUserId(Integer id);

    SubmitPerson getSubmitPersonByJobIdAndUserId(Integer jobId, Integer userId);
}
