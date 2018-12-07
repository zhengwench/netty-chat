package chat.core.common.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @auther a-de
 * @date 2018/11/6 16:55
 */
public class ResultDo<T> implements Serializable {
    private boolean success = false;

    private String errorDesc;

    private T result;

    private List<T> list;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorDesc() {
        return errorDesc;
    }

    public void setErrorDesc(String errorDesc) {
        this.success = false;
        this.errorDesc = errorDesc;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.success = true;
        this.result = result;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.success = true;
        this.list = list;
    }
}
