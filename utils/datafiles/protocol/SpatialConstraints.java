package edu.hanyang.protocol;


import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 15. 6. 2
 * Time: 오후 2:21
 * To change this template use File | Settings | File Templates.
 */
public class SpatialConstraints  {

    Set<AddressCode> symbolicSpatialConstraints = new HashSet<AddressCode>();
    Set<SpatialStatementByLocation> metricSpatialConstraints = new HashSet<SpatialStatementByLocation>();

    public void addSymbolicSpatialConstraints(AddressCode addressCode) {
        this.symbolicSpatialConstraints.add(addressCode);
    }


    public void addMetricSpatialConstraints(SpatialStatementByLocation spatialStatementByLocation) {
        this.metricSpatialConstraints.add(spatialStatementByLocation);
    }


    public Set<AddressCode> symbolicSpatialConstraints() {
        return symbolicSpatialConstraints;
    }


    public Set<SpatialStatementByLocation> metricSpatialConstraints() {
        return metricSpatialConstraints;
    }

    public String toString() {
        StringBuffer strbuf = new StringBuffer();
        if (symbolicSpatialConstraints.size() != 0) {
            strbuf.append(symbolicSpatialConstraints);
        }
        if (metricSpatialConstraints.size() * symbolicSpatialConstraints.size() != 0) {
            strbuf.append("\n");
        }
        if (metricSpatialConstraints.size() != 0) {
            strbuf.append(metricSpatialConstraints + "\t");
        }
        return strbuf.toString();
    }

    public static SpatialConstraints dummy1() {
        // 1. 서울 도봉구
        SpatialConstraints spatialConstraints1 = new SpatialConstraints();
        spatialConstraints1.addSymbolicSpatialConstraints(new AddressCode("대한민국", "서울", "도봉구"));
        spatialConstraints1.addSymbolicSpatialConstraints(new AddressCode("대한민국", "서울", "노원구"));
        spatialConstraints1.addSymbolicSpatialConstraints(new AddressCode("대한민국", "서울", "강북구"));
        return spatialConstraints1;
    }

    public static SpatialConstraints dummy2() {
        // 2. 한양대 근방 3km 이내
        SpatialConstraints spatialConstraints2 = new SpatialConstraints();
        SpatialStatementByLocation spatialStatementByPOI = new SpatialStatementByLocation(new Location(37.557338, 127.045681), 3);
        spatialConstraints2.addMetricSpatialConstraints(spatialStatementByPOI);
        return spatialConstraints2;
//        System.out.println("spatialConstraints2 = " + spatialConstraints2);

    }

    public static void test() {
        System.out.println("dummy1() = " + dummy1());
        System.out.println("dummy2() = " + dummy2());
    }

    public static void main(String[] args) {
        test();
    }
}
