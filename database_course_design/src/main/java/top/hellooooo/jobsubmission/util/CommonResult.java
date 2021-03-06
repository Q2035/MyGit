package top.hellooooo.jobsubmission.util;

public class CommonResult<T> {
    private String message;
    private T data;
    private boolean success;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "CommonResult{" +
                "message='" + message + '\'' +
                ", data=" + data +
                ", success=" + success +
                '}';
    }


    /**
     * 设置所有CommonResult的值
     * @param data
     * @param message
     * @param success
     */
    public void setAll(T data, String message, Boolean success) {
        this.data = data;
        this.message = message;
        this.success = success;
    }

}