package top.hellooooo.jobsubmission.service;

import top.hellooooo.jobsubmission.pojo.Job;
import top.hellooooo.jobsubmission.pojo.SubmitPerson;

import java.util.List;

public interface JobService {
    void jobAdd(Job job);

    void insertJobSubmitPerson(List<SubmitPerson> personList);

    Job getJobAfterInsert(Integer user_id);

    List<Job> getUnexpiredJobs();
}
