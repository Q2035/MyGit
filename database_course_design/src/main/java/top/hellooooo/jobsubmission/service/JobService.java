package top.hellooooo.jobsubmission.service;

import top.hellooooo.jobsubmission.pojo.Job;
import top.hellooooo.jobsubmission.pojo.SubmitPerson;
import top.hellooooo.jobsubmission.pojo.User;

import java.util.List;

public interface JobService {
    void jobAdd(Job job);

    void insertJobSubmitPerson(List<SubmitPerson> personList);

    Job getJobAfterInsert(Integer user_id);

    List<Job> getUnexpiredJobs();

    List<User> getUnsubmitUser(Integer id);

    List<Job> getCurrentJobByUserId(Integer userId);

    Job getJobByJobId(Integer id);

    Job getJobByUserIdAndJobId(Integer userId, Integer jobId);
}
