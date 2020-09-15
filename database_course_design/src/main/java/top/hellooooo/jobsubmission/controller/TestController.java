package top.hellooooo.jobsubmission.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import top.hellooooo.jobsubmission.mapper.UserClazzMapper;
import top.hellooooo.jobsubmission.mapper.UserMapper;
import top.hellooooo.jobsubmission.pojo.User;
import top.hellooooo.jobsubmission.service.UserService;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

//@RestController
//@ResponseBody
//@RequestMapping("/test")
@Component
public class TestController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserClazzMapper userClazzMapper;

    @RequestMapping("/t2")
    public List<User> t2(){
        Integer begin = 180604102;
        User user;
        List<User> hi = new ArrayList<>();
        while (begin < 180604140) {
            user = new User();
            user.setUsername(String.valueOf(begin));
            String password = DigestUtils.md5DigestAsHex(String.valueOf(begin).getBytes());
            user.setPassword(password);
            begin += 1;
            userService.setUserWithRole(user,2);
            hi.add(user);
        }
        return hi;
    }

    @RequestMapping("/t3")
    public List<User> t3(){
        Integer begin = 180604103;
        User user;
        List<User> hi = new ArrayList<>();
        while (begin < 180604140) {
            user = new User();
            user.setUsername(String.valueOf(begin));
            String password = DigestUtils.md5DigestAsHex(String.valueOf(begin).getBytes());
            user.setPassword(password);
            begin += 1;
            user = userService.getUserByUsername(user.getUsername());
            userService.setClazz(user.getId(),1);
            hi.add(user);
        }
        return hi;
    }

    @RequestMapping("/t4")
    public List<User> t4(){
        File file = new File("D:\\users.txt");
        List<User> users = new ArrayList<>();
        int start = 44;
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))){
            String temp = reader.readLine();
            while (temp != null) {
                String[] split = temp.split("\t");
                User user = new User();
                user.setUsername(split[1]);
                user.setNickname(split[2]);
                user.setPassword(md5(split[1]));
                userService.setUserWithRole(user,2);
                userClazzMapper.setClazz(start++, 2);
                users.add(user);
                temp = reader.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(start);
        return users;
    }

    @GetMapping("/t5")
    public String  t5(){
        return md5("180604124");
    }

    // 需要加密的字符串
    private String md5(String src){
        StringBuffer sb = new StringBuffer();
        try {
            // 加密对象，指定加密方式
            MessageDigest md5 = MessageDigest.getInstance("md5");
            // 准备要加密的数据
            byte[] b = src.getBytes();
            // 加密
            byte[] digest = md5.digest(b);
            // 十六进制的字符
            char[] chars = new char[] { '0', '1', '2', '3', '4', '5',
                    '6', '7' , '8', '9', 'A', 'B', 'C', 'D', 'E','F' };
            // 处理成十六进制的字符串(通常)
            for (byte bb : digest) {
                sb.append(chars[(bb >> 4) & 15]);
                sb.append(chars[bb & 15]);
            }
            // 打印加密后的字符串
            System.out.println(sb);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return sb.toString().toLowerCase();
    }
}
