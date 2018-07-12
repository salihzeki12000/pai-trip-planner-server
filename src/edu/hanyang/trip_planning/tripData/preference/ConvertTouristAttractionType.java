package edu.hanyang.trip_planning.tripData.preference;

import au.com.bytecode.opencsv.CSVReader;
import edu.hanyang.trip_planning.tripData.dataType.POIType;
import edu.hanyang.trip_planning.tripData.poi.BasicPOI;
import edu.hanyang.trip_planning.tripData.poi.POIManager;
import org.apache.log4j.Logger;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wykwon on 2015-10-21.
 */
public class ConvertTouristAttractionType {
    private static Logger logger = Logger.getLogger(ConvertTouristAttractionType.class);
    Map<POIType, TouristAttractionType> attractionTypeMap = new HashMap<POIType, TouristAttractionType>();

    public ConvertTouristAttractionType(){
        try {
            readConf("datafiles/pois/AttractionTypeConvert.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void readConf(String filename) throws IOException {

//logger.debug("읽어");
        CSVReader csvReader = new CSVReader(new FileReader(filename), '\t');
        List<String[]> strArrayList = csvReader.readAll();
        for (String[] strArray : strArrayList) {
            if (strArray.length == 0) {
                continue;
            }
            else if (strArray[0].length()==0) {
                continue;
            }
            else if (strArray[0].charAt(0) == '#') {
                continue;
            }
            POIType poiType = new POIType(strArray[0], strArray[1], strArray[2]);
            TouristAttractionType attractionType = TouristAttractionType.valueOf(strArray[3]);
            attractionTypeMap.put(poiType,attractionType);
        }
    }

    public TouristAttractionType getAttractionType(POIType poiType){
        TouristAttractionType attractionType = attractionTypeMap.get(poiType);
        if (attractionType==null){
            throw new RuntimeException("not defined such as " + poiType);

        }
        return attractionType;
    }
    public TouristAttractionType getAttractionType(BasicPOI basicPOI){
        TouristAttractionType attractionType = attractionTypeMap.get(basicPOI.getPoiType());
        if (attractionType==null){
            logger.fatal("not defined such as " + basicPOI.getPoiType()+ " in " + basicPOI.getTitle());
          throw new RuntimeException("not defined such as " + basicPOI.getPoiType()+ " in " + basicPOI.getTitle());
        }
        return attractionType;
    }
    public String toString(){
        return attractionTypeMap.toString();
    }

    public static void main(String[] args) {
        ConvertTouristAttractionType convertTouristAttractionType = new ConvertTouristAttractionType();
        System.out.println("convertTouristAttractionType = " + convertTouristAttractionType);

        for (BasicPOI poi : POIManager.getInstance().getAll()){
            POIType poiType = poi.getPoiType();
            if (poiType.category.equals("여행") && poiType.subCategory.equals("관광,명소") ){
                System.out.println(convertTouristAttractionType.getAttractionType(poi));
            }
        }

}
}
