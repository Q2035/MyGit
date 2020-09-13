package top.hellooooo.jobsubmission.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.hellooooo.jobsubmission.exception.NoPermissionException;
import top.hellooooo.jobsubmission.pojo.*;
import top.hellooooo.jobsubmission.service.BlackListService;
import top.hellooooo.jobsubmission.service.JobService;
import top.hellooooo.jobsubmission.service.UserService;
import top.hellooooo.jobsubmission.util.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.Date;
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

    @Value("${custom.redis.progress}")
    private String UPLOAD_PROGRESS;

    private final RedisUtil redisUtil;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 构造器注入依赖
     * @param userService
     * @param indexUtil
     * @param jobService
     * @param blackListService
     * @param filenameParser
     */
    public UserController(UserService userService, IndexUtil indexUtil, JobService jobService, BlackListService blackListService, FilenameParser filenameParser, RedisUtil redisUtil) {
        this.userService = userService;
        this.indexUtil = indexUtil;
        this.jobService = jobService;
        this.blackListService = blackListService;
        this.filenameParser = filenameParser;
        this.redisUtil = redisUtil;
    }


    @PostMapping("/authentication")
    public String auth(@RequestParam("username") String username,
                     @RequestParam("password") String password,
                     HttpServletRequest request,
                     HttpServletResponse response,
                     Model model,
                     HttpSession session){
        User user = userService.getUserWithClazzAndRoleByUsername(username);
        if (user != null) {
            if (ifUserInBlackList(user)) {
                model.addAttribute("message", "The account has been frozen, please contact the administrator");
                return "index";
            }
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
//        将用户信息存入Session
            session.setAttribute("user",user);
//            如果登录者为管理员，则将所有未过期Job信息传回前端
            List<Job> unexpiredJobs;
            if (user.getRole().getRoleName().contains(Role.MANAGER)) {
                 unexpiredJobs = jobService.getUnexpiredJobs();
                List<Job> allJobs = jobService.getAllExpiredJobs();
                model.addAttribute("allJobs", allJobs);
//                 管理员直接跳转到管理员界面
            } else if (user.getRole().getRoleName().contains(Role.ADMIN)) {
                List<User> users = userService.getAllUsers();
                model.addAttribute("users",users);
                return redirectAddress;
            } else {
//                unexpiredJobs = jobService.getCurrentJobByUserId(user.getId());
                unexpiredJobs = userService.getAllUnExpireJobByUserId(user.getId());
                List<SubmitPerson> allJobs = userService.getAllExpiredJobSubmitInfoByUserId(user.getId());

                model.addAttribute("user", user);
                model.addAttribute("allJobs", allJobs);
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
                Integer loginCountFromCookie = getLoginCountFromCookie(request);
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
     * @return
     */
    Integer getLoginCountFromCookie(HttpServletRequest request) {
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
        return "index";
    }

    @GetMapping("/fileupload/{id}")
    public String upload(@PathVariable("id")Integer id,
                         Model model,
                         HttpServletRequest request){
        HttpSession session = request.getSession();
        Job job = jobService.getJobByJobId(id);
        User user = (User) session.getAttribute("user");
        SubmitPerson submitPersonByJobIdAndUserId = userService.getSubmitPersonByJobIdAndUserId(id, user.getId());
        if (submitPersonByJobIdAndUserId == null) {
            String msg = "No permission!";
            model.addAttribute("msg", msg);
        } else {
            if (submitPersonByJobIdAndUserId.getJob().getDeadline().getTime() < System.currentTimeMillis()) {
                String msg = "Sorry, It's overdue: " + submitPersonByJobIdAndUserId.getJob().getDeadline();
                model.addAttribute("msg", msg);
                return "error/5xx";
            }
        }
        model.addAttribute("job", job);
        model.addAttribute("username", user.getUsername());
        return "user/jobupload";
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
            logger.warn("file {}",file.getName());
            result.setMessage("ERROR! NO PERMISSION!");
            return result;
        }
        if (file.isEmpty()){
            logger.warn("{} upload an empty file.",request.getRemoteAddr());
        }
        String fileName = file.getOriginalFilename();
        logger.info("{} upload name:{}",user.getUsername(),fileName);
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
        filePath = publicBasePath + File.separator + suffix + File.separator + fileName;
        File dest = new File(filePath);
//        查看是否存在目录
        if (!dest.getParentFile().exists()){
            dest.getParentFile().mkdirs();
        }
        //            更改思路
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        doFileUpload(file, dest, user);
        //        到这里的话正常来说就是提交完成了
//        更新数据库即可
        jobService.updateJobAndSubmitPerson(user.getId(),jobId);
        result.setMessage("Success!");
        result.setSuccess(true);
        logger.info(result.toString());
        return result;
    }

//    private void doFileUpload(MultipartFile sourceFile, File destFile, User user) {
//        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(sourceFile.getInputStream());
//             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(destFile))) {
//            //        设置上传实体类
//            ProgressEntity progressEntity = new ProgressEntity();
//            progressEntity.setpContentLength(sourceFile.getSize());
//
//            redisUtil.set(UPLOAD_PROGRESS + user.getUsername(), progressEntity);
//            //            这里需要注意
//            byte[] bufferBytes = new byte[1024000];
//            long tempLength;
//            long startTime = System.currentTimeMillis();
////            只要没有读取到文件尾部，就不断读取
//            while ((tempLength = bufferedInputStream.read(bufferBytes)) != -1) {
//                bufferedOutputStream.write(bufferBytes);
////                从Redis中获取文件上传进度，并进行更新
//                progressEntity = (ProgressEntity) redisUtil.get(UPLOAD_PROGRESS + user.getUsername());
//                logger.info("+++++++++");
//                logger.info(progressEntity.toString());
//                progressEntity.setpBytesRead(progressEntity.getpBytesRead() + tempLength);
//                redisUtil.set(UPLOAD_PROGRESS + user.getUsername(), progressEntity);
//            }
//            logger.info("Time Spent: {} ms", System.currentTimeMillis() - startTime);
////            一旦上传完成，就将Redis的数据移除
//            redisUtil.remove(UPLOAD_PROGRESS + user.getUsername());
//            bufferedOutputStream.flush();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    @ResponseBody
    @GetMapping("/progress/{username}")
    public CommonResult<Integer> uploadProgress(@PathVariable("username") String username) {
        CommonResult<Integer> commonResult = new CommonResult<>();
        ProgressEntity progressEntity = (ProgressEntity) redisUtil.get(UPLOAD_PROGRESS + username);
        if (progressEntity != null) {
            logger.info("read {} all {} percent {}", progressEntity.getpBytesRead(), progressEntity.getpContentLength(), ((float) progressEntity.getpBytesRead() / progressEntity.getpContentLength()));
            commonResult.setAll((int)((float)progressEntity.getpBytesRead() / progressEntity.getpContentLength() * 100), "Get Progress", true);
            return commonResult;
        }
        commonResult.setAll(0, "No Progress Found.", false);
        logger.info(commonResult.toString());
        return commonResult;
    }
}
