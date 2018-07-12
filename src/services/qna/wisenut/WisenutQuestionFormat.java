package services.qna.wisenut;

import com.google.gson.Gson;

import edu.hanyang.trip_planning.tripData.dataType.Duration;
import org.apache.log4j.Logger;
import services.qna.TemporalConstraint;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 15. 6. 2
 * Time: 오전 11:04
 * To change this template use File | Settings | File Templates.
 */
public class WisenutQuestionFormat {
    private static Logger logger = Logger.getLogger(WisenutQuestionFormat.class);

    /**
     * 필수항목들
     */
    public String sessionId;
    public Set<String> persons = new HashSet<String>();
    public List<TemporalConstraint> temporalConstraints = new ArrayList<TemporalConstraint>();
    public SpatialConstraints spatialConstraints=new SpatialConstraints();
//    public Set<String> spatialConstraints = new HashSet<String>();
    public String objective;




    /**
     * 선택 항목들
     */
    public Duration duration = null;
    public Integer budgetLimit = null; // 비용상한
    public Integer fatugueLimit = null; // 피로도 상한
    public String startPOIName = null; // 여행시작 장소
    public String endPOIName = null;   // 여행종료 장소
    public String travelStartTime = null; // 여행시작시간
    public String travelEndTime = null;   // 여행종료시간

    public String verb=null;
    public Object others=null;
    public String keywords = null;


    public void setSessionId(String id){
        this.sessionId = id;
    }

    public void setPersons(Set<String> persons) {
        this.persons = persons;
    }

    public void addPerson(String name) {
        this.persons.add(name);
    }


    public void addTemporalConstraints(TemporalConstraint temporalConstraint) {
        this.temporalConstraints.add(temporalConstraint);
    }


    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void addSpatialConstraint(String spatialConstraint) {
        this.spatialConstraints.symbolicSpatialConstraints.add(spatialConstraint);
    }

    public Set<String> getSpatialConstraint(){
        return this.spatialConstraints.symbolicSpatialConstraints;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }


    public Set<String> persons() {
        return persons;
    }


    public List<TemporalConstraint> temporalConstraints() {
        return temporalConstraints;
    }

    public void setDuration(int day, int hour){
        this.duration = new Duration(day,hour);
    }

    public Duration duration() {
        return duration;
    }

    public Set<String> spatialConstraints() {
        return spatialConstraints.symbolicSpatialConstraints;
    }

    public String objective() {
        return objective;
    }

    public void setBudgetLimit(int costLimit) {
        this.budgetLimit = costLimit;
    }


    /**
     * 필수 항목이 모두 있는지 여부를 확인
     *
     * @return
     */
    public String toString() {
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("Sesson_id: "+ sessionId + "\n");

        strBuf.append("누가: " + persons + '\n');

        strBuf.append("어디서: " + spatialConstraints.symbolicSpatialConstraints + '\n');

        strBuf.append("언제: ");

        for (TemporalConstraint temporalConstraint : temporalConstraints) {
            strBuf.append(temporalConstraint + ",\t");
        }
        strBuf.append('\n');


        strBuf.append("얼마동안: " + duration + '\n');

        strBuf.append("무엇을: " + objective + "을\n");

        strBuf.append("하려고 하는데 어떻게 하면 좋을까?");


        strBuf.append("기타항목들\n");

        if (duration!=null)
        strBuf.append("기간 : "+ duration+ '\n');
        if (budgetLimit!=null)
        strBuf.append("비용상한 : "+ budgetLimit + "원\n");
        if (fatugueLimit!=null)
        strBuf.append("피로도상한 : "+ fatugueLimit + "kCal\n");
        if (startPOIName!=null)
        strBuf.append("여행 출발장소 : "+ startPOIName + "\n");
        if (endPOIName!=null)
        strBuf.append("여행 종료장소 : "+ endPOIName + "\n");
        if (travelStartTime!=null)
        strBuf.append("여행 시작시간 : "+ travelStartTime + "\n");
        if (travelEndTime!=null)
        strBuf.append("여행 종료시간 : "+ travelEndTime + "\n");



        return strBuf.toString();
    }



    public static void main(String[] args) {
        Gson gson = new Gson();
        String str = "{ \"sessionId\":\"01045598193\", \"persons\":[ \"엑소브레인멤버들\" ], \"temporalConstraints\":[ { \"symbolicTime\":\"오늘 저녁\", \"afterTime\":\"2016-01-22 18:00\", \"beforeTime\":\"2016-01-22 24:00\" } ], \"spatialConstraints\":{ \"symbolicSpatialConstraints\":[ \"숙소 근처\" ], \"metricSpatialConstraints\":[] }, \"objective\":\"meal\", \"verb\":\"\", \"duration\":{ \"day\":\"0\", \"hour\":\"3\" }, \"keywords\":\"\", \"others\":{} }";
        WisenutQuestionFormat w=  gson.fromJson(str,WisenutQuestionFormat.class);
        logger.debug(w);
    }
}

