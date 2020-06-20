package top.hellooooo.jobsubmission.pojo;

/**
 * 规定按$号开头的需要进行替换
 */
public class Filename {
    private Integer id;
//    最多支持八部分
    private Integer partCount;
    private String part1;
    private String part2;
    private String part3;
    private String part4;
    private String part5;
    private String part6;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPartCount() {
        return partCount;
    }

    public void setPartCount(Integer partCount) {
        this.partCount = partCount;
    }

    public String getPart1() {
        return part1;
    }

    public void setPart1(String part1) {
        this.part1 = part1;
    }

    public String getPart2() {
        return part2;
    }

    public void setPart2(String part2) {
        this.part2 = part2;
    }

    public String getPart3() {
        return part3;
    }

    public void setPart3(String part3) {
        this.part3 = part3;
    }

    public String getPart4() {
        return part4;
    }

    public void setPart4(String part4) {
        this.part4 = part4;
    }

    public String getPart5() {
        return part5;
    }

    public void setPart5(String part5) {
        this.part5 = part5;
    }

    public String getPart6() {
        return part6;
    }

    public void setPart6(String part6) {
        this.part6 = part6;
    }
}
