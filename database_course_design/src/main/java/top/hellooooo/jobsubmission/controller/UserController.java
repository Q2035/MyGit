package top.hellooooo.jobsubmission.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import top.hellooooo.jobsubmission.pojo.Job;
import top.hellooooo.jobsubmission.pojo.Role;
import top.hellooooo.jobsubmission.pojo.User;
import top.hellooooo.jobsubmission.service.JobService;
import top.hellooooo.jobsubmission.service.UserService;
import top.hellooooo.jobsubmission.util.IndexUtil;

import javax.servlet.http.HttpSession;
import java.util.List;

@RequestMapping("job/user")
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private IndexUtil indexUtil;

    @Autowired
    private JobService jobService;

    /**
     * 登录认证
     * @param username
     * @param password
     * @param attributes
     * @param session
     * @return
     */
    @PostMapping("/authentication")
    public String auth(@RequestParam("username")String username,
                     @RequestParam("password")String password,
                     Model model,
                     RedirectAttributes attributes,
                     HttpSession session){
//        将用户信息存入Session
        User user = userService.getUserByUsername(username);
        if (user != null
            && user.getPassword().equals(password)){
            String redirectAddress;
            user.setPassword(null);
            redirectAddress = indexUtil.getURLByUser(user);
            session.setAttribute("user",user);
//            如果登录者为管理员，则将所有未过期Job信息传回前端
            List<Job> unexpiredJobs = jobService.getUnexpiredJobs();
            model.addAttribute("jobs", unexpiredJobs);
            return redirectAddress;
//            提示密码错误
        }else {
            String message;
            if (user == null) {
                message = "cann't find the user in db, please check the username";
            }else {
                message = "fail to login, please check your username or password.";
            }
            attributes.addFlashAttribute("message",message);
        }
        return "redirect:index";
    }


    /**
     * 登出
     * @param session
     * @return
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "/user/index";
    }

}
