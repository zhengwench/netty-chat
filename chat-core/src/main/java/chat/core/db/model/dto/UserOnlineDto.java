package chat.core.db.model.dto;

import chat.core.db.model.AbstractBaseObject;

/**
 * @auther a-de
 * @date 2018/11/20 20:46
 */
public class UserOnlineDto extends AbstractBaseObject {
    private Long id;
    private String userName;
    private String userIcon;
    private String roleIcon;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public String getRoleIcon() {
        return roleIcon;
    }

    public void setRoleIcon(String roleIcon) {
        this.roleIcon = roleIcon;
    }
}
