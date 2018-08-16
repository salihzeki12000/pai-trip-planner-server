package edu.hanyang.trip_planning.tripData.daumLocalAPI;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

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

    public double getScore() {
        // 첫번째 나온게 별점이고, 마지막 나오는건 아니다.
        Elements num_em = doc.select(".num_em");
        double sum = 0.0;
        double num = 0.0;
        for (Element e : num_em) {
            if (e.text().length() > 0) {
                if (e.id().length() == 0) {
                    sum += Double.parseDouble(e.text());
                    num += 1.0;
                }
            }
        }
        return sum / num;
    }
}
