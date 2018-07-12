package services.dictionary;

import com.google.gson.Gson;
import org.apache.log4j.Logger;
import services.InterfaceService;
import wykwon.common.tools.FileTools;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wykwon on 2015-10-28.
 */
public class DictionaryService implements InterfaceService {
    private Gson gson;
    private static Logger logger = Logger.getLogger(DictionaryService.class);
    public Map<String, InterfaceDictionary> dictionaryMap = new HashMap<String, InterfaceDictionary>();

    public DictionaryService(String directory, String thesaurusList[], String dictionaryList[]) {
        gson = new Gson();
        initialize(directory, thesaurusList, dictionaryList);
    }


    public void initialize(String directory, String thesaurusList[], String dictionaryList[]) {
        //1. 유의어 사전 돌려라

        initThesaurus(directory + "/thesaurus/", thesaurusList);
        initDictionary(directory + "/dictionary/", dictionaryList);
//        Dictionary
//        Dictionary dictionary = new Dictionary("권우영");
//        dictionary.putContents("집", "서울 도봉구 창동");
//        dictionary.putContents("직장", "한양대학교 서울캠퍼스");
//        dictionary.putContents("가족", "권우영", "백재연");
//        logger.debug(dictionary);
//        dictionary.search("가족");
//
//        dictionaryMap.put("권우영", dictionary);
    }


    private void initThesaurus(String directory, String thesaurusList[]) {
        for (String thesaurusName : thesaurusList) {
            Thesaurus thesaurus = new Thesaurus(directory + thesaurusName + ".csv");
            dictionaryMap.put(thesaurusName, thesaurus);
        }
    }

    private void initDictionary(String directory, String dictionaryList[]) {
        for (String dictionaryName : dictionaryList) {
            Dictionary dictionary = new Dictionary(directory + dictionaryName + ".csv");
            dictionaryMap.put(dictionaryName, dictionary);
        }
    }

    public InterfaceDictionary getDictionary(String dictionaryName) {
        return dictionaryMap.get(dictionaryName);
    }

    @Override
    public String parseAndResponse(String requestStr) {
        // comma seperated
        String strArray[] = requestStr.split(",");
        if (strArray.length != 2) {
            return "ERROR with requestStr: " + requestStr;
        }
        String dictionaryName = strArray[0];
        String headword = strArray[1];
//        DictionaryRequest dictionaryRequest = gson.fromJson(requestStr, DictionaryRequest.class);
        InterfaceDictionary dict = dictionaryMap.get(dictionaryName);
        if (dict == null) {
            return "NO such dictionary such  " + requestStr;
        }
//        logger.debug(dict.getName());
        String ret[] = dict.search(headword);

//        logger.debug(Arrays.toString(ret));
        StringBuffer strbuf = new StringBuffer();
        for (String resultStr : ret) {
            strbuf.append(resultStr + ",");
        }

        strbuf.deleteCharAt(strbuf.length() - 1);
        return strbuf.toString();
//        return gson.toJson(response);
    }

    @Override
    public int desiredPort() {
        return 8090;
    }

    @Override
    public String getName() {
        return "DictionaryService";
    }


    public String toString() {
        StringBuffer strbuf = new StringBuffer();
        for (InterfaceDictionary interfaceDictionary : dictionaryMap.values()) {
            strbuf.append(interfaceDictionary + "\n");

        }
        return strbuf.toString();
    }

    //    public static void startServiceAsync() {
//        String directory= "datafiles/dictionary/";
//        String thesaurusNames[] ={"기기소유자","공통유의어","권우영유의어","공통인명사전"};
//        String dictionaryNames[] ={"권우영사전","이창은사전"};
//    }
    public static DictionaryService make() {
        String directory = "datafiles/";


        String thesaurusNames[] = FileTools.getFileNameWithoutExtension("datafiles/thesaurus", ".csv");
        logger.debug(Arrays.toString(thesaurusNames));
        String dictionaryNames[] = FileTools.getFileNameWithoutExtension("datafiles/dictionary", ".csv");
        logger.debug(Arrays.toString(dictionaryNames));
//        String thesaurusNames[] = {"기기소유자", "공통유의어", "권우영유의어", "이창은유의어", "하영국유의어", "안영민유의어", "주소유의어", "공통장소유의어"};
//        String dictionaryNames[] = {"권우영사전", "이창은사전", "하영국사전", "안영민사전","최은정사전", "주소사전"
//                , "권우영아내사전", "권우영동생사전", "권우영아버지사전", "권우영어머니사전","공통인명사전"
//        };
        DictionaryService dictionaryService = new DictionaryService(directory, thesaurusNames, dictionaryNames);
//        logger.debug(dictionaryService);
//        logger.debug(dictionaryService.parseAndResponse("권우영사전,아버지"));
        return dictionaryService;
    }


    public static void main(String[] args) {
        DictionaryService dictionaryService = make();
        String userName = dictionaryService.parseAndResponse("기기소유자,+821045598193");
        logger.debug(userName);
//        DictionaryService dictionaryService = new DictionaryService();
//        String str = new DictionaryRequest("권우영사전", "가족").toJson();
//        logger.debug(str);
//        logger.debug(dictionaryService.parseAndResponse(str));
    }
}

