package top.hellooooo.jobsubmission.mapper;

import top.hellooooo.jobsubmission.pojo.Job;
import top.hellooooo.jobsubmission.pojo.SubmitPerson;

import java.util.List;

public interface JobMapper {
    void jobAdd(Job job);

    void insertJobSubmitPerson(List<SubmitPerson> personList);

//    新建作业后,重新从DB获取ID
    Job getJobAfterInsert(Integer user_id);

    List<Job> getUnexpiredJobs();
}
