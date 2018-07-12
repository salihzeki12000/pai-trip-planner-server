package edu.hanyang.trip_planning.tripHTBN.poi;

import au.com.bytecode.opencsv.CSVReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wykwo on 2015-10-02.
 */
public class SubsetPOIGen {
    public static SubsetPOIs getJeju()  {
        // 여행지, 식당,  호텔 모두 읽기
        List<String> poiTitleList = new ArrayList<>();
        try {
            CSVReader csvReader = new CSVReader(new FileReader("datafiles/pois/jeju_attraction_pois.csv"),'\t');
            for (String rowStrs[] :  csvReader.readAll()){
                if (rowStrs.length == 0) {
                    continue;
                } else if (rowStrs[0].charAt(0) == '#') {
                    continue;
                }
                    poiTitleList.add(rowStrs[1]);

            }
            csvReader.close();
            csvReader = new CSVReader(new FileReader("datafiles/pois/jeju_resturant_pois.csv"),'\t');
            for (String rowStrs[] :  csvReader.readAll()){
                if (rowStrs.length == 0) {
                    continue;
                } else if (rowStrs[0].charAt(0) == '#') {
                    continue;
                }
                poiTitleList.add(rowStrs[1]);
            }
            csvReader = new CSVReader(new FileReader("datafiles/pois/jeju_hotel_pois.csv"),'\t');
            for (String rowStrs[] :  csvReader.readAll()){
                if (rowStrs.length == 0) {
                    continue;
                } else if (rowStrs[0].charAt(0) == '#') {
                    continue;
                }
                poiTitleList.add(rowStrs[1]);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SubsetPOIs subsetPOIs = new SubsetPOIs();
        subsetPOIs.makeSubsetPOIsByTitle(poiTitleList);
        return subsetPOIs;
    }

    public static SubsetPOIs genSeoul() {
        SubsetPOIs subsetPOIs = new SubsetPOIs();
        subsetPOIs.makeSubsetPOIsByTitle("분당서울대학교병원", "교보문고 광화문점", "뚝섬한강공원", "창동주공4단지아파트",
                "한양대학교 정보통신관");
        return subsetPOIs;
    }

    public static SubsetPOIs getJeju10_() {
        SubsetPOIs subsetPOIs = new SubsetPOIs();
        subsetPOIs.makeSubsetPOIsByTitle("메종글래드제주", "섭지코지", "산방굴사", "대포주상절리", "제주민속촌박물관", "비자림", "삼성혈", "만장굴", "성산일출봉", "용연구름다리", "정방폭포", "쇠소깍","돈사돈");
        return subsetPOIs;
    }

    public static SubsetPOIs getJeju30() {
        SubsetPOIs subsetPOIs = new SubsetPOIs();
        subsetPOIs.makeSubsetPOIsByTitle("메종글래드제주", "섭지코지", "산방굴사", "대포주상절리", "제주민속촌박물관", "비자림", "삼성혈", "만장굴", "성산일출봉", "용연구름다리",
                "정방폭포","소인국테마파크",               "천제연폭포","천제연난대림","제주마방목지","별빛누리 공원","영실기암","녹하지악","쇠소깍","제주중문관광단지",
                "이중섭미술관","홍조단괴해수욕장","화순금모래해변","오설록티뮤지엄","백약이오름","천지연폭포","사려니숲길","여미지식물원"
                ,"노루생태관찰원","금능석물원","신산공원","용머리해안","산방산탄산온천","넥슨컴퓨터박물관","새섬","한라산국립공원 돈내코탐방로"
                ,"돈사돈","한성식당","모이세해장국","대촌회미락","흑돈가","천제연토속음식점","노형삼대국수회관","어진이네횟집","국수회관 본점","꺼멍흑돼지"
                ,"롯데면세점 제주점","제주관광공사 지정면세점"

        );
        return subsetPOIs;
    }
    public static SubsetPOIs getJeju50() {
        SubsetPOIs subsetPOIs = new SubsetPOIs();
        subsetPOIs.makeSubsetPOIsByTitle("메종글래드제주", "섭지코지", "산방굴사", "대포주상절리", "제주민속촌박물관", "비자림", "삼성혈", "만장굴", "성산일출봉", "용연구름다리", "정방폭포", "그랜드호텔사거리"
                ,"소인국테마파크"
                ,               "천제연폭포","천제연난대림","제주마방목지","별빛누리 공원","제주박기사 승합차여행","영실기암","녹하지악","쇠소깍","제주중문관광단지"
                ,               "1","2","3","4","5","6","7","8","9","10"
                ,               "11","12","13","14","15","16","17","18","19","20"
                ,               "21","22","23","24","25","26","27","28"
        );
        return subsetPOIs;
    }

    public static void main(String[] args) {
        getJeju();
    }

}
