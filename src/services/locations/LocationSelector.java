package services.locations;


import edu.hanyang.trip_planning.tripData.poi.BasicPOI;
import edu.hanyang.trip_planning.tripData.poi.POIManager;
import org.apache.log4j.Logger;
import services.dictionary.InterfaceDictionary;
import services.dummy.ServiceManager;

import java.util.*;

/**
 * Created by wykwon on 2015-11-12.
 */
public class LocationSelector {
    Set<BasicPOI> poiCandidates;
    Set<String> personNames;
    LocationSortMethod sortMethod = LocationSortMethod.Distance;
    private static Logger logger = Logger.getLogger(LocationSelector.class);

    public LocationSelector(Set<BasicPOI> poiCandidates, Set<String> personNames) {
        this.personNames = personNames;
        this.poiCandidates = poiCandidates;
    }

    public void setSortMethod(LocationSortMethod sortMethod) {
        this.sortMethod = sortMethod;
    }

    public List<String> topN(int size) {
        List<String> retList = new ArrayList<String>();
        TreeSet<OrderedLocation> orderedLocations = null;


        if (sortMethod==LocationSortMethod.Distance){
            orderedLocations = sortBySumOfDistance();
        }
        else if (sortMethod==LocationSortMethod.Score){
            orderedLocations =sortByScore();
        }

//
//        for (Pair<DateTime, DateTime> pair : freeTimeList) {
//            orderedFreeTimeTreeSet.add(new OrderedFreeTime(pair.first(), pair.second(), sortMethod));
//        }

        int i = 0;
        for (OrderedLocation orderedLocation : orderedLocations) {
            retList.add(orderedLocation.poi.getTitle());
            i++;
            if (i >= size) {
                break;
            }
        }
        return retList;
    }

    /**
     * 집하고 직장에서 가까운데로

     * @return
     */
    private TreeSet<OrderedLocation> sortBySumOfDistance(){
        TreeSet<OrderedLocation> orderedLocationTreeSet = new TreeSet<OrderedLocation>();
        Set<BasicPOI> referencePOIs = new HashSet<BasicPOI>();

        // 각자의 직장과 집을 찾아보자.
        for (String personName: personNames){
            String office[] = userDictionary(personName).search("직장");
            referencePOIs.add(getPOI(office[0]));
            String home[] = userDictionary(personName).search("집");
            referencePOIs.add(getPOI(home[0]));
        }

        // 후보지역과 모두의 거리를 비교함
        for(BasicPOI candidatePOI: poiCandidates){
            double dist = sumOfDistance(candidatePOI,referencePOIs);
            OrderedLocation orderedLocation = new OrderedLocation(candidatePOI,dist);
            orderedLocationTreeSet.add(orderedLocation);
        }
//        logger.debug(orderedLocationTreeSet);
        return orderedLocationTreeSet;
    }

    /**
     * 집하고 직장에서 가까운데로

     * @return
     */
    private TreeSet<OrderedLocation> sortByScore(){
        TreeSet<OrderedLocation> orderedLocationTreeSet = new TreeSet<OrderedLocation>();
        // 후보지역과 모두의 거리를 비교함
        for(BasicPOI candidatePOI: poiCandidates){
            double score = candidatePOI.getScore();
            OrderedLocation orderedLocation = new OrderedLocation(candidatePOI,score);
            orderedLocationTreeSet.add(orderedLocation);
        }
        return orderedLocationTreeSet;
    }
    private BasicPOI getPOI(String poiTitle) {
        InterfaceDictionary dic = ServiceManager.getInstance().getDictionaryService().getDictionary("공통장소유의어");
        String ret[] = dic.search(poiTitle);
//        logger.debug(Arrays.toString(ret));

        String poiHeadTitle = ret[0];
        if (poiHeadTitle==null){
            return POIManager.getInstance().getPOIByTitle(poiTitle);
        }
        else {
            return POIManager.getInstance().getPOIByTitle(poiHeadTitle);
        }
    }


    private double sumOfDistance(BasicPOI targetPOI, Set<BasicPOI> referencePOIs) {
        double sum = 0.0;
        for (BasicPOI poi : referencePOIs) {
            sum+=POIManager.getInstance().distance(targetPOI,poi);
        }


        return sum;
    }

    // 사전에서 탐색
    private String[] searchPersonalDictionary(String userName, String query) {
        String headWord = userThesaurus(userName).search(query)[0];
        logger.debug("표제어: " + headWord + " from " + query);
        return userDictionary(userName).search(headWord);

    }

    private InterfaceDictionary userDictionary(String userName) {
        return ServiceManager.getInstance().getDictionaryService().getDictionary(userName + "사전");
    }

    private InterfaceDictionary userThesaurus(String userName) {
        return ServiceManager.getInstance().getDictionaryService().getDictionary(userName + "유의어");
    }

    public static void main(String[] args) {

        //직장을 알아보자고

        Set<String> poiCandidatesTitle = new HashSet<String>();
        poiCandidatesTitle.add("ETRI");
        poiCandidatesTitle.add("한양대학교");
        poiCandidatesTitle.add("건국대학교");
        poiCandidatesTitle.add("와이즈넛");
        Set<String> personNames = new HashSet<String>();
        personNames.add("권우영");
        personNames.add("이창은");
        personNames.add("하영국");
        personNames.add("안영민");


        Set<BasicPOI> candidatePOIs = new HashSet<BasicPOI>();
        for (String poiTitle: poiCandidatesTitle){
            candidatePOIs.add(POIManager.getInstance().getPOIByTitle(poiTitle));
        }
        LocationSelector locationSelector = new LocationSelector(candidatePOIs,personNames);
        locationSelector.topN(5);
    }
}

