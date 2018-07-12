package services.schedule;

import org.joda.time.DateTime;
import wykwon.common.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by wykwon on 2015-11-12.
 */
public class FreeTimeSelector {
    List<Pair<DateTime, DateTime>> freeTimeList;
    FreeTimeSortMethod sortMethod = FreeTimeSortMethod.Time;

    public FreeTimeSelector(List<Pair<DateTime, DateTime>> freeTimeList) {
        this.freeTimeList = freeTimeList;
    }

    public void setSortMethod(FreeTimeSortMethod freeTimeSortMethod) {
        this.sortMethod = freeTimeSortMethod;
    }

    public List<Pair<DateTime, DateTime>> all() {
        List<Pair<DateTime, DateTime>> retList = new ArrayList<Pair<DateTime, DateTime>>();
        TreeSet<OrderedFreeTime> orderedFreeTimeTreeSet = new TreeSet<OrderedFreeTime>();
        for (Pair<DateTime, DateTime> pair: freeTimeList){
            orderedFreeTimeTreeSet.add(new OrderedFreeTime(pair.first(),pair.second(), sortMethod));
        }

        for (OrderedFreeTime orderedFreeTime: orderedFreeTimeTreeSet ){
            retList.add(new Pair<DateTime,DateTime>(orderedFreeTime.startTime,orderedFreeTime.endTime));
        }
        return retList;
    }
    public List<Pair<DateTime, DateTime>> topN(int size) {
        List<Pair<DateTime, DateTime>> retList = new ArrayList<Pair<DateTime, DateTime>>();
        TreeSet<OrderedFreeTime> orderedFreeTimeTreeSet = new TreeSet<OrderedFreeTime>();
        for (Pair<DateTime, DateTime> pair: freeTimeList){
            orderedFreeTimeTreeSet.add(new OrderedFreeTime(pair.first(),pair.second(), sortMethod));
        }

        int i=0;
        for (OrderedFreeTime orderedFreeTime: orderedFreeTimeTreeSet ){
            retList.add(new Pair<DateTime,DateTime>(orderedFreeTime.startTime,orderedFreeTime.endTime));
            i++;
            if (i>=size){
                break;
            }
        }
        return retList;
    }



    public static void main(String[] args) {
        TreeSet<OrderedFreeTime> orderedFreeTimeTreeSet = new TreeSet<OrderedFreeTime>();
        orderedFreeTimeTreeSet.add(new OrderedFreeTime("2015-11-12 08:00", "2015-11-12 12:00", FreeTimeSortMethod.Time));
        orderedFreeTimeTreeSet.add(new OrderedFreeTime("2015-11-12 09:00", "2015-11-12 12:00", FreeTimeSortMethod.Time));
        orderedFreeTimeTreeSet.add(new OrderedFreeTime("2015-11-12 10:00", "2015-11-12 12:00", FreeTimeSortMethod.Time));
        System.out.println("orderedFreeTimeTreeSet = " + orderedFreeTimeTreeSet);
    }
}
