package services.dictionary.time;

import au.com.bytecode.opencsv.CSVReader;
import org.apache.log4j.Logger;

import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 15. 2. 20
 * Time: 오후 8:13
 * 뭐 별겨 있냐?
 * 이런 표현이 있는지를 확인하는게지
 */
public class QueryAnalysis {
    // 대표이름과 나머지들

    // 개인적 호칭
    private Map<String, String[]> personalTitleMap = new HashMap<String, String[]>();
    private Map<String, String[]> keywordMap = new HashMap<String, String[]>();
    private Map<String, String[]> nameMap = new HashMap<String, String[]>();
    private static Logger logger = Logger.getLogger(QueryAnalysis.class);

    public QueryAnalysis() {
        try {
            loadPersonalTitle("datafile/text_analysis/personalTitle.csv");
            loadKewords();
//            loadKeywords("datafile/text_analysis/keywords.csv");
            loadNames();
//            loadNames("datafile/text_analysis/names.csv");

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public void loadPersonalTitle(String filename) throws IOException {
        CSVReader reader = new CSVReader(new FileReader(filename), ',');
        String headers[] = reader.readNext();
        List<String[]> rowStrList = reader.readAll();
        for (String[] rowStr : rowStrList) {
            if (rowStr[0].charAt(0) == '#') {
                continue;
            }

            personalTitleMap.put(rowStr[0], Arrays.copyOfRange(rowStr, 1, rowStr.length));
        }
        reader.close();

    }

    public void loadNames(String filename) throws IOException {
        CSVReader reader = new CSVReader(new FileReader(filename), ',');
        String headers[] = reader.readNext();
        List<String[]> rowStrList = reader.readAll();
        for (String[] rowStr : rowStrList) {
            if (rowStr[0].charAt(0) == '#') {
                continue;
            }

            nameMap.put(rowStr[0], rowStr);
        }
        reader.close();

    }

    public void loadNames() throws IOException {
        String strs = "권우영\n김성훈\n하영국\n엑소브레인\n" +
                "내동창,내 동창,동창\n" +
                "내동료,동료\n" +
                "내가족,가족,내 가족\n" +
                "내동생,동생,내 동생\n" +
                "나,내,나의\n" +
                "내친구,친구\n" +
                "아버지,아빠\n" +
                "어머니,엄마\n" +
                "부모님,부모\n";
        CSVReader reader = new CSVReader(new StringReader(strs), ',');
        List<String[]> rowStrList = reader.readAll();
        for (String[] rowStr : rowStrList) {
            if (rowStr[0].charAt(0) == '#') {
                continue;
            }

            nameMap.put(rowStr[0], rowStr);
        }
        reader.close();
    }

    public void loadKeywords(String filename) throws IOException {
        CSVReader reader = new CSVReader(new FileReader(filename), ',');
        String headers[] = reader.readNext();
        List<String[]> rowStrList = reader.readAll();
        for (String[] rowStr : rowStrList) {
            if (rowStr[0].charAt(0) == '#') {
                continue;
            }

            keywordMap.put(rowStr[0], rowStr);
        }
        reader.close();

    }

    public void loadKewords() throws IOException {
        String strs = "회의,미팅\n" +
                "식사,점심,저녁,회식,가족모임,가족 모임,모임,아침,식당,먹을까,먹기,먹을,맛집,음식점\n" +
                "점심식사,점심\n" +
                "저녁식사,저녁,회식\n" +
                "아침식사,아침,조찬\n" +
                "주말식사,주말,가족모임,가족 모임\n" +
                "이번주,이번 주,금주\n" +
                "오늘,금일\n" +
                "주말,이번주말,이번 주말\n" +
                "주중,이번주중,이번 주\n" +
                "다음주,다음 주\n" +
                "다음주말,다음 주말\n" +
                "이번달,이번 달,이달,요번달\n" +
                "다음달,다음 달\n" +
                "1주,한주,일주\n" +
                "2주,이주,두주\n" +
                "오늘,금일\n"+
                "내일,내일\n"+
                "10일,열흘\n" +
                "3주,삼주,세주\n" +
                "1달,한달\n" +
                "내친구,친구,내 친구,친구들\n" +
                "내동창,동창,내 동창,동료\n" +
                "평일\n";

        CSVReader reader = new CSVReader(new StringReader(strs), ',');
        List<String[]> rowStrList = reader.readAll();
        for (String[] rowStr : rowStrList) {
            if (rowStr[0].charAt(0) == '#') {
                continue;
            }

            keywordMap.put(rowStr[0], rowStr);
        }
        reader.close();

    }



    private Set<String> findPersonalNames(String queryStr) {
        Set<String> nameSet = hasNames(queryStr);
        Set<String> realNameSet = new HashSet<String>();
        for (String name : nameSet) {
            String values[] = personalTitleMap.get(name);
            if (values != null) {
                for (String value : values) {
//                    logger.debug("name="+name + "\t value="+value);
                    realNameSet.add(value);
                }
            } else {
                realNameSet.add(name);
            }
        }
//        logger.debug(realNameSet);
        if (realNameSet.size()==0){
            realNameSet.add(personalTitleMap.get("나")[0]);
        }
        return realNameSet;
    }


    private String findEatType(Set<String> keywordSet) {

        boolean bDinner = true;
        boolean bLunch = true;
        boolean bWeekday = true;
        boolean bWeekend = true;

        if (keywordSet.contains("저녁식사")) {
            bDinner = true;
            bLunch = false;
        }
        if (keywordSet.contains("점심식사")) {
            bDinner = false;
            bLunch = true;
        }
        if (keywordSet.contains("주말")) {
            bWeekday = false;
            bWeekend = true;
        }
        if (keywordSet.contains("평일")) {
            bWeekday = true;
            bWeekend = false;
        }

        if (bDinner == true && bLunch == true && bWeekday == true && bWeekend == true) {
            return "full";
        } else if (bDinner == true && bLunch == true && bWeekday == true && bWeekend == false) {
            return "평일";
        } else if (bDinner == true && bLunch == true && bWeekday == true && bWeekend == true) {
            return "주말";
        } else if (bDinner == false && bLunch == true && bWeekday == true && bWeekend == true) {
            return "점심";
        } else if (bDinner == true && bLunch == false && bWeekday == true && bWeekend == true) {
            return "저녁";
        } else if (bDinner == false && bLunch == true && bWeekday == true && bWeekend == false) {
            return "평일점심";
        } else if (bDinner == true && bLunch == false && bWeekday == true && bWeekend == false) {
            return "평일저녁";
        } else if (bDinner == false && bLunch == true && bWeekday == false && bWeekend == true) {
            return "주말점심";
        } else if (bDinner == true && bLunch == false && bWeekday == false && bWeekend == true) {
            return "주말저녁";
        } else {
            return "몰라";
        }
    }

    private String[] getTemporalConstraint(Set<String> keywordSet) {
        /**
         *
         * 이번주,이번 주,금주
         오늘,금일
         주말,이번주말,이번 주말
         주중,이번주중,이번 주
         다음주말,다음 주말
         이번달,이번 달,이달,요번달
         다음달,다음 달
         1주,한주,일주
         2주,이주,두주
         10일,열흘
         3주,삼주,세주
         1달,한달
         */
        AnalysisTemporalStatements analysisTemporalStatements = new AnalysisTemporalStatements("권우영");
        String ret[] ;
        if (keywordSet.contains("이번주")) {
            ret = analysisTemporalStatements.getThisWeekDays();
        } else if (keywordSet.contains("주말")) {
            ret = analysisTemporalStatements.getThisWeekend();
        } else if (keywordSet.contains("주중")) {
            ret = analysisTemporalStatements.getThisWeekDays();
        }else if (keywordSet.contains("다음주")) {
            ret = analysisTemporalStatements.getNextWeekDays();
        }
        else if (keywordSet.contains("다음주말")) {
            ret = analysisTemporalStatements.getNextWeekends();
        } else if (keywordSet.contains("2주")) {
            ret = analysisTemporalStatements.getNextTwoWeekDays();
        }
        else if (keywordSet.contains("이번달")) {
            ret = analysisTemporalStatements.getThisMonth();
        } else if (keywordSet.contains("다음달")) {
            ret = analysisTemporalStatements.getNextMonthStr();
        } else if (keywordSet.contains("오늘")) {
            ret = analysisTemporalStatements.getToday();
        } else if (keywordSet.contains("내일")) {
            logger.debug("내일");
            ret = analysisTemporalStatements.getTomorrow();
        } else {
            // Default 다음주
            ret = analysisTemporalStatements.getThisWeekDays();
        }


        return ret;
    }

    /**
     * keyword가 문장에 포함되었는지를 볼것
     *
     * @param queryStr
     * @return
     */


    private Set<String> hasKeywords(String queryStr) {
        Set<String> keywordSet = new HashSet<String>();
        for (Map.Entry<String, String[]> entry : keywordMap.entrySet()) {
            if (hasKeyword(queryStr, entry.getValue())) {
                keywordSet.add(entry.getKey());
            }
        }

        return keywordSet;
    }

    private Set<String> hasNames(String queryStr) {
        Set<String> nameSet = new HashSet<String>();
        for (Map.Entry<String, String[]> entry : nameMap.entrySet()) {
            if (hasKeyword(queryStr, entry.getValue())) {
                nameSet.add(entry.getKey());
            }
        }

        return nameSet;
    }

    private static boolean hasKeyword(String queryStr, String... keywordVariation) {
        for (String keyword : keywordVariation) {
            if (queryStr.indexOf(keyword) >= 0) {
                return true;
            }
        }
        return false;
    }


}
