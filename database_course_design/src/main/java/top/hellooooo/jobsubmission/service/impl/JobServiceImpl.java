package top.hellooooo.jobsubmission.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.hellooooo.jobsubmission.mapper.JobMapper;
import top.hellooooo.jobsubmission.pojo.Filename;
import top.hellooooo.jobsubmission.pojo.Job;
import top.hellooooo.jobsubmission.pojo.SubmitPerson;
import top.hellooooo.jobsubmission.pojo.User;
import top.hellooooo.jobsubmission.service.JobService;

import java.util.ArrayList;
import java.util.List;

@Service
public class JobServiceImpl implements JobService {

    private final JobMapper jobMapper;

    public JobServiceImpl(JobMapper jobMapper) {
        this.jobMapper = jobMapper;
    }

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

    @Override
    public List<Job> getAllExpiredJobs() {
        return jobMapper.getAllExpiredJobs();
    }

    @Override
    public List<Job> getUnexpiredJobsByUserId(Integer id) {
        List<Job> unexpiredJobsByUserId = jobMapper.getUnexpiredJobsByUserId(id);
        List<Integer> jobIds = new ArrayList<>();
        unexpiredJobsByUserId.stream().forEach(job -> {
            jobIds.add(job.getId());
        });

        return unexpiredJobsByUserId;
    }
}