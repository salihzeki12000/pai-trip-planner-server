package services.qna;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 15. 5. 8
 * Time: 오후 5:25
 * To change this template use File | Settings | File Templates.
 */
public class SpatialStatementByLocation {

    public String poiTitle;
    public double distance;


    public SpatialStatementByLocation(String poiTitle, double distance) {
        this.poiTitle = poiTitle;
        this.distance = distance;
    }


    public String toString() {
        return poiTitle + "로부터 " + distance + "km 이내";
    }
}
