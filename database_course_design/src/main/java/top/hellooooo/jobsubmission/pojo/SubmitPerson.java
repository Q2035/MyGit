package top.hellooooo.jobsubmission.pojo;

import java.util.Date;

public class SubmitPerson {
    private Integer id;
    private Integer userId;
    private Integer jobId;
    private Boolean ifSubmit;
    private Date submitTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public Boolean getIfSubmit() {
        return ifSubmit;
    }

    public void setIfSubmit(Boolean ifSubmit) {
        this.ifSubmit = ifSubmit;
    }

    public Date getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
    }
}
