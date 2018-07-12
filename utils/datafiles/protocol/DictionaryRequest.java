package services.protocol;

import com.google.gson.Gson;

/**
 * Created by wykwon on 2015-10-28.
 */
public class DictionaryRequest {
    public String dictionaryName;
    public String headword;

    public DictionaryRequest(String dictionaryName, String headword){
        this.dictionaryName = dictionaryName;
        this.headword = headword;
    }

    public String toJson(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static void main(String[] args) {
        DictionaryRequest request = new DictionaryRequest("권우영","가족");
        System.out.println(request.toJson());
    }
}
