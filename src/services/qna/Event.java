package services.qna;


import edu.hanyang.trip_planning.tripData.dataType.ActivityType;
import edu.hanyang.trip_planning.tripData.dataType.ProbabilisticDuration;

/**
 * Event
 */
public class Event {

    /**
     * location: name of InterfacePOI
     *
     * @return
     */
    public String where;

    /**
     * start time
     */
    public String startTime;


    /**
     * duration
     */
    public ProbabilisticDuration duration;

    /**
     * type of event, or activity
     */
    public ActivityType activity;

    public Event(String where, String startTime, ProbabilisticDuration duration, ActivityType activity) {
        this.where = where;
        this.startTime = startTime;
        this.duration = duration;
        this.activity = activity;
    }

    public String toString() {
        StringBuffer strbuf = new StringBuffer();
        strbuf.append(where + "에서 ");
        strbuf.append(startTime + "에 시작해서 ");
        strbuf.append(duration + "동안 ");
        strbuf.append(activity + "를 한다.");

        return strbuf.toString();
    }

    public static Event dummy() {
        return new Event("한양대", "2015-06-04 14:00", new ProbabilisticDuration(180, 30), ActivityType.Meeting);
    }

    public static void test() {
        System.out.println("dummy() = " + dummy());
    }

    public static void main(String[] args) {
        test();
    }
}

