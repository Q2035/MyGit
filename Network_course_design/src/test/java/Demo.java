/*
功能：零比特插入
日期：2019/3/17
*/
import java.util.Scanner;
public class Demo {

    public static void main(String[] args) {
        //原来的二进制编码
        System.out.println("Please input binary code:");
        Scanner scan = new Scanner(System.in);
        String input = scan.next();
        System.out.println("original:" + input);

        //输出的编码
        String outCode = input.replace("11111","111110" );
        outCode = "01111110" + outCode + "01111110";
        System.out.println("output:" + outCode);

        //收到的编码
        String receiveNum = outCode;
        int count = 0;
        int start = receiveNum.indexOf("01111110") + 8;
        receiveNum = receiveNum.substring(8);
        int end = receiveNum.indexOf("01111110");
        receiveNum = receiveNum.substring(0, receiveNum.length()-8);
        System.out.println("receive:" + receiveNum);

        //处理后的编码
        receiveNum = receiveNum.replace("1111101","111111" );
        System.out.println("handle:" + receiveNum);
    }
}
