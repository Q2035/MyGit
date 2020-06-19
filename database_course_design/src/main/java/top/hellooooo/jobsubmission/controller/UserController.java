package top.hellooooo.jobsubmission.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import top.hellooooo.jobsubmission.pojo.Job;
import top.hellooooo.jobsubmission.pojo.Role;
import top.hellooooo.jobsubmission.pojo.User;
import top.hellooooo.jobsubmission.service.JobService;
import top.hellooooo.jobsubmission.service.UserService;
import top.hellooooo.jobsubmission.util.CommonResult;
import top.hellooooo.jobsubmission.util.IndexUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
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

    @Value("${file.basepath}")
    private String publicBasePath;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

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

    @GetMapping("/fileupload/{id}")
    public String upload(@PathVariable("id")Integer id,
                         Model model){
        Job job = jobService.getJobByJobId(id);
        model.addAttribute("job", job);
        return "/user/jobupload";
    }


    /**
     * 上传文件处理，需要通过id判断是否有这个任务
     * @param file
     * @param request
     * @return
     */
    @PostMapping("/fileupload")
    public CommonResult fileUpload(MultipartFile file,
                                   HttpServletRequest request){
        CommonResult result = new CommonResult();
        if (file.isEmpty()){
            logger.warn("{} upload an empty file.",request.getRemoteAddr());
        }
        String fileName = file.getOriginalFilename();
        String contentType = file.getContentType();
        logger.info("upload name:{} type:{}",fileName, contentType);
        String filePath;
        filePath = publicBasePath + fileName;
        File dest = new File(filePath);
//        查看是否存在目录
        if (!dest.getParentFile().exists()){
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            logger.error("upload error!");
            e.printStackTrace();
            result.setMessage("upload error!");
            result.setSuccess(false);
            return result;
        }
        result.setMessage("Success!");
        result.setSuccess(true);
        logger.info(result.toString());
        return result;
    }
}
