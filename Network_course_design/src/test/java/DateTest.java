import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTest {
    public static void main(String[] args) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final long a = formatter.parse("2020-06-20 12:51:00").getTime();
        System.out.println(a);
        final long b = System.currentTimeMillis();
        System.out.println(b);
        System.out.println(b - a < 24*60*60*1000);
        if (formatter.parse("2020-07-17 12:00:00:123").getTime() - System.currentTimeMillis() < 24*60*60*1000) {
            System.out.println("HI");
        }
//        DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date now = new Date();
//        System.out.println(now);
//        try {
//            System.out.println(simpleDateFormat.format(now));
//            Date parse = simpleDateFormat.parse("2020-02-03 23:32:11");
//            System.out.println(parse);
//        } catch (ParseException e) {
//
//            e.printStackTrace();
//        }
    }
}
