package top.hellooooo;

import com.google.zxing.WriterException;
import top.hellooooo.qrcode.wrapper.QrCodeGenWrapper;
import top.hellooooo.qrcode.wrapper.QrCodeOptions;

import java.io.IOException;

public class QRCodeApplication {
    public static void main(String[] args) throws IOException, WriterException {
        String msg = "https://hellooooo.top";
//        QrCodeGenWrapper.of(msg).asFile("/tmp/qr.png");
        // 背景渲染方式，用背景图来填充二维码，对应下图中的右图
        String bg = "https://www.hellooooo.top/image/blog/2020/07/first_picture/529-800x450.jpg";
        QrCodeGenWrapper.of(msg)
                .setBgImg(bg)
                .setBgStyle(QrCodeOptions.BgImgStyle.OVERRIDE)
                .setBgW(500)
                .setBgH(500)
                .setW(500)
                .asFile("/tmp/bqr3.png");
    }
}
