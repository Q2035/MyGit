package top.hellooooo.jobsubmission.pojo;

import top.hellooooo.jobsubmission.util.AccountStatus;

/**
 * - id 主键
 * - username 用户名 UNIQUE
 * - nickname 昵称
 * - avatar 头像
 * - email 邮箱
 * - account_status 账户状态 封号、冻结、违规
 * - description 保留字段
 */
public class User {
    private Integer id;
    private String username;
    private String nickname;
    private String avatar;
    private String email;
    private AccountStatus accountStatus;
    private String description;
}
