package top.hellooooo.jobsubmission.config;

import org.apache.commons.fileupload.ProgressListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import top.hellooooo.jobsubmission.pojo.ProgressEntity;
import top.hellooooo.jobsubmission.pojo.User;
import top.hellooooo.jobsubmission.util.RedisUtil;

import javax.servlet.http.HttpSession;

/**
 * @Author Q
 * @Date 13/09/2020 15:46
 * @Description
 */
@Component
public class FileUploadProgressListener implements ProgressListener{

    private final RedisUtil redisUtil;

    private HttpSession session;

    @Value("${custom.redis.progress}")
    private String UPLOAD_PROGRESS;

    public void setSession(HttpSession session) {
        this.session = session;
        User user = (User) session.getAttribute("user");
        ProgressEntity status = new ProgressEntity();
        redisUtil.set(UPLOAD_PROGRESS + user.getUsername(), status);
    }

    public FileUploadProgressListener(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @Override
    public void update(long l, long l1, int i) {
        User user = (User) session.getAttribute("user");
        ProgressEntity progressEntity = (ProgressEntity) redisUtil.get(UPLOAD_PROGRESS + user.getUsername());
        progressEntity.setpBytesRead(l);
        progressEntity.setpContentLength(l1);
        progressEntity.setpItems(i);
        redisUtil.set(UPLOAD_PROGRESS + user.getUsername(), progressEntity);
    }
}
