package top.hellooooo.jobsubmission.mapper;

import org.apache.ibatis.annotations.Param;
import top.hellooooo.jobsubmission.pojo.Filename;
import top.hellooooo.jobsubmission.pojo.Job;
import top.hellooooo.jobsubmission.pojo.SubmitPerson;
import top.hellooooo.jobsubmission.pojo.User;

import java.util.List;

public interface JobMapper {
    void jobAdd(Job job);

    void insertJobSubmitPerson(List<SubmitPerson> personList);

//    新建作业后,重新从DB获取ID
    Job getJobAfterInsert(Integer user_id);

    /**
     * 返回未过期的Job
     * @return
     */
    List<Job> getUnexpiredJobs();

    List<User> getUnsubmitUser(Integer id);

    List<Job> getCurrentJobByUserId(Integer userId);

    Job getJobByJobId(Integer id);

    Job getJobByUserIdAndJobId(@Param("userId") Integer userId,@Param("jobId") Integer jobId);

    /**
     * 更新submit_person表
     * @param jobId
     */
    void updateJobSubmitPerson(@Param("userId") Integer userId,@Param("jobId") Integer jobId);

    void updateJobSubmitCount(Integer jobId);

    /**
     * 将要求的文件名插入数据库
     */
    void setFilename(Filename filename);

    Filename getFilenameByJobId(Integer jobId);

    void dropSubmitPersonByUserId(Integer id);

    /**
     * 丛数据库返回所有已经截止的作业
     * @return
     */
    List<Job> getAllExpiredJobs();
}
