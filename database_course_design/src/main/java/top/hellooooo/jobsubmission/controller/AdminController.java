package top.hellooooo.jobsubmission.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import top.hellooooo.jobsubmission.service.UserService;

@Controller
@RequestMapping("/job/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("/userdelete/{id}")
    public String index(Model model,
                        @PathVariable("id")Integer id){

        logger.info("the user [" + userService.getUserById(id) + "] will be deleted" );
        userService.deleteUser(id);
        return "admin/index";
    }
}
