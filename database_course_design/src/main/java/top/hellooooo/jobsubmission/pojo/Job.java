package top.hellooooo.jobsubmission.pojo;

import java.util.Date;

public class Job {
    private Integer id;
    private String job_description;
    private Integer originator;
    private Date start_time;
    private Date deadtime;
    private Integer submit_count;
    private Integer total_count;

    @Override
    public String toString() {
        return "Job{" +
                "id=" + id +
                ", job_description='" + job_description + '\'' +
                ", originator=" + originator +
                ", start_time=" + start_time +
                ", deadtime=" + deadtime +
                ", submit_count=" + submit_count +
                ", total_count=" + total_count +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getJob_description() {
        return job_description;
    }

    public void setJob_description(String job_description) {
        this.job_description = job_description;
    }

    public Integer getOriginator() {
        return originator;
    }

    public void setOriginator(Integer originator) {
        this.originator = originator;
    }

    public Date getStart_time() {
        return start_time;
    }

    public void setStart_time(Date start_time) {
        this.start_time = start_time;
    }

    public Date getDeadtime() {
        return deadtime;
    }

    public void setDeadtime(Date deadtime) {
        this.deadtime = deadtime;
    }

    public Integer getSubmit_count() {
        return submit_count;
    }

    public void setSubmit_count(Integer submit_count) {
        this.submit_count = submit_count;
    }

    public Integer getTotal_count() {
        return total_count;
    }

    public void setTotal_count(Integer total_count) {
        this.total_count = total_count;
    }
}
