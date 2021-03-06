package tripPlanning.optimize.constraints.categoryConstraint;

import tripPlanning.tripData.dataType.PoiType;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

public class CategoryConstraint {
    private Set<PoiType> poiTypes;
    private double startHour;
    private double endHour;
    private int minCount;
    private int maxCount;

    public CategoryConstraint() {
    }

    public CategoryConstraint(double startHour, double endHour, int minCount, int maxCount, String... categoryTypeStrs) {
        poiTypes = new HashSet<>();
        for (String s : categoryTypeStrs) {
            StringTokenizer stringTokenizer = new StringTokenizer(s, ".");
            int countToken = stringTokenizer.countTokens();

            PoiType poiType;
            if (countToken == 1) {
                poiType = new PoiType(stringTokenizer.nextToken());
            } else if (countToken == 2) {
                poiType = new PoiType(stringTokenizer.nextToken(), stringTokenizer.nextToken());
            } else if (countToken == 3) {
                poiType = new PoiType(stringTokenizer.nextToken(), stringTokenizer.nextToken(), stringTokenizer.nextToken());
            } else {
                throw new RuntimeException("Parsing error with: " + s);
            }
            poiTypes.add(poiType);
        }
        this.startHour = startHour;
        this.endHour = endHour;
        this.minCount = minCount;
        this.maxCount = maxCount;
    }

    public Set<PoiType> getPoiType() {
        return poiTypes;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public int getMinCount() {
        return minCount;
    }

    public double getStartHour() {
        return startHour;
    }

    public double getEndHour() {
        return endHour;
    }

    @Override
    public String toString() {
        StringBuffer strbuf = new StringBuffer();
        strbuf.append("constraint types=" + poiTypes.toString() + "\t");
        strbuf.append("time [" + String.format("%3.3f", startHour) + "--" + String.format("%3.3f", endHour) + "]\t");
        strbuf.append("count =[" + minCount + "," + maxCount + "]");

        return strbuf.toString();
    }
}
