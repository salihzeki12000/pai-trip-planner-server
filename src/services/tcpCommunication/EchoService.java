package services.tcpCommunication;

import services.InterfaceService;

/**
 * Created by wykwon on 2015-10-28.
 */
public class EchoService implements InterfaceService {
    public String parseAndResponse(String requestStr){
        return requestStr;
    }

    @Override
    public int desiredPort() {
        return 0;
    }

    @Override
    public String getName() {
        return "EchoService";
    }
}
