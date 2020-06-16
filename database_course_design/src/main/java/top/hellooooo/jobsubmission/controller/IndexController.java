package top.hellooooo.jobsubmission.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import top.hellooooo.jobsubmission.pojo.User;
import top.hellooooo.jobsubmission.util.IndexUtil;

import javax.servlet.http.HttpSession;

@RequestMapping("/job")
@Controller
public class IndexController {

    @Autowired
    private IndexUtil indexUtil;

    @RequestMapping({"/user/index","/user/","/user/login"})
    public String userIndex(HttpSession session){
//        如果Session中存在对应用户信息，则跳过登录，直接进入用户界面
        Object user = session.getAttribute("user");
        if (user != null) {
            String urlByUser = indexUtil.getURLByUser((User) user);
            return urlByUser;
        }
        return "index";
    }
}
