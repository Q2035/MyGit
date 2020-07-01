package top.hellooooo.netjobsubmission.util;

import org.springframework.stereotype.Component;
import top.hellooooo.netjobsubmission.pojo.Filename;
import top.hellooooo.netjobsubmission.pojo.User;

@Component
public class FilenameParser {

    public static String placeholder = "$";

    public String parseFilename(Filename filename, User user){
        if (filename.getSeparat().equals("")) {
            filename.setSeparat(" ");
        }
        StringBuffer stringBuffer = new StringBuffer();
        int tempCount = filename.getPartCount();
//        所有的Part
        String [] parts = new String[]{filename.getPart1(),filename.getPart2(),filename.getPart3(),filename.getPart4(),filename.getPart5(),filename.getPart6()};
        for (int i = 0; i < tempCount; i++) {
            if (!parts[i].equals("")) {
//                替换占位符
                if (parts[i].startsWith(placeholder)) {
//                    把开头的$号去了
                    switch (parts[i].substring(1)) {
                        case "username":
                            stringBuffer.append(user.getUsername());
                            break;
                        case "classname":
                            stringBuffer.append(user.getClazz().getClazzName());
                            break;
                        case "nickname":
                            stringBuffer.append(user.getNickname());
                            break;
                    }
//                否则，直接接在后面即可
                }else {
                    stringBuffer.append(parts[i]);
                }
            }
            if (i != tempCount - 1) {
                stringBuffer.append(filename.getSeparat());
            }
        }
        return stringBuffer.toString();
    }
}
