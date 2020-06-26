package main.java.top.hellooooo.jobsubmission.util;

import org.springframework.stereotype.Component;
import top.hellooooo.jobsubmission.pojo.Role;
import top.hellooooo.jobsubmission.pojo.User;


@Component
public class IndexUtil {
    /**
     * 通过用户权限信息跳转到指定页面
     * @param user
     * @return 用户跳转到的页面
     */
    public String getURLByUser(User user) {
        String redirectAddress = "";
        switch (user.getRole().getRoleName()) {
            case Role.ADMIN:
                redirectAddress = "admin/index";
                break;
            case Role.MANAGER:
                redirectAddress =  "manager/index";
                break;
            case Role.STUDENT:
                redirectAddress = "user/home";
                break;
        }
        return redirectAddress;
    }
}
