package wykwon.common.tools;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 13. 8. 13
 * Time: 오전 11:42
 * To change this template use File | Settings | File Templates.
 */
public class FileNameTools {
    public static String[] make(String body, int startIdx, int endIdx) {
        String fileNames[] = new String[endIdx - startIdx + 1];
        for (int i = startIdx; i <= endIdx; i++) {
            fileNames[i - startIdx] = body + i + ".csv";
        }
        return fileNames;
    }

    /**
     * 진짜로 읽어보고 만든다.
     *
     * @param body
     * @param startIdx
     * @param endIdx
     * @return
     */
    public static String[] safeMake(String body, int startIdx, int endIdx) {
        List<String> fileNameList = new ArrayList<String>();

        String fileNames[] = new String[endIdx - startIdx + 1];
        for (int i = startIdx; i <= endIdx; i++) {
            fileNames[i - startIdx] = body + i + ".csv";
            try {
                FileReader reader = new FileReader(fileNames[i - startIdx]);
                fileNameList.add(fileNames[i - startIdx]);
            } catch (FileNotFoundException e) {
//                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        String[] ret = new String[fileNameList.size()];
        return fileNameList.toArray(ret);
    }

    /**
     * 진짜로 읽어보고 만든다.
     *
     * @param body
     * @param startIdx
     * @param endIdx
     * @return
     */
    public static String[] safeMake(String dir, String body, int startIdx, int endIdx) {
        List<String> fileNameList = new ArrayList<String>();

        String fileNames[] = new String[endIdx - startIdx + 1];
        for (int i = startIdx; i <= endIdx; i++) {
            fileNames[i - startIdx] = body + i + ".csv";
            try {
                FileReader reader = new FileReader(dir + fileNames[i - startIdx]);
                fileNameList.add(fileNames[i - startIdx]);
            } catch (FileNotFoundException e) {
//                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        String[] ret = new String[fileNameList.size()];
        return fileNameList.toArray(ret);
    }

    /**
     * 진짜로 읽어보고 만든다.
     *
     * @param body
     * @param startIdx
     * @param endIdx
     * @return
     */
    public static String[] safeMakeTraining(String body, int startIdx, int endIdx) {
        List<String> fileNameList = new ArrayList<String>();

        String fileNames[] = new String[endIdx - startIdx + 1];
        for (int i = startIdx; i <= endIdx; i++) {
            fileNames[i - startIdx] = body + i + ".csv";
            try {
                FileReader reader = new FileReader(fileNames[i - startIdx]);
                if (i % 10 != 0) {
                    fileNameList.add(fileNames[i - startIdx]);
                }
            } catch (FileNotFoundException e) {
//                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        String[] ret = new String[fileNameList.size()];
        return fileNameList.toArray(ret);
    }

    /**
     * 진짜로 읽어보고 만든다.
     *
     * @param body
     * @param startIdx
     * @param endIdx
     * @return
     */
    public static String[] safeMakeTest(String body, int startIdx, int endIdx) {
        List<String> fileNameList = new ArrayList<String>();

        String fileNames[] = new String[endIdx - startIdx + 1];
        for (int i = startIdx; i <= endIdx; i++) {
            fileNames[i - startIdx] = body + i + ".csv";
            try {
                FileReader reader = new FileReader(fileNames[i - startIdx]);

                if (i % 10 == 0) {
                    fileNameList.add(fileNames[i - startIdx]);
                }
            } catch (FileNotFoundException e) {
//                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        String[] ret = new String[fileNameList.size()];
        return fileNameList.toArray(ret);
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(FileNameTools.make("edesd", 1, 10)));
    }

}
