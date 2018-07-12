package edu.hanyang.trip_planning.tripData.dataType;

import com.google.gson.Gson;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 15. 5. 13
 * Time: 오후 5:43
 * To change this template use File | Settings | File Templates.
 */
public class AdjacentPOI {

    // 인접한 위치
    public String poiIdentifier;

    //  걸어서 도달가능한 시간 (분)
    public int arrivalTimeByWalk;

    public AdjacentPOI(String poiIdentifier, int arrivalTimeByWalk) {
        this.poiIdentifier = poiIdentifier;
        this.arrivalTimeByWalk = arrivalTimeByWalk;
    }

    @Override
    public String toString() {
        return "AdjacentPOI{" +
                "poiIdentifier='" + poiIdentifier + '\'' +
                ", arrivalTimeByWalk=" + arrivalTimeByWalk +
                '}';
    }

    public static void main(String[] args) {
        AdjacentPOI adjacentPOI = new AdjacentPOI("녹천역 1호선", 2);
        AdjacentPOI adjacentPOI2 = new AdjacentPOI("창동역 4호선", 10);
        Gson gson = new Gson();

        System.out.println(gson.toJson(adjacentPOI));

        Set<AdjacentPOI> set = new HashSet<AdjacentPOI>();
        set.add(adjacentPOI);
        set.add(adjacentPOI2);

        System.out.println(gson.toJson(set));

        AdjacentPOI adjacentPOIs[] = gson.fromJson(gson.toJson(set), AdjacentPOI[].class);
        System.out.println("Arrays.toString(adjacentPOIs) = " + Arrays.toString(adjacentPOIs));
    }
}
