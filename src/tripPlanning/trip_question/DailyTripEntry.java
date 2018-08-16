package tripPlanning.trip_question;

import tripPlanning.optimize.constraints.categoryConstraint.CategoryConstraint;
import tripPlanning.optimize.constraints.poiConstraint.PoiConstraint;
import util.TimeStrHelper;

import java.util.ArrayList;
import java.util.List;

// 하루 일정에 대한 질문 구조체
public class DailyTripEntry {
    private String startTimeStr;        // 여정 시작시간
    private String returnTimeStr;       // 여정 종료시간
    private String startPoiTitle;       // 여정 시작장소
    private String endPoiTitle;         // 여정 종료장소
    private String[] areas;               // 여정 지역
    private Double physicalActivityLimit; // 활동량 상한
    private List<CategoryConstraint> categoryConstraintList = new ArrayList<>();
    private List<PoiConstraint> poiConstraintList = new ArrayList<>();

    public DailyTripEntry(String startTimeStr, String returnTimeStr, String[] areas, String startPoiTitle, String endPoiTitle, double physicalActivityLimit) {
        this.startTimeStr = startTimeStr;
        this.returnTimeStr = returnTimeStr;
        this.startPoiTitle = startPoiTitle;
        this.endPoiTitle = endPoiTitle;
        this.areas = areas;
        this.physicalActivityLimit = physicalActivityLimit;
    }

    public void addCategoryConstraint(CategoryConstraint categoryConstraint) {
        categoryConstraintList.add(categoryConstraint);
    }

    public void addPoiConstraint(PoiConstraint poiConstraint) {
        poiConstraintList.add(poiConstraint);
    }

    public int[] getStartTime() {
        return TimeStrHelper.fullDateStr2IntArray(startTimeStr);
    }

    public int[] getReturnTime() {
        return TimeStrHelper.fullDateStr2IntArray(returnTimeStr);
    }

    public String getStartPoiTitle() {
        return startPoiTitle;
    }

    public String getEndPoiTitle() {
        return endPoiTitle;
    }

    public String[] getAreas() {
        return areas;
    }

    public List<CategoryConstraint> getCategoryConstraintList() {
        return categoryConstraintList;
    }

    public List<PoiConstraint> getPoiConstraintList() {
        return poiConstraintList;
    }

    @Override
    public String toString() {
        StringBuffer strbuf = new StringBuffer();
        strbuf.append("daily itinerary [" + startTimeStr + "--" + returnTimeStr + "]\n");
        strbuf.append("\tareas=" + areas + " from " + startPoiTitle + " to " + endPoiTitle + "\n");
        strbuf.append("\tphysical activity limit=" + physicalActivityLimit + "\n");
        for (CategoryConstraint categoryConstraint : categoryConstraintList) {
            strbuf.append("\t" + categoryConstraint + "\n");
        }
        return strbuf.toString();
    }
}
