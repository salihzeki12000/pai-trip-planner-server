package edu.hanyang.trip_planning.optimize.constraints.categoryConstraint;

import edu.hanyang.trip_planning.tripData.dataType.POIType;
import org.apache.log4j.Logger;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.StringTokenizer;

public class CategoryConstraint {
    private static Logger logger = Logger.getLogger(CategoryConstraint.class);
    private Set<POIType> poiTypes;
    private double startHour;
    private double endHour;
    private int minCount;
    private int maxCount;

    public CategoryConstraint(){}
    public CategoryConstraint(double startHour, double endHour, int minCount, int maxCount, String... categoryTypeStrs) {
        poiTypes = new HashSet<>();
        for (String s : categoryTypeStrs) {
            StringTokenizer stringTokenizer = new StringTokenizer(s,".");
            int countToken = stringTokenizer.countTokens();

            POIType poiType = null;
            if (countToken==1){
                poiType= new POIType(stringTokenizer.nextToken());
            }
            else if (countToken==2){
                poiType= new POIType(stringTokenizer.nextToken(),stringTokenizer.nextToken());
            }
            else if (countToken==3) {
                poiType= new POIType(stringTokenizer.nextToken(),stringTokenizer.nextToken(),stringTokenizer.nextToken());
            }
            else {
                throw new RuntimeException("Parsing error with: "+ s);
            }
            poiTypes.add(poiType);
        }
        this.startHour = startHour;
        this.endHour = endHour;
        this.minCount = minCount;
        this.maxCount = maxCount;
    }
    public String toString() {
        StringBuffer strbuf = new StringBuffer();
        strbuf.append("constraint types="+poiTypes.toString()+"\t");
        strbuf.append("time ["+String.format("%3.3f",startHour)+ "--"+String.format("%3.3f",endHour)+"]\t");
        strbuf.append("count =["+minCount+","+maxCount+"]");

        return strbuf.toString();
    }

    public Set<POIType> getPOIType(){
        return poiTypes;
    }
    public int getMaxCount(){
        return maxCount;
    }
    public int getMinCount(){
        return minCount;
    }
    public double getStartHour(){
        return startHour;
    }
    public double getEndHour(){
        return endHour;
    }

    public static void test() {
        CategoryConstraint categoryConstraint = new CategoryConstraint(9.00, 18.00, 1, 1, "음식점.한식");
        logger.debug(categoryConstraint);
        logger.debug(CategoryConstraintFactory.createLunchConstraint());
        logger.debug(CategoryConstraintFactory.createDinnerConstraint());
    }

    public static void main(String[] args) {
        test();
    }
}
