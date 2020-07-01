package top.hellooooo.netjobsubmission.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.hellooooo.netjobsubmission.mapper.JobMapper;
import top.hellooooo.netjobsubmission.pojo.Filename;
import top.hellooooo.netjobsubmission.pojo.Job;
import top.hellooooo.netjobsubmission.pojo.SubmitPerson;
import top.hellooooo.netjobsubmission.pojo.User;
import top.hellooooo.netjobsubmission.service.JobService;

import java.util.List;

@Service
public class JobServiceImpl implements JobService {

    @Autowired
    private JobMapper jobMapper;

    @Override
    public void jobAdd(Job job) {
        jobMapper.jobAdd(job);
    }

    @Override
    public void insertJobSubmitPerson(List<SubmitPerson> personList) {
        jobMapper.insertJobSubmitPerson(personList);
    }

    @Override
    public Job getJobAfterInsert(Integer user_id) {
        return jobMapper.getJobAfterInsert(user_id);
    }

    @Override
    public List<Job> getUnexpiredJobs() {
        return jobMapper.getUnexpiredJobs();
    }

    @Override
    public List<User> getUnsubmitUser(Integer id) {
        return jobMapper.getUnsubmitUser(id);
    }

    @Override
    public List<Job> getCurrentJobByUserId(Integer userId) {
        return jobMapper.getCurrentJobByUserId(userId);
    }

    @Override
    public Job getJobByUserIdAndJobId(Integer id, Integer jobId) {
        return jobMapper.getJobByUserIdAndJobId(id,jobId);
    }

    @Override
    public Job getJobByJobId(Integer id) {
        return jobMapper.getJobByJobId(id);
    }

    @Override
    public void updateJobAndSubmitPerson(Integer userId, Integer jobId) {
        jobMapper.updateJobSubmitCount(jobId);
        jobMapper.updateJobSubmitPerson(userId,jobId);
    }

    @Override
    public void setFilename(Filename filename) {
        jobMapper.setFilename(filename);
    }

    @Override
    public Filename getFilenameByJobId(Integer jobId) {
        return jobMapper.getFilenameByJobId(jobId);
    }
}