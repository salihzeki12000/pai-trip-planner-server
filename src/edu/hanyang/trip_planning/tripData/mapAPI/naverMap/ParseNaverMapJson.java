package edu.hanyang.trip_planning.tripData.mapAPI.naverMap;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.TreeSet;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 14. 12. 24
 * Time: 오후 4:43
 * To change this template use File | Settings | File Templates.
 */
public class ParseNaverMapJson {

    private static Logger logger = Logger.getLogger(ParseNaverMapJson.class);
    Gson gson = new Gson();
    JsonElement rootElement;

    public ParseNaverMapJson(JsonElement rootElement) {
        this.rootElement = rootElement;
    }

    public TypePath checkType() {
        JsonElement jsonElement = rootElement.getAsJsonObject().get("result");
        if (jsonElement == null) {
            JsonElement errorElement = rootElement.getAsJsonObject().get("error");
            if (errorElement == null) {
                throw new RuntimeException("이건 또 뭔데.. " + errorElement);

            } else {
                JsonElement errorCodeElement = errorElement.getAsJsonObject().get("code");
                if (errorCodeElement == null) {
                    throw new RuntimeException("이건 또 뭔데.. " + errorCodeElement);
                }
                int errorcode = errorCodeElement.getAsInt();
                if (errorcode == -98) {
                    return TypePath.TOONEAR;
                }
            }
        }
        if (jsonElement == null) {
            throw new RuntimeException(rootElement.toString());
        }
        JsonElement pathElementList = jsonElement.getAsJsonObject().get("path");
        if (pathElementList == null) {
//            logger.info("시외구간");
            return TypePath.INTERCITY;
        } else {
//            logger.info("시내구간");
            return TypePath.INTRACITY;
        }
    }

    public void parse() {
        JsonObject resultObject = rootElement.getAsJsonObject().get("result").getAsJsonObject();
        JsonElement pathElementList = resultObject.get("path");
        if (pathElementList == null) {
            logger.info("시외구간임");
            parseInterCity();
        } else {
            parseIntraCity();
        }
    }


    public TreeSet<PathElement> parseIntraCity() {
        TreeSet<PathElement> pathElementList = new TreeSet<PathElement>();
        JsonElement resultElement = rootElement.getAsJsonObject().get("result");
        if (resultElement == null) {
            return pathElementList;
        }
        JsonElement jsonPathElementList = resultElement.getAsJsonObject().get("path");
//        List<Info> infoList = new ArrayList<Info>();
        JsonArray paths = jsonPathElementList.getAsJsonArray();
        for (int i = 0; i < paths.size(); i++) {
            Info info = parsePathInfo(paths.get(i));
//            logger.debug(info);
            PathElement pathElement = new PathElement();
            pathElement.setDuration((int) ((double) info.getTotalTime() * 0.97));
            pathElement.setCost(info.getPayment());
            pathElementList.add(pathElement);
//                subPathList.add(parseSubPathList(pathElement));
        }
        return pathElementList;
    }

    public TreeSet<PathElement> parseInterCity() {
        TreeSet<PathElement> pathElementList = new TreeSet<PathElement>();
        // 철도
        JsonObject resultObject = rootElement.getAsJsonObject().get("result").getAsJsonObject();
        JsonElement trainElement = resultObject.get("trainRequest");
//        logger.debug(trainElement.getAsJsonObject());
        int traintCount = trainElement.getAsJsonObject().get("count").getAsInt();
//        logger.debug(traintCount);
        if (traintCount > 0) {
            JsonElement objElements = trainElement.getAsJsonObject().get("OBJ");
            JsonArray objectArray = objElements.getAsJsonArray();

            for (int i = 0; i < objectArray.size(); i++) {
                TrainObject trainObject = gson.fromJson(objectArray.get(i), TrainObject.class);
                PathElement pathElement = new PathElement();
                pathElement.setSrcCoordinate(trainObject.getSX(), trainObject.getSY());
                pathElement.setDestCoordinate(trainObject.getEX(), trainObject.getEY());
                pathElement.setDuration((int) ((double) trainObject.getTime() * 0.94));
                pathElement.setCost(trainObject.getPayment());
                pathElement.setWay(trainObject.getTrainType());
                pathElementList.add(pathElement);
            }
        }


        //고속버스
        JsonElement exBusElement = resultObject.get("exBusRequest");
//        logger.debug(exBusElement);
        int exBusCount = exBusElement.getAsJsonObject().get("count").getAsInt();
//        logger.debug(exBusCount);
        if (exBusCount > 0) {
            JsonElement objElements = exBusElement.getAsJsonObject().get("OBJ");
            JsonArray objectArray = objElements.getAsJsonArray();

            for (int i = 0; i < objectArray.size(); i++) {
                TrainObject trainObject = gson.fromJson(objectArray.get(i), TrainObject.class);
                PathElement pathElement = new PathElement();
                pathElement.setSrcCoordinate(trainObject.getSX(), trainObject.getSY());
                pathElement.setDestCoordinate(trainObject.getEX(), trainObject.getEY());
                pathElement.setDuration(trainObject.getTime());
                pathElement.setCost(trainObject.getPayment());
                pathElement.setWay("exBus");
                pathElementList.add(pathElement);
            }
        }
        //시외버스
        JsonElement outBusElement = resultObject.get("outBusRequest");
//        logger.debug(exBusElement);
        int outBusCount = outBusElement.getAsJsonObject().get("count").getAsInt();
//        logger.debug(exBusCount);
        if (outBusCount > 0) {
            JsonElement objElements = outBusElement.getAsJsonObject().get("OBJ");
            JsonArray objectArray = objElements.getAsJsonArray();

            for (int i = 0; i < objectArray.size(); i++) {
                TrainObject trainObject = gson.fromJson(objectArray.get(i), TrainObject.class);
                PathElement pathElement = new PathElement();
                pathElement.setSrcCoordinate(trainObject.getSX(), trainObject.getSY());
                pathElement.setDestCoordinate(trainObject.getEX(), trainObject.getEY());
                pathElement.setDuration(trainObject.getTime());
                pathElement.setCost(trainObject.getPayment());
                pathElement.setWay("outBus");
                pathElementList.add(pathElement);
            }
        }

        return pathElementList;
    }

    private Info parsePathInfo(JsonElement pathElement) {
        JsonElement infoElement = pathElement.getAsJsonObject().get("info");
        Info info = gson.fromJson(infoElement, Info.class);
        return info;
    }


    private void findFullPath(List<TrainObject> trainObjectList) {
        for (TrainObject trainObject : trainObjectList) {
            double mid_s_longtitude = trainObject.getSX();
            double mid_s_latitude = trainObject.getSY();
            double mid_e_longtitude = trainObject.getEX();
            double mid_e_latitude = trainObject.getEY();
//            double start_longtitude = srcCoordinate.getFirst();
//            double start_latitude = srcCoordinate.getSecond();
//            double end_longtitude = destCoordinate.getFirst();
//            double end_latitude = destCoordinate.getSecond();
//
//            logger.debug(start_longtitude + "," + start_latitude + " 에서 " + mid_s_longtitude + "," + mid_s_latitude + " 까지");
//            logger.debug(mid_e_longtitude + "," + mid_e_latitude + " 에서 " + end_longtitude + "," + end_latitude + " 까지");

        }
    }
//    private List<SubPath> parseSubPathList(JsonElement pathElement) {
//        JsonElement subPathElement = pathElement.getAsJsonObject().get("subPath").getAsJsonArray();
//        JsonArray subPathArray = subPathElement.getAsJsonArray();
//        logger.debug(subPathElement);
//
//        for (int i = 0; i < subPathArray.size(); i++) {
//            parseSubPath(subPathArray.get(i).getAsJsonObject());
//
//
//        }
//        return null;
//    }
//
//    private SubPath parseSubPath(JsonObject subPathObject) {
//        SubPath subPath = new SubPath();
//        logger.debug(subPathObject.get("distance"));
//        logger.debug(subPathObject.get("sectionTime"));
//        logger.debug(subPathObject.get("sectionTime"));
//        logger.debug(subPathObject.get("sectionTime"));
//        logger.debug(subPathObject.get("sectionTime"));
//        logger.debug(subPathObject.get("sectionTime"));
//        logger.debug(subPathObject.get("sectionTime"));
//        logger.debug(subPathObject.get("sectionTime"));
//
//
////        logger.debug(subPathElement);
//
//        return null;
//    }
}

