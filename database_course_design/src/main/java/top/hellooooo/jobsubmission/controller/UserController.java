package top.hellooooo.jobsubmission.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.hellooooo.jobsubmission.pojo.*;
import top.hellooooo.jobsubmission.service.BlackListService;
import top.hellooooo.jobsubmission.service.JobService;
import top.hellooooo.jobsubmission.service.UserService;
import top.hellooooo.jobsubmission.util.AccountStatus;
import top.hellooooo.jobsubmission.util.CommonResult;
import top.hellooooo.jobsubmission.util.FilenameParser;
import top.hellooooo.jobsubmission.util.IndexUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.List;

@RequestMapping("job/user")
@Controller
public class UserController {

    private final UserService userService;

    private final IndexUtil indexUtil;

    private final JobService jobService;

    private final BlackListService blackListService;

    private final FilenameParser filenameParser;

    @Value("${file.basepath}")
    private String publicBasePath;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 构造器注入依赖
     * @param userService
     * @param indexUtil
     * @param jobService
     * @param blackListService
     * @param filenameParser
     */
    public UserController(UserService userService, IndexUtil indexUtil, JobService jobService, BlackListService blackListService, FilenameParser filenameParser) {
        this.userService = userService;
        this.indexUtil = indexUtil;
        this.jobService = jobService;
        this.blackListService = blackListService;
        this.filenameParser = filenameParser;
    }

    /**
     * 登录认证
     * @param username
     * @param password
     * @param session
     * @return
     */
    @PostMapping("/authentication")
    public String auth(@RequestParam("username")String username,
                     @RequestParam("password")String password,
                     HttpServletRequest request,
                     HttpServletResponse response,
                     Model model,
                     HttpSession session){
//        将用户信息存入Session
        User user = userService.getUserWithClazzAndRoleByUsername(username);
        if (ifUserInBlackList(user)) {
            model.addAttribute("message", "The account has been frozen, please contact the administrator");
            return "index";
        }
        int userLoginCount = 1;
//        登录成功
        if (user != null
            && user.getPassword().equals(password)){
//            登录成功后就清空Cookie中的错误次数统计
            removeTheCountOfWrongPassCookie(request,response);
            String redirectAddress;
            user.setPassword(null);
            redirectAddress = indexUtil.getURLByUser(user);
            session.setAttribute("user",user);
//            如果登录者为管理员，则将所有未过期Job信息传回前端
            List<Job> unexpiredJobs;
            if (user.getRole().getRoleName().contains(Role.MANAGER)) {
                 unexpiredJobs = jobService.getUnexpiredJobs();
            } else if (user.getRole().getRoleName().contains(Role.ADMIN)) {

            } else {
                unexpiredJobs = jobService.getCurrentJobByUserId(user.getId());
            }

            model.addAttribute("jobs", unexpiredJobs);
            return redirectAddress;
        }else {
            //            提示密码错误
            String message = "";
            if (user == null) {
                message = "can't find the user in db, please check the username";
            }else {
    //            之前登录过
                Integer loginCountFromCookie = getLoginCountFromCookie(request, username);
                if (loginCountFromCookie != null) {
                    userLoginCount = loginCountFromCookie + 1;
                }
                Cookie cookie = new Cookie(BlackList.USER_COOKIE,String.valueOf(userLoginCount));
                cookie.setPath("/");
//                默认30分钟
                cookie.setMaxAge(30 * 60);
                response.addCookie(cookie);

                if (userLoginCount > BlackList.MAX_FAILURE_COUNT) {
                    message = "Too many errors and the account {" + username + "} is frozen!\n";
    //                更新用户状态
                    blackListService.setBlackListByUser(user);
                    blackListService.updateUserAccountStatus(user.getId(), AccountStatus.TOO_MANY_PASSWORD_ERRORS);
                }
                message += "fail to login, please check your username or password.";
            }
            model.addAttribute("message",message);
        }
        return "index";
    }

    /**
     * 如果Cookie中不存在对应信息，返回null
     * @param request
     * @param username
     * @return
     */
    Integer getLoginCountFromCookie(HttpServletRequest request,String username) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(BlackList.USER_COOKIE)) {
                return Integer.valueOf(cookie.getValue());
            }
        }
        return null;
    }

    /**
     * 一旦用户登录成功，删除之前Cookie中的信息，防止用户被冻结
     * @param request
     * @param response
     */
    void removeTheCountOfWrongPassCookie(HttpServletRequest request,
                                         HttpServletResponse response){
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(BlackList.USER_COOKIE)) {
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }
    }

    /**
     * 判断用户是否在黑名单中
     * @param userByUsername
     * @return
     */
    boolean ifUserInBlackList(User userByUsername){
        if (userByUsername.getAccountStatus() == AccountStatus.TOO_MANY_PASSWORD_ERRORS
            || userByUsername.getAccountStatus() == AccountStatus.UNAUTHORIZED_ACCESS_TO) {
            return true;
        }
        return false;
    }

    /**
     * 登出
     * @param session
     * @return
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "/index";
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
    @ResponseBody
    @PostMapping("/fileupload")
    public CommonResult fileUpload(@RequestParam("file") MultipartFile file,
                                   @RequestParam("jobId")Integer jobId,
                                   HttpServletRequest request){
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        Job job = jobService.getJobByUserIdAndJobId(user.getId(), jobId);
//        判断当前用户是否具有相应的Job，否则无权限提交
        CommonResult result = new CommonResult();
        if (job == null) {
            logger.warn("user {} wants to upload file but is rejected! ",user.getUsername());
            logger.warn("file {}",file.getOriginalFilename());
            result.setMessage("ERROR! NO PERMISSION!");
            return result;
        }
        if (file.isEmpty()){
            logger.warn("{} upload an empty file.",request.getRemoteAddr());
        }
        String fileName = file.getOriginalFilename();
        String contentType = file.getContentType();
        logger.info("upload name:{} type:{}",fileName, contentType);
        String[] split = fileName.split("\\.");
//        从数据库中取出Filename对象
        Filename filenameByJobId = jobService.getFilenameByJobId(jobId);
//        解析出文件名的前半部分，之后只需要加上提交文件本来的文件后缀即可
        String parseFilename = filenameParser.parseFilename(filenameByJobId, user);
        fileName = parseFilename + (split.length != 0 ? "." + split[split.length - 1]: "");
        String filePath;
//        为了保存多个Job的文件，需要加上每个Job特定字符
        String suffix = Job.prefix + job.getId();
//        例如：/basepath/JOB1/temp.txt
//        这里需要加上班级
        filePath = publicBasePath + suffix + File.separator + fileName;
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
//        到这里的话正常来说就是提交完成了
//        更新数据库即可
        jobService.updateJobAndSubmitPerson(user.getId(),jobId);
        result.setMessage("Success!");
        result.setSuccess(true);
        logger.info(result.toString());
        return result;
    }
}
