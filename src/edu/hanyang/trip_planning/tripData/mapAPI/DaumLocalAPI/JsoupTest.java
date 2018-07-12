package edu.hanyang.trip_planning.tripData.mapAPI.DaumLocalAPI;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 15. 7. 21
 * Time: 오후 4:42
 * To change this template use File | Settings | File Templates.
 */

public class JsoupTest {
    private static Logger logger = Logger.getLogger(JsoupTest.class);

    public static void main(String[] args) throws IOException {
        Document doc = Jsoup.connect("http://place.map.daum.net/8025234").get();
        Elements num_em = doc.select(".num_em");
        for (Element e : num_em) {
            logger.debug(e);
            logger.debug(e.text());
        }


        /**
         * <dl class="list_info">
         <dt class="tit">이용시간</dt>
         <dd class="cont">
         평일(월~토) 09:30 ~ 21:00			</dd>
         </dl>

         */
        Elements tit_list = doc.select("dl.list_info dt.tit");
        Elements cont_list = doc.select("dl.list_info dd.cont");
        int size = tit_list.size();
        for (int i = 0; i < size; i++) {
            String title = tit_list.get(i).text();
            String content = cont_list.get(i).text();
            logger.debug(title + "=" + content);
            if (title.equals("이용시간")) {
                logger.debug("이건이용시간이라고, " + content);
            }
            if (title.equals("휴무일")) {
                logger.debug("이건 휴무일 이라고, " + content);
            }


        }

    }
}
