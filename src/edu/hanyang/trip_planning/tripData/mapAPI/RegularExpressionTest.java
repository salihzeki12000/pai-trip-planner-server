package edu.hanyang.trip_planning.tripData.mapAPI;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: 우영
 * Date: 15. 7. 24
 * Time: 오전 9:41
 * To change this template use File | Settings | File Templates.
 */
public class RegularExpressionTest {
    public static void main(String[] args) {
        String data = "MySQL5";

// 방법 1
        final String regex = "(\\D+)(\\d+)";
        String regex1 = "평일(월~토)";
// 방법 2
//final String regex = "([a-zA-Z]+)(\\s?)([0-9]*)";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(data);

        String v = null;
        String a = null;
        String n = null;

        int cnt = 0;
        while (m.find()) {
            v = m.group(1);
            a = m.group(2);
//n = m.group(3);
            cnt++;
        }

        System.out.println("1 : " + v);
        System.out.println("2 : " + a);
//System.out.println("3 : "+n);
    }


}


