package top.hellooooo.qrcode.wrapper;


import com.google.zxing.EncodeHintType;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import top.hellooooo.base.gif.GifDecoder;
import top.hellooooo.qrcode.constants.QuickQrUtil;
import top.hellooooo.qrcode.entity.DotSize;
import top.hellooooo.qrcode.helper.QrCodeRenderHelper;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yihui on 2017/7/17.
 */
@Data
public class QrCodeOptions {
    /**
     * 塞入二维码的信息
     */
    private String msg;

    /**
     * 生成二维码的宽
     */
    private Integer w;


    /**
     * 生成二维码的高
     */
    private Integer h;


    /**
     * 二维码信息(即传统二维码中的黑色方块) 绘制选项
     */
    private DrawOptions drawOptions;


    /**
     * 背景图样式选项
     */
    private BgImgOptions bgImgOptions;

    /**
     * logo 样式选项
     */
    private LogoOptions logoOptions;


    /**
     * todo 后续可以考虑三个都可以自配置
     * <p>
     * 三个探测图形的样式选项
     */
    private DetectOptions detectOptions;


    private Map<EncodeHintType, Object> hints;


    /**
     * 生成二维码图片的格式 png, jpg
     */
    private String picType;


    /**
     * true 表示生成的是动图
     *
     * @return
     */
    public boolean gifQrCode() {
        return bgImgOptions != null && bgImgOptions.getGifDecoder() != null;
    }


    /**
     * logo 的配置信息
     */
    @Data
    public static class LogoOptions {

        /**
         * logo 图片
         */
        private BufferedImage logo;

        /**
         * logo 样式
         */
        private LogoStyle logoStyle;

        /**
         * logo 占二维码的比例
         */
        private int rate;

        /**
         * true 表示有边框，
         * false 表示无边框
         */
        private boolean border;

        /**
         * 边框颜色
         */
        private Color borderColor;

        /**
         * 外围边框颜色
         */
        private Color outerBorderColor;

        /**
         * 用于设置logo的透明度
         */
        private Float opacity;

        public LogoOptions(BufferedImage logo, LogoStyle logoStyle, int rate, boolean border, Color borderColor, Color outerBorderColor, Float opacity) {
            this.logo = logo;
            this.logoStyle = logoStyle;
            this.rate = rate;
            this.border = border;
            this.borderColor = borderColor;
            this.outerBorderColor = outerBorderColor;
            this.opacity = opacity;
        }

        static class LogoOptionsBuilder {
            private BufferedImage logo;
            private LogoStyle logoStyle;
            private int rate;
            private boolean border;
            private Color borderColor;
            private Color outerBorderColor;
            private Float opacity;

            public LogoOptionsBuilder() {
            }

            LogoOptionsBuilder outerBorderColor(Color outerBorderColor) {
                this.outerBorderColor = outerBorderColor;
                return this;
            }
            LogoOptionsBuilder opacity(Float opacity) {
                this.opacity = opacity;
                return this;
            }

            LogoOptionsBuilder logo(BufferedImage logo) {
                this.logo = logo;
                return this;
            }
            LogoOptionsBuilder logoStyle(LogoStyle logoStyle) {
                this.logoStyle = logoStyle;
                return this;
            }
            LogoOptionsBuilder border(boolean border) {
                this.border = border;
                return this;
            }
            LogoOptionsBuilder rate(int rate) {
                this.rate = rate;
                return this;
            }
            LogoOptions build(){
                return new LogoOptions(logo, logoStyle, rate, border, borderColor, outerBorderColor, opacity);
            }

            LogoOptionsBuilder borderColor(Color color) {
                this.borderColor = color;
                return this;
            }
        }

        static LogoOptionsBuilder builder(){
            return new LogoOptionsBuilder();
        }
    }


    /**
     * 背景图的配置信息
     */
    @Data
    public static class BgImgOptions {
        /**
         * 背景图
         */
        private BufferedImage bgImg;

        /**
         * 动态背景图
         */
        private GifDecoder gifDecoder;

        /**
         * 背景图宽
         */
        private int bgW;

        /**
         * 背景图高
         */
        private int bgH;

        /**
         * 背景图样式
         */
        private BgImgStyle bgImgStyle;

        /**
         * if {@link #bgImgStyle} ==  QrCodeOptions.BgImgStyle.OVERRIDE，
         * 用于设置二维码的透明度
         */
        private float opacity;


        /**
         * if {@link #bgImgStyle} ==  QrCodeOptions.BgImgStyle.FILL
         * <p>
         * 用于设置二维码的绘制在背景图上的x坐标
         */
        private int startX;


        /**
         * if {@link #bgImgStyle} ==  QrCodeOptions.BgImgStyle.FILL
         * <p>
         * 用于设置二维码的绘制在背景图上的y坐标
         */
        private int startY;

        public BgImgOptions() {
        }

        public BgImgOptions(BufferedImage bgImg, GifDecoder gifDecoder, int bgW, int bgH, BgImgStyle bgImgStyle, float opacity, int startX, int startY) {
            this.bgImg = bgImg;
            this.gifDecoder = gifDecoder;
            this.bgW = bgW;
            this.bgH = bgH;
            this.bgImgStyle = bgImgStyle;
            this.opacity = opacity;
            this.startX = startX;
            this.startY = startY;
        }

        public static BgImgOptionsBuilder builder() {
            return new BgImgOptionsBuilder();
        }


        public int getBgW() {
            if (bgImgStyle == BgImgStyle.FILL && bgW == 0) {
                if (bgImg != null) {
                    return bgImg.getWidth();
                } else {
                    return gifDecoder.getFrame(0).getWidth();
                }
            }
            return bgW;
        }

        public int getBgH() {
            if (bgImgStyle == BgImgStyle.FILL && bgH == 0) {
                if (bgImg != null) {
                    return bgImg.getHeight();
                } else {
                    return gifDecoder.getFrame(0).getHeight();
                }
            }
            return bgH;
        }

        static class BgImgOptionsBuilder {
            /**
             * 背景图
             */
            private BufferedImage bgImg;

            /**
             * 动态背景图
             */
            private GifDecoder gifDecoder;

            /**
             * 背景图宽
             */
            private int bgW;

            /**
             * 背景图高
             */
            private int bgH;

            /**
             * 背景图样式
             */
            private BgImgStyle bgImgStyle;

            /**
             * if {@link #bgImgStyle} ==  QrCodeOptions.BgImgStyle.OVERRIDE，
             * 用于设置二维码的透明度
             */
            private float opacity;


            /**
             * if {@link #bgImgStyle} ==  QrCodeOptions.BgImgStyle.FILL
             * <p>
             * 用于设置二维码的绘制在背景图上的x坐标
             */
            private int startX;


            /**
             * if {@link #bgImgStyle} ==  QrCodeOptions.BgImgStyle.FILL
             * <p>
             * 用于设置二维码的绘制在背景图上的y坐标
             */
            private int startY;

            public BgImgOptions build() {
                return new BgImgOptions(bgImg, gifDecoder, bgW, bgH, bgImgStyle, opacity, startX, startY);
            }

            public BgImgOptionsBuilder() {
            }

            BgImgOptionsBuilder bgImg(BufferedImage bgImg) {
                this.bgImg = bgImg;
                return this;
            }
            BgImgOptionsBuilder gifDecoder(GifDecoder gifDecoder) {
                this.gifDecoder = gifDecoder;
                return this;
            }
            BgImgOptionsBuilder bgW(int bgW) {
                this.bgW = bgW;
                return this;
            }
            BgImgOptionsBuilder bgH(int bgH) {
                this.bgH = bgH;
                return this;
            }
            BgImgOptionsBuilder bgImgStyle(BgImgStyle bgImgStyle) {
                this.bgImgStyle = bgImgStyle;
                return this;
            }
            BgImgOptionsBuilder opacity(float opacity) {
                this.opacity = opacity;
                return this;
            }
            BgImgOptionsBuilder startX(int startX) {
                this.startX = startX;
                return this;
            }
            BgImgOptionsBuilder startY(int startY) {
                this.startY = startY;
                return this;
            }
        }
    }


    /**
     * 探测图形的配置信息
     */
    @Data
    public static class DetectOptions {
        private Color outColor;

        private Color inColor;

        /**
         * 默认探测图形，优先级高于颜色的定制（即存在图片时，用图片绘制探测图形）
         */
        private BufferedImage detectImg;

        /**
         * 左上角的探测图形
         */
        private BufferedImage detectImgLT;

        /**
         * 右上角的探测图形
         */
        private BufferedImage detectImgRT;

        /**
         * 左下角的探测图形
         */
        private BufferedImage detectImgLD;

        /**
         * true 表示探测图形单独处理
         * false 表示探测图形的样式更随二维码的主样式
         */
        private Boolean special;

        public static DetectOptionsBuilder builder() {
            return new DetectOptionsBuilder();
        }

        public Boolean getSpecial() {
            return BooleanUtils.isTrue(special);
        }

        public BufferedImage chooseDetectedImg(QrCodeRenderHelper.DetectLocation detectLocation) {
            switch (detectLocation) {
                case LD:
                    return detectImgLD == null ? detectImg : detectImgLD;
                case LT:
                    return detectImgLT == null ? detectImg : detectImgLT;
                case RT:
                    return detectImgRT == null ? detectImg : detectImgRT;
                default:
                    return null;
            }
        }

        static class DetectOptionsBuilder{

            private Color outColor;

            private Color inColor;

            /**
             * 默认探测图形，优先级高于颜色的定制（即存在图片时，用图片绘制探测图形）
             */
            private BufferedImage detectImg;

            /**
             * 左上角的探测图形
             */
            private BufferedImage detectImgLT;

            /**
             * 右上角的探测图形
             */
            private BufferedImage detectImgRT;

            /**
             * 左下角的探测图形
             */
            private BufferedImage detectImgLD;

            /**
             * true 表示探测图形单独处理
             * false 表示探测图形的样式更随二维码的主样式
             */
            private Boolean special;

            public DetectOptionsBuilder detectImg(BufferedImage detectImg) {
                this.detectImg = detectImg;
                return this;
            }

            public DetectOptionsBuilder special(boolean b) {
                this.special = b;
                return this;
            }

            public DetectOptionsBuilder outColor(Color outColor) {
                this.outColor = outColor;
                return this;
            }

            public DetectOptionsBuilder inColor(Color inColor) {
                this.inColor = inColor;
                return this;
            }

            public DetectOptions build() {
                return new DetectOptions(outColor, inColor, detectImg, detectImgLT, detectImgRT, detectImgLD, special);
            }

            public DetectOptionsBuilder detectImgLT(BufferedImage detectImg) {
                this.detectImgLT = detectImg;
                return this;
            }

            public DetectOptionsBuilder detectImgRT(BufferedImage detectImg) {
                this.detectImgRT = detectImg;
                return this;
            }

            public DetectOptionsBuilder detectImgLD(BufferedImage detectImg) {
                this.detectImgLD = detectImgLD;
                return this;
            }
        }

        public DetectOptions(Color outColor, Color inColor, BufferedImage detectImg, BufferedImage detectImgLT, BufferedImage detectImgRT, BufferedImage detectImgLD, Boolean special) {
            this.outColor = outColor;
            this.inColor = inColor;
            this.detectImg = detectImg;
            this.detectImgLT = detectImgLT;
            this.detectImgRT = detectImgRT;
            this.detectImgLD = detectImgLD;
            this.special = special;
        }
    }


    /**
     * 绘制二维码的配置信息
     */
    @Data
    public static class DrawOptions {
        /**
         * 着色颜色
         */
        private Color preColor;

        /**
         * 背景颜色
         */
        private Color bgColor;

        /**
         * 背景图
         */
        private BufferedImage bgImg;

        /**
         * 绘制样式
         */
        private DrawStyle drawStyle;

        /**
         * 生成文字二维码时的候字符池
         */
        private String text;

        /**
         * 生成文字二维码时的字体
         */
        private String fontName;

        /**
         * 文字二维码渲染模式
         */
        private TxtMode txtMode;

        /**
         * 字体样式
         *
         * {@link Font#PLAIN} 0
         * {@link Font#BOLD}  1
         * {@link Font#ITALIC} 2
         */
        private int fontStyle;

        /**
         * true 时表示支持对相邻的着色点进行合并处理 （即用一个大图来绘制相邻的两个着色点）
         * <p>
         * 说明： 三角形样式关闭该选项，因为留白过多，对识别有影响
         */
        private boolean enableScale;

        /**
         * 图片透明处填充，true则表示透明处用bgColor填充； false则透明处依旧透明
         */
        private boolean diaphaneityFill;

        /**
         * 渲染图
         */
        private Map<DotSize, BufferedImage> imgMapper;

        public BufferedImage getImage(int row, int col) {
            return getImage(DotSize.create(row, col));
        }

        public BufferedImage getImage(DotSize dotSize) {
            return imgMapper.get(dotSize);
        }

        /**
         * 获取二维码绘制的文字
         *
         * @return
         */
        public String getDrawQrTxt() {
            return QuickQrUtil.qrTxt(text, txtMode != null && txtMode == TxtMode.RANDOM);
        }

        public static DrawOptionsBuilder builder() {
            return new DrawOptionsBuilder();
        }

        public static class DrawOptionsBuilder {
            /**
             * 二维码居中 1对应的着色颜色
             */
            private Color preColor;

            /**
             * 二维码矩阵中 0对应的背景颜色
             */
            private Color bgColor;

            /**
             * 透明度填充，如绘制二维码的图片中存在透明区域，若这个参数为true，则会用bgColor填充透明的区域；若为false，则透明区域依旧是透明的
             */
            private boolean diaphaneityFill;

            /**
             * 文字二维码中，用于渲染的文字库，支持按字符顺序or随机两种展现方式（说明：英文不友好）
             */
            private String text;

            /**
             * 文字二维码，渲染模式
             */
            private TxtMode txtMode;

            /**
             * 文字二维码，字体名
             */
            private String fontName;

            /**
             * 字体样式
             *
             * {@link Font#PLAIN} 0
             * {@link Font#BOLD}  1
             * {@link Font#ITALIC} 2
             */
            private Integer fontStyle;

            /**
             * 二维码矩阵中，0点对应绘制的背景图片， 1点对应绘制的图片在 imgMapper 中
             */
            private BufferedImage bgImg;

            /**
             * 二维码绘制样式
             */
            private DrawStyle drawStyle;


            /**
             * true 时表示支持对相邻的着色点进行合并处理 （即用一个大图来绘制相邻的两个着色点）
             * <p>
             * 说明： 三角形样式关闭该选项，因为留白过多，对识别有影响
             */
            private boolean enableScale;

            /**
             * 渲染图
             */
            private Map<DotSize, BufferedImage> imgMapper;

            public DrawOptionsBuilder() {
                imgMapper = new HashMap<>();
            }

            public DrawOptionsBuilder preColor(Color preColor) {
                this.preColor = preColor;
                return this;
            }

            public DrawOptionsBuilder bgColor(Color bgColor) {
                this.bgColor = bgColor;
                return this;
            }

            public DrawOptionsBuilder diaphaneityFill(boolean fill) {
                this.diaphaneityFill = fill;
                return this;
            }

            public DrawOptionsBuilder bgImg(BufferedImage image) {
                this.bgImg = image;
                return this;
            }

            public DrawOptionsBuilder drawStyle(DrawStyle drawStyle) {
                this.drawStyle = drawStyle;
                return this;
            }

            public DrawOptionsBuilder text(String text) {
                this.text = text;
                return this;
            }

            public DrawOptionsBuilder txtMode(TxtMode txtMode) {
                this.txtMode = txtMode;
                return this;
            }

            public DrawOptionsBuilder fontName(String fontName) {
                this.fontName = fontName;
                return this;
            }

            public DrawOptionsBuilder fontStyle(int fontStyle) {
                this.fontStyle = fontStyle;
                return this;
            }

            public DrawOptionsBuilder enableScale(boolean enableScale) {
                this.enableScale = enableScale;
                return this;
            }

            public DrawOptionsBuilder drawImg(int row, int column, BufferedImage image) {
                imgMapper.put(new DotSize(row, column), image);
                return this;
            }

            public DrawOptions build() {
                DrawOptions drawOptions = new DrawOptions();
                drawOptions.setBgColor(this.bgColor);
                drawOptions.setBgImg(this.bgImg);
                drawOptions.setPreColor(this.preColor);
                drawOptions.setDrawStyle(this.drawStyle);
                drawOptions.setEnableScale(this.enableScale);
                drawOptions.setImgMapper(this.imgMapper);
                drawOptions.setDiaphaneityFill(this.diaphaneityFill);
                drawOptions.setText(text == null ? QuickQrUtil.DEFAULT_QR_TXT : text);
                drawOptions.setTxtMode(txtMode == null ? TxtMode.ORDER : txtMode);
                drawOptions.setFontName(fontName == null ? QuickQrUtil.DEFAULT_FONT_NAME : fontName);
                drawOptions.setFontStyle(fontStyle == null ? QuickQrUtil.DEFAULT_FONT_STYLE : fontStyle);
                return drawOptions;
            }
        }
    }


    /**
     * logo的样式
     */
    public enum LogoStyle {
        ROUND, NORMAL, CIRCLE;


        public static LogoStyle getStyle(String name) {
            return LogoStyle.valueOf(name.toUpperCase());
        }
    }


    /**
     * 背景图样式
     */
    public enum BgImgStyle {
        /**
         * 设置二维码透明度，然后全覆盖背景图
         */
        OVERRIDE,

        /**
         * 将二维码填充在背景图的指定位置
         */
        FILL,


        /**
         * 背景图穿透显示, 即二维码主题色为透明，由背景图的颜色进行填充
         */
        PENETRATE,;


        public static BgImgStyle getStyle(String name) {
            return "fill".equalsIgnoreCase(name) ? FILL : OVERRIDE;
        }
    }


    /**
     * 绘制二维码信息的样式
     */
    public enum DrawStyle {
        RECT { // 矩形

            @Override
            public void draw(Graphics2D g2d, int x, int y, int w, int h, BufferedImage img, String txt) {
                g2d.fillRect(x, y, w, h);
            }

            @Override
            public boolean expand(DotSize dotSize) {
                return dotSize.getRow() == dotSize.getCol();
            }
        }, CIRCLE { // 圆点

            @Override
            public void draw(Graphics2D g2d, int x, int y, int w, int h, BufferedImage img, String txt) {
                g2d.fill(new Ellipse2D.Float(x, y, w, h));
            }

            @Override
            public boolean expand(DotSize dotSize) {
                return dotSize.getRow() == dotSize.getCol();
            }
        }, TRIANGLE { // 三角形

            @Override
            public void draw(Graphics2D g2d, int x, int y, int w, int h, BufferedImage img, String txt) {
                int px[] = {x, x + (w >> 1), x + w};
                int py[] = {y + w, y, y + w};
                g2d.fillPolygon(px, py, 3);
            }

            @Override
            public boolean expand(DotSize expandType) {
                return false;
            }
        }, DIAMOND { // 五边形-钻石

            @Override
            public void draw(Graphics2D g2d, int x, int y, int size, int h, BufferedImage img, String txt) {
                int cell4 = size >> 2;
                int cell2 = size >> 1;
                int px[] = {x + cell4, x + size - cell4, x + size, x + cell2, x};
                int py[] = {y, y, y + cell2, y + size, y + cell2};
                g2d.fillPolygon(px, py, 5);
            }

            @Override
            public boolean expand(DotSize dotSize) {
                return dotSize.getRow() == dotSize.getCol();
            }
        }, SEXANGLE { // 六边形

            @Override
            public void draw(Graphics2D g2d, int x, int y, int size, int h, BufferedImage img, String txt) {
                int add = size >> 2;
                int px[] = {x + add, x + size - add, x + size, x + size - add, x + add, x};
                int py[] = {y, y, y + add + add, y + size, y + size, y + add + add};
                g2d.fillPolygon(px, py, 6);
            }

            @Override
            public boolean expand(DotSize dotSize) {
                return dotSize.getRow() == dotSize.getCol();
            }
        }, OCTAGON { // 八边形

            @Override
            public void draw(Graphics2D g2d, int x, int y, int size, int h, BufferedImage img, String txt) {
                int add = size / 3;
                int px[] = {x + add, x + size - add, x + size, x + size, x + size - add, x + add, x, x};
                int py[] = {y, y, y + add, y + size - add, y + size, y + size, y + size - add, y + add};
                g2d.fillPolygon(px, py, 8);
            }

            @Override
            public boolean expand(DotSize dotSize) {
                return dotSize.getRow() == dotSize.getCol();
            }
        }, IMAGE { // 自定义图片

            @Override
            public void draw(Graphics2D g2d, int x, int y, int w, int h, BufferedImage img, String txt) {
                g2d.drawImage(img.getScaledInstance(w, h, Image.SCALE_SMOOTH), x, y, null);
            }

            @Override
            public boolean expand(DotSize expandType) {
                return true;
            }
        },

        TXT { // 文字绘制

            @Override
            public void draw(Graphics2D g2d, int x, int y, int w, int h, BufferedImage img, String txt) {
                Font oldFont = g2d.getFont();
                if (oldFont.getSize() != w) {
                    Font newFont = QuickQrUtil.font(oldFont.getName(), oldFont.getStyle(), w);
                    g2d.setFont(newFont);
                }
                g2d.drawString(txt, x, y + w);
                g2d.setFont(oldFont);
            }

            @Override
            public boolean expand(DotSize dotSize) {
                return dotSize.getRow() == dotSize.getCol();
            }
        };

        private static Map<String, DrawStyle> map;

        static {
            map = new HashMap<>(10);
            for (DrawStyle style : DrawStyle.values()) {
                map.put(style.name(), style);
            }
        }

        public static DrawStyle getDrawStyle(String name) {
            if (StringUtils.isBlank(name)) { // 默认返回矩形
                return RECT;
            }


            DrawStyle style = map.get(name.toUpperCase());
            return style == null ? RECT : style;
        }


        public abstract void draw(Graphics2D g2d, int x, int y, int w, int h, BufferedImage img, String txt);


        /**
         * 返回是否支持绘制自定义图形的扩展
         *
         * @param dotSize
         * @return
         */
        public abstract boolean expand(DotSize dotSize);
    }


    public enum TxtMode {
        /***
         * 文字二维码，随机模式
         */
        RANDOM, /**
         * 文字二维码，顺序模式
         */
        ORDER;
    }
}
