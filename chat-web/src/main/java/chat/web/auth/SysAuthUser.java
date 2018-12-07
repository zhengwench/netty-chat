package chat.web.auth;

import java.io.Serializable;

/**
 * @auther a-de
 * @date 2018/11/6 13:59
 */
public class SysAuthUser implements Serializable {
    /**
     * id
     */
    private Long userId;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 角色id
     */
    private Long roleId;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 登录信息
     */
    private String token;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
