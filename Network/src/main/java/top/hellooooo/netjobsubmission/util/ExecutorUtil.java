package top.hellooooo.netjobsubmission.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.hellooooo.netjobsubmission.pojo.Job;
import top.hellooooo.netjobsubmission.pojo.User;
import top.hellooooo.netjobsubmission.service.JobService;

import java.util.List;

@Component
public class ExecutorUtil{

    private final JobService jobService;

    private final MailUtil emailUtil;

    @Value("${file.submitURL}")
    private String submitURL;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public ExecutorUtil(JobService jobService, MailUtil emailUtil) {
        this.jobService = jobService;
        this.emailUtil = emailUtil;
    }

    @Scheduled(initialDelay = 0, fixedDelay = 24 * 60 * 60 * 1000)
    public void execute() {
//        提交任务，一天检查一次数据库，截止日期是否快过到了
        List<Job> unexpiredJobs = jobService.getUnexpiredJobs();
        unexpiredJobs.stream().forEach(job -> {
//                判断日期是否小于一天
            if (System.currentTimeMillis() - job.getDeadline().getTime() < 24*60*60*1000) {
                List<User> unsubmitUser = jobService.getUnsubmitUser(job.getId());
                for (User user : unsubmitUser) {
//                        如果小于一天并且用户没有提交作业，则发送邮件提醒
                    if (user.getEmail() != null && !user.getEmail().equals("")) {
                        logger.info("will send a message to user {}",user.getUsername());
                        MailBean mailBean = new MailBean();
                        mailBean.setRecipient(user.getEmail());
                        mailBean.setSubject("The deadline of \'" +job.getJobDescription() +"\' is coming up");
                        mailBean.setContent("HI "+ user.getNickname() +", your homework has not been handed in, please go to "
                                + submitURL + " for submission. " + "And the deadline is "
                                + job.getDeadline());
                        emailUtil.sendSimpleMain(mailBean);
                    }
                }
            }
        });
    }
}
