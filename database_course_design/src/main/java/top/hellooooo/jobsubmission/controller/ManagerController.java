package top.hellooooo.jobsubmission.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import top.hellooooo.jobsubmission.pojo.Clazz;
import top.hellooooo.jobsubmission.pojo.Job;
import top.hellooooo.jobsubmission.pojo.SubmitPerson;
import top.hellooooo.jobsubmission.pojo.User;
import top.hellooooo.jobsubmission.service.JobService;
import top.hellooooo.jobsubmission.service.UserClazzService;
import top.hellooooo.jobsubmission.util.CommonResult;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/job/manager")
public class ManagerController {

    @Autowired
    private UserClazzService userClazzService;

    @Autowired
    private JobService jobService;

    public SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 跳转到job页面
     *  包含：
     *      所有发布的Job信息
     *      Job新增链接
     *      Job删除链接
     *      Job修改链接
     * @param model
     * @return
     */
    @GetMapping("/jobadd")
    public String jobShow(Model model){
        List<Clazz> allClazz = userClazzService.getAllClazz();
        model.addAttribute("allclazz",allClazz);
        return "/manager/job";
    }


    /**
     * Job发布
     * @param deadline
     * @param job_description
     * @param model
     * @param clazz
     * @param session
     * @return
     */
    @PostMapping("/jobadd")
    public String jobadd(String deadline,
                         String job_description,
                         Model model,
                         String clazz,
                         HttpSession session) {
        CommonResult result = new CommonResult();
        try {
            Date parse = simpleDateFormat.parse(deadline);
//            实例化Job，将发起人、开始、结束时间等放入
            Job job = new Job();
            job.setStartTime(new Date());
            job.setDeadline(parse);
            job.setOriginator(((User)session.getAttribute("user")).getId());
            job.setJobDescription(job_description);
            job.setSubmitCount(0);
//            从数据库获取选中班级的总人数
            List<Clazz> clazzById = userClazzService.getClazzById(clazz);
//            这个不能为0，应该直接接用班级人数
            job.setSubmitCount(0);
            job.setTotalCount(clazzById.stream().mapToInt(Clazz::getTotalCount).sum());
//            将Job信息插入数据库
            jobService.jobAdd(job);
//            将Job学生信息插入数据库
            List<SubmitPerson> userByClazzId = userClazzService.getUserByClazzId(clazz);
            List<SubmitPerson> submitPersonList = new ArrayList<>();
            for (SubmitPerson user : userByClazzId) {
                SubmitPerson e = new SubmitPerson();
                e.setIfSubmit(false);
//                无法获取JobID
                e.setJobId(getJobIdAfterInsert(session));
                e.setUserId(user.getId());
                submitPersonList.add(e);
            }
//            将生成的对象插入数据库
            jobService.insertJobSubmitPerson(submitPersonList);
        } catch (ParseException e) {
            result.setMessage("Error! The deadline is illegal!");
            e.printStackTrace();
        }
        model.addAttribute("result", result);
        return "/manager/jobshow";
    }

    /**
     * 根据Session中的用户ID返回刚刚创建的Job对象ID
     * @param session
     * @return
     */
    Integer getJobIdAfterInsert(HttpSession session) {
        synchronized (ManagerController.class) {
            User user = (User) session.getAttribute("user");
            Job jobAfterInsert = jobService.getJobAfterInsert(user.getId());
            return jobAfterInsert.getId();
        }
    }
}
