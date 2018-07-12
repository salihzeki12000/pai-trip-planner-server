package wykwon.common.tools;

import java.io.File;
import java.util.Arrays;

/**
 * Created by wykwon on 2016-02-05.
 */
public class FileTools {

    public static String[] getFileName(String path){
        File dirFile=new File(path);
        File fileList[]=dirFile.listFiles();
        String fileNames[] =new String[fileList.length];
        for (int i=0; i<fileList.length;i++){
            if(fileList[i].isFile()) {
                fileNames[i] = fileList[i].getName();
            }
        }
        return fileNames;
    }

    public static String[] getFileNameWithoutExtension(String path, String extension){
        File dirFile=new File(path);
        File fileList[]=dirFile.listFiles();
        String fileNames[] =new String[fileList.length];
        for (int i=0; i<fileList.length;i++){
            if(fileList[i].isFile()) {
                String str = fileList[i].getName();
                fileNames[i] =str.replaceAll(extension,"");
            }
        }
        return fileNames;
    }

    public static void main(String[] args) {
        String fileNames[] = getFileNameWithoutExtension("datafiles/dictionary",".csv");
        System.out.println(Arrays.toString(fileNames));
    }
}
