package services.dictionary;

import au.com.bytecode.opencsv.CSVReader;
import org.apache.log4j.Logger;
import wykwon.common.MyCollections;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by wykwon on 2015-10-28.
 */
public class Dictionary implements InterfaceDictionary {


    private Map<String, String[]> table = new HashMap<String, String[]>();
    private static Logger logger = Logger.getLogger(Dictionary.class);
    String filename;

    public Dictionary(String filename) {
        this.filename = filename;
        try {
            readFromFile(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readFromFile(String filename) throws IOException {
        CSVReader reader = new CSVReader(new FileReader(filename), ',');
        String headers[] = reader.readNext();
        List<String[]> rowStrList = reader.readAll();

        for (String[] rowStr : rowStrList) {
            if (rowStr.length == 0) {
                continue;
            }
            if (rowStr[0].length() == 0) {
                continue;
            }
//            logger.debug(Arrays.toString(rowStr));
            if (rowStr[0].charAt(0) == '#') {
//                logger.debug("주석: " + Arrays.toString(rowStr));
                continue;
            }
            if (rowStr.length == 1) {
                continue;
            }
            String headword = rowStr[0].trim();
            List<String> contents = new ArrayList<String>();
            for (int i = 1; i < rowStr.length; i++) {

                if (rowStr[i].length() != 0 && !rowStr[i].equals(" ")) {
//                    logger.debug(i+ "th str="+rowStr[i]);
                    contents.add(rowStr[i].trim());
                }
            }

            putContents(headword, contents);


        }
        reader.close();
    }

    /**
     * 빈 사전항목을 추가함
     *
     * @param keyword
     * @param contents
     */
    public void putContents(String keyword, String... contents) {
        table.put(keyword, contents);
    }

    /**
     * 빈 사전항목을 추가함
     *
     * @param keyword
     * @param contents
     */
    public void putContents(String keyword, Collection<String> contents) {
        String strArray[] = new String[contents.size()];
        int i = 0;
        for (String str : contents) {
            strArray[i] = str;
            i++;
        }
        table.put(keyword, strArray);
    }

    /**
     * 기존의 사전항목에 추가적인 항목을 넣음
     *
     * @param keyword
     * @param contents
     */
    public void addContents(String keyword, String... contents) {
        String oldContents[] = table.get(keyword);
        Set<String> newContentsSet = new HashSet<String>();
        if (oldContents!=null){
            for (String str: oldContents) {
                newContentsSet.add(str);
            }
        }
        for (String str: contents) {
            newContentsSet.add(str);
        }

        String newContentsArray[] = MyCollections.toArray(newContentsSet);
        table.put(keyword,newContentsArray);

    }

    @Override
    public void deleteContents(String keyword, String... contents) {
        String oldContents[] = table.get(keyword);
        Set<String> newContentsSet = new HashSet<String>();
        if (oldContents==null){
            return ;
        }
        else{
            for (String str: oldContents) {
                newContentsSet.add(str);
            }
        }
        for (String str: contents) {
            newContentsSet.remove(str);
        }

        String newContentsArray[] = MyCollections.toArray(newContentsSet);
        table.put(keyword,newContentsArray);
    }


    public String[] search(String keyword) {
        return table.get(keyword);
    }

    @Override
    public String getName() {
        return filename;
    }


    public String toString() {

        StringBuffer strbuf = new StringBuffer();
        for (Map.Entry<String, String[]> entry : table.entrySet()) {
            strbuf.append(entry.getKey() + " = " + Arrays.toString(entry.getValue()) + "\n");
        }
        return strbuf.toString();
    }

    public static void test() {
        Dictionary dictionary = new Dictionary("datafiles/dictionary/권우영사전.csv");
        logger.debug(dictionary);


    }

    public static void main(String[] args) {
        test();

    }
}
