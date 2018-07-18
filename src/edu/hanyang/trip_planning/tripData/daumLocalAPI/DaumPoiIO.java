package edu.hanyang.trip_planning.tripData.daumLocalAPI;

import au.com.bytecode.opencsv.CSVReader;
import com.google.gson.Gson;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Daum local api를 저장함.
 * json object[item] 의 array형태로 저장됨
 * <p/>
 * 파일은 append형태로 이루어지며,
 */

public class DaumPoiIO {
    private static Logger logger = Logger.getLogger(DaumPoiIO.class);
    private String fileName = "datafiles/pois/daum/daum_pois.json";
    private static String indices[] = {"가", "나", "다", "라", "마", "바", "사", "아", "자", "차", "타", "파", "하", "A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    public DaumPoiIO() {

    }


    public DaumPoiIO(String fileName) {
        this.fileName = fileName;
    }

    // json 형태인데,
    public static void initDirectory(String directory) {
        // 가~하, A-Z 까지의 빈 텍스트 파일을 만드세

        String fileNames[] = {"가", "나", "다", "라", "마", "바", "사", "아", "자", "차", "타", "파", "하", "A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        try {
            for (String filename : fileNames) {

                String name = directory + filename + ".txt";
                FileWriter fw = new FileWriter(name, true);
                logger.debug(name);
                fw.flush();
                fw.close();

            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


    public ItemList readItemList() {
        return readItemSet(this.fileName);
    }

    public void writeItemList(Collection<Item> additionalItems) {
        ItemList itemList = readItemSet(fileName);
        itemList.addItems(additionalItems);
        writeItemList(fileName, itemList);
        logger.debug(itemList);


        // 1. 이전 POI를 읽어들인다.
//            ItemList itemList =
        // 2.
    }


    public ItemList readItemSet(String filename) {
        Gson gson = new Gson();
        BufferedReader br = null;
        ItemList itemList = null;
        try {
            br = new BufferedReader(new FileReader(filename));
            itemList = gson.fromJson(br, ItemList.class);

        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        if (itemList == null) {
            return new ItemList();
        } else {
            return itemList;
        }

    }

    public ItemList readItemMap(String filename) {
        Gson gson = new Gson();
        BufferedReader br = null;
        ItemList itemList = null;
        try {
            br = new BufferedReader(new FileReader(filename));
            itemList = gson.fromJson(br, ItemList.class);

        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        if (itemList == null) {
            return new ItemList();
        } else {
            return itemList;
        }

    }

    public void writeItemList(String filename, ItemList itemList) {
        Gson gson = new Gson();
        String str = gson.toJson(itemList);
        FileWriter writer = null;
        try {
            writer = new FileWriter(fileName);
            writer.write(str);
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public static void testRead() {
        DaumPoiIO daumPOIWriter = new DaumPoiIO("datafiles/pois/daum/daum_pois.json");
        ItemList itemList = daumPOIWriter.readItemSet("datafiles/pois/daum/daum_pois.json");
        logger.debug("size=" + itemList.getItemList().size());

    }


    public static void testWrite() {
        DaumPoiIO daumPOIWriter = new DaumPoiIO("datafiles/pois/daum/daum_pois3.json");
        List<Item> list = DaumLocalAPI.getPOIs("평양면옥", 20);
        logger.debug(list);
//        Item item = list.get(0);
//        item.setTitle("가산디지털단지 1호선");
//        logger.debug(list);
        daumPOIWriter.writeItemList(list);
    }

    public static void subway2() {
        try {
            List<Item> list = new ArrayList<Item>();
            DaumPoiIO daumPOIWriter = new DaumPoiIO();

            CSVReader csvReader = new CSVReader(new FileReader("documents/subwayStations.csv"), '\t');
            List<String[]> rowStrList = csvReader.readAll();
            for (String[] strs : rowStrList) {
                String str = strs[1] + ' ' + strs[0] + "역 ";
                System.out.println(str);

                list.addAll(DaumLocalAPI.getPOIs(str, 3));


            }
            daumPOIWriter.writeItemList(list);
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

//
//        DaumPoiIO daumPOIWriter = new DaumPoiIO();
//        for (String sta : subway2) {
//            List<Item> list = DaumLocalAPI.getPOIs(sta, 1);
//            daumPOIWriter.writeItemList(list);
//        }

    }

    public static void main(String[] args) {

        testWrite();

//
//        testRead();
//        subway2();
//        List<Item> list = DaumLocalAPI.getPOIs("한양대학교");
//        for (Item infoPOI : list) {
//            logger.debug(infoPOI);
//        }
//        ItemList itemlist = new ItemList();

//        writeItem(DaumLocalAPI.dummyItem());
//        initDirectory("datafiles/pois/daum/");


    }
}
