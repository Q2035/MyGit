package top.hellooooo.jobsubmission.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 对管理员需要下载的内容进行压缩
 */
public class ZipCompress {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private String zipFileName;	//目的地Zip文件

    private String sourceFileName;	//源文件

    public ZipCompress(String zipFileName, String sourceFileName) {
        this.zipFileName = zipFileName;
        this.sourceFileName = sourceFileName;
    }

    public void zip() throws IOException {
        logger.info("开始压缩...");
        //创建zip输出流
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
        File sourceFile = new File(sourceFileName);
        //调用函数
        compress(out, sourceFile, sourceFile.getName());
        out.close();
        logger.info("压缩完成！");
    }

    public void compress(ZipOutputStream out, File sourceFile, String base) throws IOException {
        //如果路径为目录（文件夹）
        if(sourceFile.isDirectory()) {
            //取出文件夹中的文件（或子文件夹）
            File[] flist = sourceFile.listFiles();
            if(flist.length == 0) {//如果文件夹为空，则只需在目的地zip文件中写入一个目录进入点
                logger.info(base + File.separator);
                out.putNextEntry(new ZipEntry(base + File.separator));
            } else {//如果文件夹不为空，则递归调用compress,文件夹中的每一个文件（或文件夹）进行压缩
                for(int i = 0; i < flist.length; i++) {
                    compress(out, flist[i], base+File.separator+flist[i].getName());
                }
            }
        } else {
            out.putNextEntry(new ZipEntry(base));
            FileInputStream fos = new FileInputStream(sourceFile);
            BufferedInputStream bis = new BufferedInputStream(fos);
            int len;
            byte[] buf = new byte[1024];
            logger.info(base);
            while((len=bis.read(buf, 0, 1024)) != -1) {
                out.write(buf, 0, len);
            }
            bis.close();
            fos.close();
        }
    }
}
