package top.hellooooo.netjobsubmission.service;

import top.hellooooo.netjobsubmission.pojo.Filename;
import top.hellooooo.netjobsubmission.pojo.Job;
import top.hellooooo.netjobsubmission.pojo.SubmitPerson;
import top.hellooooo.netjobsubmission.pojo.User;

import java.util.List;

public interface JobService {
    void jobAdd(Job job);

    void insertJobSubmitPerson(List<SubmitPerson> personList);

    Job getJobAfterInsert(Integer user_id);

    List<Job> getUnexpiredJobs();

    List<User> getUnsubmitUser(Integer id);

    List<Job> getCurrentJobByUserId(Integer userId);

    Job getJobByUserIdAndJobId(Integer id, Integer jobId);

    Job getJobByJobId(Integer id);

    void updateJobAndSubmitPerson(Integer userId,Integer jobId);

    void setFilename(Filename filename);

    Filename getFilenameByJobId(Integer jobId);
}
