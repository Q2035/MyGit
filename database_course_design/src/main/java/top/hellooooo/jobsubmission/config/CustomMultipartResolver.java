package top.hellooooo.jobsubmission.config;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author Q
 * @Date 13/09/2020 15:52
 * @Description
 */
public class CustomMultipartResolver extends CommonsMultipartResolver {
    @Autowired
    private FileUploadProgressListener progressListener;

    @Override
    protected MultipartParsingResult parseRequest(HttpServletRequest request) throws MultipartException {
        FileUpload fileUpload = prepareFileUpload("UTF-8");
        progressListener.setSession(request.getSession());
        fileUpload.setProgressListener(progressListener);
        try {
            List<FileItem> fileItems = ((ServletFileUpload) fileUpload).parseRequest(request);
            return parseFileItems(fileItems, "UTF-8");
        } catch (FileUploadException e) {
            e.printStackTrace();
        }
        return super.parseRequest(request);
    }
}
