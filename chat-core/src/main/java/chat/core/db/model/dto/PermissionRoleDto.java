package chat.core.db.model.dto;

import chat.core.db.model.AbstractBaseObject;

import java.util.List;

/**
 * @auther a-de
 * @date 2018/10/12 13:45
 */
public class PermissionRoleDto extends AbstractBaseObject {
    private Long id;
    private String name;
    private String url;
    private List<PermissionRoleDto> list;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<PermissionRoleDto> getList() {
        return list;
    }

    public void setList(List<PermissionRoleDto> list) {
        this.list = list;
    }
}
