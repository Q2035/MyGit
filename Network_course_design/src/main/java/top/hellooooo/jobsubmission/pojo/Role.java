package main.java.top.hellooooo.jobsubmission.pojo;

/**
 * - id 主键
 * - role_name 角色名称
 * - role_description 角色描述
 */
public class Role {
    private Integer id;
    private String roleName;
    private String roleDescription;

    public final static String ADMIN = "admin";
    public final static String STUDENT = "student";
    public final static String MANAGER = "manager";

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", roleName='" + roleName + '\'' +
                ", roleDescription='" + roleDescription + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleDescription() {
        return roleDescription;
    }

    public void setRoleDescription(String roleDescription) {
        this.roleDescription = roleDescription;
    }
}
