import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArrayTest {
    public static void main(String[] args) {
        Integer[] list = new Integer[]{1, 2, 4};
        String temp = "temp.txt";

        Arrays.stream(temp.split(".")).forEach(System.out::println);
    }
}
