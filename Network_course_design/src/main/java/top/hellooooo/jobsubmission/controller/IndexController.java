package main.java.top.hellooooo.jobsubmission.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import top.hellooooo.jobsubmission.pojo.Job;
import top.hellooooo.jobsubmission.pojo.Role;
import top.hellooooo.jobsubmission.pojo.User;
import top.hellooooo.jobsubmission.service.JobService;
import top.hellooooo.jobsubmission.service.UserService;
import top.hellooooo.jobsubmission.util.IndexUtil;

import javax.servlet.http.HttpSession;
import java.util.List;

@RequestMapping("/job")
@Controller
public class IndexController {

    private final IndexUtil indexUtil;

    private final JobService jobService;

    @Autowired
    private UserService userService;

    public IndexController(IndexUtil indexUtil, JobService jobService) {
        this.indexUtil = indexUtil;
        this.jobService = jobService;
    }

    @RequestMapping({"/","/user/index","/user/","/user/login"})
    public String userIndex(HttpSession session,
                            Model model){
//        如果Session中存在对应用户信息，则跳过登录，直接进入用户界面
        Object user = session.getAttribute("user");
        if (user != null) {
            String urlByUser = indexUtil.getURLByUser((User) user);
            if (urlByUser.contains("manager")) {
                List<Job> jobs = jobService.getUnexpiredJobs();
                model.addAttribute("jobs",jobs);
            } else if (urlByUser.contains("user")) {
                List<Job> jobs = jobService.getCurrentJobByUserId(((User) user).getId());
                model.addAttribute("jobs", jobs);
//                管理员提供用户
            } else {
                model.addAttribute("users", userService.getAllUsers());
            }
            return urlByUser;
        }
        return "index";
    }
}
