package services.protocol;

/**
 * Created by wykwon on 2015-10-28.
 */
public class DictionaryResponse {
    public String type;
    public String contents[];

    public DictionaryResponse(String tyype, String... contents){
        this.type = type;
        this.contents = contents.clone();
    }

}
