package main.java.top.hellooooo.jobsubmission.util;

public class CommonResult<T> {
    private String message;
    private T data;
    private boolean success;
    private String redirectURL;

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
                ", redirectURL='" + redirectURL + '\'' +
                '}';
    }

    public String getRedirectURL() {
        return redirectURL;
    }

    public void setRedirectURL(String redirectURL) {
        this.redirectURL = redirectURL;
    }
}