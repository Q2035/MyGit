package top.hellooooo.jobsubmission.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.hellooooo.jobsubmission.mapper.JobMapper;
import top.hellooooo.jobsubmission.pojo.Job;
import top.hellooooo.jobsubmission.pojo.SubmitPerson;
import top.hellooooo.jobsubmission.service.JobService;

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
}