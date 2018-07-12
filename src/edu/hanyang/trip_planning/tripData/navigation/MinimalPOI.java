package edu.hanyang.trip_planning.tripData.navigation;



import edu.hanyang.trip_planning.tripData.dataType.AddressCode;
import edu.hanyang.trip_planning.tripData.dataType.Location;
import edu.hanyang.trip_planning.tripData.poi.BasicPOI;

import java.util.Arrays;

/**
 * POI 표현에 필요한 최소한의 정보만을 담고 있음.
 * <p/>
 * 교통요지를 표현하는데 사용됨
 */
public class MinimalPOI {
    private String title;
    private Location location;
    private AddressCode addressCode;

    public MinimalPOI(String title, Location location, AddressCode addressCode) {
        this.title = title;
        this.location = location.deepCopy();
        this.addressCode = addressCode.deepCopy();
    }


    public MinimalPOI(BasicPOI basicPOI) {
        this.title = basicPOI.getTitle();
        this.location = basicPOI.getLocation().deepCopy();
        this.addressCode = basicPOI.getAddress().addressCode.deepCopy();
    }

    public MinimalPOI(String strs[]) {
        this.title = strs[0];
        this.location = new Location(Double.parseDouble(strs[1]), Double.parseDouble(strs[2]));
        if (strs.length == 4) {
            this.addressCode = new AddressCode(strs[3]);
        } else if (strs.length == 5) {
            this.addressCode = new AddressCode(strs[3], strs[4]);
        } else if (strs.length == 6) {
            this.addressCode = new AddressCode(strs[3], strs[4], strs[5]);
        } else {
            throw new RuntimeException("size mismatch for " + Arrays.toString(strs));
        }
    }

    public String getTitle() {
        return title;
    }

    public Location getLocation() {
        return location;
    }

    public AddressCode getAddressCode() {
        return addressCode;
    }

    public String toString() {
        return title + "@" + location.toString() + "\t 주소:" + addressCode;
    }


}
