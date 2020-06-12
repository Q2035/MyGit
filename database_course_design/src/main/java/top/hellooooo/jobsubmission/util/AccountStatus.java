package top.hellooooo.jobsubmission.util;

public enum AccountStatus {
    NORMAL(0),FREEZE(1);
    int status;

    AccountStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
