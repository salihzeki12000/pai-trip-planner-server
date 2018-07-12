package services.problemSolving;

import edu.hanyang.trip_planning.tripData.dataType.AddressCode;
import edu.hanyang.trip_planning.tripData.dataType.Duration;
import org.apache.log4j.Logger;
import services.qna.ScheduleType;
import services.qna.SpatialConstraints;
import services.qna.SpatialStatementByLocation;
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
public class BasicWellFormedQuestion {
    private static Logger logger = Logger.getLogger(BasicWellFormedQuestion.class);

    /**
     * 필수항목들
     */
    private String questioner;
    private Set<String> persons = new HashSet<String>();
    private List<TemporalConstraint> temporalConstraints = new ArrayList<TemporalConstraint>();
    private SpatialConstraints spatialConstraints = null;
    private ScheduleType objective = null;

    /**
     * 경우에 따라 필수항목이거나 선택항목인 것
     * - 여행의 경우에는 기간이 필수고, 식사의 경우는 식사의 종류에 따라 결정되고, 회의는 시간이 결정됨
     * - 클래스 내에서 알아서 처리할 것
     */

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


    //    private Map<String, String> keywords = new HashMap<String, String>();   // 키워드들

    public void setQuestioner(String questioner){
        this.questioner= questioner;
    }

    public String getQuestioner(){
        return questioner;
    }
    public void setPersons(Set<String> persons) {
        this.persons = persons;
    }

    public void addPerson(String name) {
        this.persons.add(name);
    }


    public void setSrcDestPOIsName(String startPOIName, String endPOIName) {
        this.startPOIName = startPOIName;
        this.endPOIName = endPOIName;
    }


    public void addTemporalConstraints(TemporalConstraint temporalConstraint) {
        this.temporalConstraints.add(temporalConstraint);
    }


    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setSpatialConstraints(SpatialConstraints spatialConstraints) {
        this.spatialConstraints = spatialConstraints;
    }

    public void addSpatialConstraint(AddressCode addressCode) {
        if (spatialConstraints == null) {
            spatialConstraints = new SpatialConstraints();
        }
        spatialConstraints.addSymbolicSpatialConstraints(addressCode);
    }

    public void addSpatialConstraint(SpatialStatementByLocation spatialStatementByLocation) {
        if (spatialConstraints == null) {
            spatialConstraints = new SpatialConstraints();
        }
        spatialConstraints.addMetricSpatialConstraints(spatialStatementByLocation);
    }

    public void setObjective(ScheduleType objective) {
        this.objective = objective;
    }


    public Set<String> persons() {
        return persons;
    }


    public List<TemporalConstraint> temporalConstraints() {
        return temporalConstraints;
    }


    public Duration duration() {
        return duration;
    }


    public SpatialConstraints spatialConstraints() {
        return spatialConstraints;
    }


    public ScheduleType objective() {
        return objective;
    }

    public void setBudgetLimit(int costLimit) {
        this.budgetLimit = costLimit;
    }


    public String getStartPOIName() {
        return startPOIName;
    }

    public String getEndPOIName() {
        return endPOIName;
    }

    /**
     * 필수 항목이 모두 있는지 여부를 확인
     *
     * @return
     */
    public boolean checkAllRequiredItems() {
        if (objective == ScheduleType.SingleEventEat && duration == null) {
            duration = new Duration(null, 2);
        }
        if (duration == null) {
            return false;
        }

        // 사람
        if (persons.size() == 0) {
            return false;
        }
        if (temporalConstraints.size() == 0) {
            return false;
        }
        if (spatialConstraints == null) {
            return false;
        }
        return objective != null;

    }

    /**
     * 재질의 문장을 확인한다.
     *
     * @return
     */
    public String getReturnQuestion() {

        checkAllRequiredItems();

        StringBuffer strBuf = new StringBuffer();
        // 사람
        if (persons.size() == 0) {
            strBuf.append("누구와 가시나요? \n");

        }
        if (temporalConstraints.size() == 0) {
            strBuf.append("언제 가시나요? \n");

        }
        if (duration == null) {
            strBuf.append("얼마동안 가시나요? \n");
        }
        if (spatialConstraints == null) {
            strBuf.append("어디로 가시나요? \n");
        }
        if (objective == null) {
            strBuf.append("어떤걸 하시나요? (여행, 출장, 식사, 회의, ..) \n");
        }

        if (strBuf.toString().length() > 0) {
            return strBuf.toString();
        } else {
            return "없음";
        }
    }


    public String toString() {
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("누가: " + persons + '\n');

        strBuf.append("어디서: " + spatialConstraints + '\n');

        strBuf.append("언제: ");

        for (TemporalConstraint temporalConstraint : temporalConstraints) {
            strBuf.append(temporalConstraint + ",\t");
        }
        strBuf.append('\n');


        strBuf.append("얼마동안: " + duration + '\n');

        strBuf.append("무엇을: " + objective + "을\n");

        strBuf.append("하려고 하는데 어떻게 하면 좋을까?");

        if (budgetLimit != null) {
            strBuf.append("비용은 " + budgetLimit + "원 을 넘지 않았으면 좋겠어\n");
        }


        return strBuf.toString();
    }


    public static void testAnswer() {
        String questionstr = "이번 겨울에 부모님을 모시고 일본으로 온천 여행을 다녀오려고 하는데 괜찮은 계획 좀 부탁해";
        BasicWellFormedQuestion question = new BasicWellFormedQuestion();
        question.addPerson("나");
        question.addPerson("부모님");
        question.addTemporalConstraints(new TemporalConstraint("2015-06-05", "2015-06-30"));
        question.addTemporalConstraints(new TemporalConstraint("2015-09-01", "2015-09-30"));


//        InterfaceTemporalConstraints temporalConstraints = new InterfaceTemporalConstraints();
//
//                new TemporalConstraint("2015-06-05", "2015-06-30"),
//                new TemporalConstraint("2015-09-01", "2015-09-30"));
        question.addTemporalConstraints(new TemporalConstraint("2015-06-05", "2015-06-30"));
        question.addTemporalConstraints(new TemporalConstraint("2015-09-01", "2015-09-30"));
        question.setObjective(ScheduleType.OnedayTravel);
        question.setDuration(new Duration(1, 0));
//        InterfaceSpatialConstraints spatialConstraints = new InterfaceSpatialConstraints();
//        spatialConstraints.
        question.setSpatialConstraints(SpatialConstraints.dummy1());
        logger.info(question);

    }

    public static void main(String[] args) {

    }
}
