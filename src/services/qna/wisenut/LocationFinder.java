package services.qna.wisenut;


import edu.hanyang.trip_planning.tripData.dataType.Location;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 15. 8. 4
 * Time: 오후 8:16
 * To change this template use File | Settings | File Templates.
 */
public class LocationFinder {

    public static Location getLocationByName(String locationName) {
        if (locationName.equals("송도")) {
            return new Location(37.384385, 126.646251);
        }
        if (locationName.equals("연수구")) {
            return new Location(37.422506, 126.683601);
        }
        if (locationName.equals("구로디지털단지역")) {
            return new Location(37.485084, 126.901386);
        }
        throw new RuntimeException("No such location as " + locationName);
    }
}
