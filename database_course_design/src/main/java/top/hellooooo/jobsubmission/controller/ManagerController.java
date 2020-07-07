package top.hellooooo.jobsubmission.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import top.hellooooo.jobsubmission.pojo.*;
import top.hellooooo.jobsubmission.service.JobService;
import top.hellooooo.jobsubmission.service.UserClazzService;
import top.hellooooo.jobsubmission.service.UserService;
import top.hellooooo.jobsubmission.util.CommonResult;
import top.hellooooo.jobsubmission.util.ZipCompress;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;
import java.io.*;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Controller
@RequestMapping("/job/manager")
public class ManagerController {

    private final UserClazzService userClazzService;

    private final JobService jobService;

    private final UserService userService;

    public SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${file.basepath}")
    private String basepath;

    public ManagerController(UserClazzService userClazzService, JobService jobService, UserService userService) {
        this.userClazzService = userClazzService;
        this.jobService = jobService;
        this.userService = userService;
    }

    @GetMapping({"/index","/"})
    public String index(Model model){
//        一方面加上当前未过期的作业，另一方面加上所有历史作业
        List<Job> unexpiredJobs = jobService.getUnexpiredJobs();
        List<Job> allJobs = jobService.getAllExpiredJobs();
        model.addAttribute("jobs", unexpiredJobs);
        model.addAttribute("allJobs", allJobs);
        return "manager/index";
    }


    /**
     * 跳转到job页面
     * 包含：
     * 所有发布的Job信息
     * Job新增链接
     * Job删除链接
     * Job修改链接
     *
     * @param model
     * @return
     */
    @GetMapping("/jobadd")
    public String jobShow(Model model) {
        List<Clazz> allClazz = userClazzService.getAllClazz();
        model.addAttribute("allclazz", allClazz);
        return "manager/job";
    }


    /**
     * Job发布
     * @param deadline
     * @param job_description
     * @param filename 要求的Job文件名格式 未指定的部分默认为空字符串，而不是null
     * @param clazz
     * @param session
     * @return
     */
    @Transactional
    @PostMapping("/jobadd")
    public String jobadd(String deadline,
                         String job_description,
                         Filename filename,
                         String clazz,
                         HttpSession session,
                         RedirectAttributes attributes) {
        try {
            Date parse = simpleDateFormat.parse(deadline);
//            实例化Job，将发起人、开始、结束时间等放入
            Job job = new Job();
            job.setStartTime(new Date());
//            设置Job提交时间
            job.setSubmitTime(new Date());
            job.setDeadline(parse);
            User currentUser = (User) session.getAttribute("user");
            job.setOriginator(currentUser.getId());
            job.setJobDescription(job_description);
            job.setSubmitCount(0);
//            从数据库获取选中班级的总人数
            List<Clazz> clazzById = userClazzService.getClazzById(clazz);
//            这个不能为0，应该直接接用班级人数
            job.setTotalCount(clazzById.stream().mapToInt(Clazz::getTotalCount).sum());
//            将Job信息插入数据库
            jobService.jobAdd(job);
//            将Job学生信息插入数据库
            List<SubmitPerson> userByClazzId = userClazzService.getUserByClazzId(clazz);
            List<SubmitPerson> submitPersonList = new ArrayList<>();
            for (SubmitPerson user : userByClazzId) {
                SubmitPerson e = new SubmitPerson();
//                无法获取JobID
                e.setJobId(getJobIdAfterInsert(session));
                e.setUserId(user.getId());
                submitPersonList.add(e);
            }
//            将生成的对象插入数据库
            jobService.insertJobSubmitPerson(submitPersonList);
            job = jobService.getJobAfterInsert(currentUser.getId());
//            将文件格式信息插入数据库
            filename.setJobId(job.getId());
            jobService.setFilename(filename);
        } catch (ParseException e) {
            attributes.addFlashAttribute("message","Error! The deadline is illegal!");
            e.printStackTrace();
        }
        return "redirect:/job/manager/index";
    }

    /**
     * @param id    Job的ID
     * @param model
     * @return
     */
    @GetMapping("/jobinfo/{id}")
    public String jobInfo(@PathVariable("id") Integer id,
                          Model model) {
        List<User> users = userService.getUnSubmitPersonWithJobId(id);
        model.addAttribute("unSubmitUsers", users);
        return "manager/jobinfo";
    }

    @ResponseBody
    @GetMapping("/filedownload/{jobId}")
    public CommonResult fileDownload(@PathVariable("jobId")Integer jobId,
                               HttpServletRequest request,
                               HttpServletResponse response){
        String sourceFileName = basepath + File.separator + Job.prefix + jobId;
        File parentPath = new File(sourceFileName);
        if (!parentPath.exists()) {
            parentPath.mkdirs();
        }
        String zipFileName = Job.prefix + jobId + ".zip";
//        eg：D:\Temp\zip\Job12.zip
        ZipCompress zipCompress = new ZipCompress(basepath + File.separator + "zip" + File.separator + zipFileName, sourceFileName);
        CommonResult commonResult = new CommonResult();
        try {
            zipCompress.zip();
        } catch (Exception e) {
            logger.error("ZIP:IOException ");
            e.printStackTrace();
            commonResult.setMessage("Internal Error!");
            return commonResult;
        }

//        获取压缩文件路径
        File file = new File(basepath + File.separator + "zip" + File.separator + zipFileName);
        if (!file.exists()){
            commonResult.setMessage("error,the file doesn't exist!");
            commonResult.setSuccess(false);
            logger.warn("user:{}, the file:{} doesn't exist",request.getRemoteAddr(),zipFileName);
            return commonResult;
        }
        String agent = request.getHeader("User-Agent");
        logger.info("browser agent:{}",agent);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/octet-stream");
        response.addHeader("Content-Disposition","attachment;filename="+ zipFileName);
//        设置文件大小
        response.setContentLengthLong(file.length());
        byte[] buffer = new byte[1024];
        try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            OutputStream os = response.getOutputStream()) {
            while (bis.read(buffer) != -1){
                os.write(buffer);
            }
            os.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return commonResult;
    }

    /**
     * 根据Session中的用户ID返回刚刚创建的Job对象ID
     *
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
