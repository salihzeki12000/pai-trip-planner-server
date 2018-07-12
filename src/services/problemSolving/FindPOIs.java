package services.problemSolving;


import org.apache.log4j.Logger;
import services.dictionary.InterfaceDictionary;
import services.dummy.ServiceManager;
import services.qna.ScheduleType;

import java.util.HashSet;
import java.util.Set;

/**
 * 경우에 따라서 POI를 찾는다.
 */
public class FindPOIs {
    private static Logger logger = Logger.getLogger(FindPOIs.class);

    Set<String> personNames;
    ScheduleType scheduleType;

    public FindPOIs( Set<String> personNames, ScheduleType scheduleType) {
        this.personNames = personNames;
        this.scheduleType = scheduleType;
    }

// POI 후보군을 반환한다.
    public Set<String> getPOIs(){
        if (scheduleType==ScheduleType.SingleEventMeeting){
            return getMeetingPOIs();
        }
        else if (scheduleType==ScheduleType.SingleEventEat){
            return getRestaurantPOIs();
        }
        else if (scheduleType==ScheduleType.Travel)  {
            return getAttractionPOIs();
        }
        return null;
    }


    private Set<String> getMeetingPOIs(){
        Set<String> poiNameSet= new HashSet<String>();
        for (String personName: personNames ){
            String ret[] = searchPersonalDictionary(personName,"직장");
//            logger.debug(Arrays.toString(ret));
            if (ret.length==1) {
                poiNameSet.add(ret[0]);
            }
        }
        return poiNameSet;
    }
    private Set<String> getRestaurantPOIs(){
        return null;
    }


    private Set<String> getAttractionPOIs(){
        return null;
    }
    // 사전에서 탐색
    private String[] searchPersonalDictionary(String userName, String query) {
        String headWord = userThesaurus(userName).search(query)[0];
//        logger.debug("표제어: " + headWord + " from " + query_handling);
        return userDictionary(userName).search(headWord);
    }

    private InterfaceDictionary userDictionary(String userName) {
        return ServiceManager.getInstance().getDictionaryService().getDictionary(userName + "사전");
    }

    private InterfaceDictionary userThesaurus(String userName) {
        return ServiceManager.getInstance().getDictionaryService().getDictionary(userName + "유의어");
    }

}
