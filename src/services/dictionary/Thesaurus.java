package services.dictionary;

import au.com.bytecode.opencsv.CSVReader;
import org.apache.log4j.Logger;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by wykwon on 2015-10-28.
 */
public class Thesaurus implements InterfaceDictionary{

    private static Logger logger = Logger.getLogger(Thesaurus.class);
    // 표제어, 유의어들의 map
    private Map<String, Set<String>> synonymsTable = new HashMap<String, Set<String>>();
    // 유의어1개, 표제어의 검색이 가능하도록 하는 map
    private Map<String, String> searchTable = new HashMap<String, String>();
    String filename;
    public Thesaurus(String filename){
        this.filename = filename;
        try {
//            readFromFile("datafiles/dictionary/공통유의어.csv");
            readFromFile(filename);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void putContents(String keyword, String... contents) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void addContents(String keyword, String... contents) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void deleteContents(String keyword, String... contents) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public String[] search(String keyword) {
        String str[] = new String[1];
        str[0] = getHeadword(keyword);
        return str;
    }

    public String getHeadword(String query) {
        return searchTable.get(query.trim());
    }




    public void readFromFile(String filename) throws IOException {
        CSVReader reader = new CSVReader(new FileReader(filename), ',');
        String headers[] = reader.readNext();
        List<String[]> rowStrList = reader.readAll();

        for (String[] rowStr : rowStrList) {
//            logger.debug(Arrays.toString(rowStr));
            if (rowStr.length==0){
                continue;
            }
            if (rowStr[0].length()==0){
                continue;
            }
            if (rowStr[0].charAt(0) == '#') {
//                logger.debug("주석: " + Arrays.toString(rowStr));
                continue;
            }
            if (rowStr.length == 1) {
                continue;
            }
            String headword = rowStr[0].trim();
            List<String> synonyms = new ArrayList<String>();
            for (int i=1; i<rowStr.length; i++){

                if (rowStr[i].length()!=0 && !rowStr[i].equals(" ") ) {
//                    logger.debug(i+ "th str="+rowStr[i]);
                    synonyms.add(rowStr[i].trim());
                }
            }

            addSynonyms(headword, synonyms);


        }
        reader.close();
    }


    public void addSynonyms(String headword, String... synonyms) {
        searchTable.put(headword,headword);
        Set<String> synonymSet =  synonymsTable.get(headword);
        if (synonymSet==null){
            synonymSet = new HashSet<String>();
        }
        for (String synonym: synonyms) {
            synonymSet.add(synonym);
            searchTable.put(synonym, headword);
        }
        synonymsTable.put(headword, synonymSet);
    }
    public void addSynonyms(String headword, List<String> synonyms) {
        searchTable.put(headword,headword);
        Set<String> synonymSet =  synonymsTable.get(headword);
        if (synonymSet==null){
            synonymSet = new HashSet<String>();
        }
        for (String synonym: synonyms) {
            synonymSet.add(synonym);
            searchTable.put(synonym,headword);
        }
        synonymsTable.put(headword,synonymSet);
    }
    public Map<String, Set<String>> getSynonymsMap(){
        return synonymsTable;
    }
    public String toString(){
        StringBuffer strbuf = new StringBuffer();
        for (Map.Entry<String,Set<String>> entry: synonymsTable.entrySet()){
            strbuf.append(entry.getKey() + " = " + entry.getValue() + "\n");
        }
        return strbuf.toString();
    }
    @Override
    public String getName() {
        return filename;
    }

    public static void test() {
        Thesaurus thesaurus = new Thesaurus("datafiles/dictionary/공통유의어.csv");
        logger.debug(thesaurus.getHeadword("이번 주"));
    }

    public static void main(String[] args) {
        test();
    }



}
