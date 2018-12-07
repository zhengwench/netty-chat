package chat.core.db.model.query;

/**
 * @auther a-de
 * @date 2018/11/24 19:53
 */
public class ServerModelQuery extends BaseQuery {
    private String serverIp;

    private String serverDomain;

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getServerDomain() {
        return serverDomain;
    }

    public void setServerDomain(String serverDomain) {
        this.serverDomain = serverDomain;
    }
}
