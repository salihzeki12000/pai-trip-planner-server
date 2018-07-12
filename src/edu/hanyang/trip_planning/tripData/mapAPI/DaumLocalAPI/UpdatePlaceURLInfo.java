package edu.hanyang.trip_planning.tripData.mapAPI.DaumLocalAPI;

import edu.hanyang.trip_planning.tripData.dataType.BusinessHour;
import edu.hanyang.trip_planning.tripData.dataType.ClosingDays;
import edu.hanyang.trip_planning.tripData.poi.BasicPOI;
import edu.hanyang.trip_planning.tripData.poi.POIManager;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 15. 7. 21
 * Time: 오후 4:25
 * To change this template use File | Settings | File Templates.
 */
public class UpdatePlaceURLInfo {
    private static Logger logger = Logger.getLogger(UpdatePlaceURLInfo.class);
    private Document doc;

    public UpdatePlaceURLInfo(String url) {
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            logger.debug(url);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public double getSatisfication() {
        // 첫번째 나온게 별점이고, 마지막 나오는건 아니다.
        Elements num_em = doc.select(".num_em");
        double sum = 0.0;
        double num = 0.0;
        for (Element e : num_em) {
//            logger.debug(e.html()+ "\t textlength="+e.text().length()+ "\t idlength="+e.id().length());
            if (e.text().length() > 0) {
                if (e.id().length() == 0) {
//                    logger.debug(e.text());
                    sum += Double.parseDouble(e.text());
                    num += 1.0;
                }
            }
//            logger.debug(e.text());
//            logger.debug(e.className());
//            logger.debug(e.id());
        }
        return sum / num;
    }

    public BusinessHour businessTime() {
        Elements tit_list = doc.select("dl.list_info dt.tit");
        Elements cont_list = doc.select("dl.list_info dd.cont");
        int size = tit_list.size();
        for (int i = 0; i < size; i++) {
            String title = tit_list.get(i).text();
            String content = cont_list.get(i).text();
//            logger.debug(title+"="+content);
            if (title.equals("이용시간")) {
//                logger.debug("이건이용시간이라고, " + content);
                return parseBusinessHour(content);
            }
        }
        return new BusinessHour();
    }

    public ClosingDays cLosingDays() {
        Elements tit_list = doc.select("dl.list_info dt.tit");
        Elements cont_list = doc.select("dl.list_info dd.cont");
        int size = tit_list.size();
        for (int i = 0; i < size; i++) {
            String title = tit_list.get(i).text();
            String content = cont_list.get(i).text();
//            logger.debug(title+"="+content);
            if (title.equals("휴무일")) {
//                logger.debug("휴무일 , " + content);
                return parseClosingDays(content);
            }
        }
        return new ClosingDays();
    }

    private BusinessHour parseBusinessHour(String str) {
        /**
         * 1. 일단 , 로 분리한다.
         *
         */
        String strArray[] = str.split(", ");
        BusinessHour businessHour = new BusinessHour();
        for (String s : strArray) {
//            logger.debug(s);
            Pattern p = Pattern.compile("\\d{2}:\\d{2} ~ \\d{2}:\\d{2}");
            Matcher m = p.matcher(str);
            boolean bFound = m.find();
            if (bFound) {
//                logger.debug(m.group(0));
                String open_close = m.group(0);
                if (s.contains("평일(월~금")) {
                    businessHour.setWeekDay(open_close);
                } else if (s.contains("평일(월~토)")) {
                    businessHour.setWeekDay(open_close);
                    businessHour.setSaturday(open_close);
                } else if (s.contains("상시(월~일)")) {
                    businessHour.setWeekDay(open_close);
                    businessHour.setSaturday(open_close);
                    businessHour.setSunday(open_close);
                } else if (s.contains("주말")) {
                    businessHour.setSaturday(open_close);
                    businessHour.setSunday(open_close);
                }
            }
        }


//        logger.debug(businessHour);


        // case 1. 00:00 ~ 24:00
        // case 2. 평일(월~금) 09:30 ~ 19:00, 09:30 ~ 21:00, 주말 09:30 ~ 16:00

        // 평일, 주말 구분 없는 문자는 버릴것 .
        return businessHour;
    }

    private ClosingDays parseClosingDays(String str) {

        /**
         * 매주 일요일
         */
        String strs[] = str.split(", ");
        ClosingDays closingDays = ClosingDays.parse(strs);
        return closingDays;

    }

    public void test() {

    }

    public static void regrextest() {
        Pattern p = Pattern.compile("\\d\\d\\d");
        Matcher m = p.matcher("a123b");
        System.out.println(m.find());
        System.out.println(m.matches());

        p = Pattern.compile("^\\d\\d\\d$");
        m = p.matcher("123");
        System.out.println(m.find());
        System.out.println(m.matches());
    }


    public static void testAll() {
        POIManager poiManager = POIManager.getInstance();
        for (BasicPOI poi : poiManager.getAll()) {
            String placeUrl = poi.getURL("place");
            UpdatePlaceURLInfo u = new UpdatePlaceURLInfo(placeUrl);
//            logger.debug(u.businessTime());
            if (u.cLosingDays().toString().length() > 0) {
                logger.debug(u.cLosingDays());
            }
//            logger.debug(placeUrl);
        }
    }

    public static void main(String[] args) {
//        regrextest();
        testAll();
//        UpdatePlaceURLInfo u = new UpdatePlaceURLInfo("http://place.map.daum.net/9078153");
//        u.parseBusinessHour("평일(월~금) 11:30 ~ 21:30, 주말 11:30 ~ 21:00");
//        UpdatePlaceURLInfo u = new UpdatePlaceURLInfo("http://place.map.daum.net/9078153");
//        logger.debug(u.getSatisfication());


    }

/* output:
true
false
true
true
*/

}
