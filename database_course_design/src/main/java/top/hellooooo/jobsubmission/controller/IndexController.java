package top.hellooooo.jobsubmission.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import top.hellooooo.jobsubmission.pojo.Job;
import top.hellooooo.jobsubmission.pojo.Role;
import top.hellooooo.jobsubmission.pojo.User;
import top.hellooooo.jobsubmission.service.JobService;
import top.hellooooo.jobsubmission.util.IndexUtil;

import javax.servlet.http.HttpSession;
import java.util.List;

@RequestMapping("/job")
@Controller
public class IndexController {

    @Autowired
    private IndexUtil indexUtil;

    @Autowired
    private JobService jobService;

    @RequestMapping({"/user/index","/user/","/user/login"})
    public String userIndex(HttpSession session,
                            Model model){
//        如果Session中存在对应用户信息，则跳过登录，直接进入用户界面
        Object user = session.getAttribute("user");
        if (user != null) {
            String urlByUser = indexUtil.getURLByUser((User) user);
            if (urlByUser.contains(Role.MANAGER)) {
                List<Job> jobs = jobService.getUnexpiredJobs();
                model.addAttribute("jobs",jobs);
            } else if (urlByUser.contains(Role.STUDENT)) {
                List<Job> jobs = jobService.getCurrentJobByUserId(((User) user).getId());
                model.addAttribute("jobs",jobs);
            }
            return urlByUser;
        }
        return "index";
    }
}
