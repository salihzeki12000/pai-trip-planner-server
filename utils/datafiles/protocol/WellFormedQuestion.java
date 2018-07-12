package edu.hanyang.protocol;


import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 15. 6. 2
 * Time: 오전 11:04
 * To change this template use File | Settings | File Templates.
 */
public class WellFormedQuestion {


    /**
     * 필수항목들
     */
    private Set<String> persons = new HashSet<String>();
    private List<TemporalConstraint> temporalConstraints = new ArrayList<TemporalConstraint>();
    private SpatialConstraints spatialConstraints = null;
    private ScheduleType objective = null;

    /**
     * 경우에 따라 필수항목이거나 선택항목인 것
     * - 여행의 경우에는 기간이 필수고, 식사의 경우는 식사의 종류에 따라 결정되고, 회의는 시간이 결정됨
     * - 클래스 내에서 알아서 처리할 것
     */
    private Duration duration = null;

    /**
     * 추가항목들
     */
    private Integer budgetLimit; // 비용상한
    private Map<String, String> keywords = new HashMap<String, String>();   // 키워드들
    private Location startLocation = null; // 시작 장소
    private Location endLocation = null;   // 종료 장소

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

    public void setSpatialConstraints(SpatialConstraints spatialConstraints) {
        this.spatialConstraints = spatialConstraints;
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

    public void setStartLocation(Location startLocation) {
        this.startLocation = startLocation;
    }

    public void setEndLocation(Location endLocation) {
        this.endLocation = endLocation;
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
        if (objective == null) {
            return false;
        }


        return true;

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

    public void addKeyword(String key, String value) {
        keywords.put(key, value);
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
        if (keywords.entrySet().size() > 0) {
            strBuf.append("그리고, ");
        }
        for (Map.Entry<String, String> entry : keywords.entrySet()) {
            strBuf.append(entry.getKey() + "=" + entry.getValue() + ", ");
        }
        if (keywords.entrySet().size() > 0) {
            strBuf.deleteCharAt(strBuf.length() - 1);
            strBuf.deleteCharAt(strBuf.length() - 1);
            strBuf.append("(들)을 고려했으면 좋겠어.");
        }


        return strBuf.toString();
    }


    public static void testAnswer() {
        String questionstr = "이번 겨울에 부모님을 모시고 일본으로 온천 여행을 다녀오려고 하는데 괜찮은 계획 좀 부탁해";
        WellFormedQuestion question = new WellFormedQuestion();
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


    }

    public static void main(String[] args) {

    }
}
