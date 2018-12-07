package chat.core.db.model;

import java.util.Date;

/**
 * @auther a-de
 * @date 2018/10/10 21:02
 */
public class Permission extends AbstractBaseObject {
    /**
     * id
     */
    private Long id;
    /**
     * 父权限资源id
     */
    private Long parentId;
    /**
     * 资源级数
     */
    private Integer type;
    /**
     * 是否是后台
     */
    private Integer backStatus;
    /**
     * 资源名字
     */
    private String name;
    /**
     * 资源url
     */
    private String url;
    /**
     * 鉴权
     */
    private String authority;
    /**
     * 创建时间
     */
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getBackStatus() {
        return backStatus;
    }

    public void setBackStatus(Integer backStatus) {
        this.backStatus = backStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
