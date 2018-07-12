package services.dummy;

import org.apache.log4j.Logger;
import services.dictionary.DictionaryService;

/**
 * Created by wykwon on 2015-10-28.
 */
public class ServiceManager {
    private static Logger logger = Logger.getLogger(ServiceManager.class);

    private static ServiceManager instance = new ServiceManager();
    public static ServiceManager getInstance() {
        return instance;
    }


    DictionaryService dictionaryService;

    private ServiceManager() {
        dictionaryService = DictionaryService.make();
    }


    public DictionaryService getDictionaryService(){

        return dictionaryService;
    }


}
