package services.tcpCommunication;

import org.apache.log4j.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 15. 2. 2
 * Time: ?��?�� 4:08
 * To change this template use File | Settings | File Templates.
 */
public class TCPClient {

    Socket clientSocket;
    DataOutputStream outToServer;
    DataInputStream inFromServer;
    private static Logger logger = Logger.getLogger(TCPClient.class);

    public TCPClient(String hostAddr, int port) {
        try {
            clientSocket = new Socket(hostAddr, port);
            outToServer = new DataOutputStream(clientSocket.getOutputStream());
            inFromServer = new DataInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    /**
     * data 보내�? 받기
     *
     * @param msg 보낼 message (UTF-8)
     * @return 반환?��?�� message (UTF-8)
     * @throws IOException
     */
    public String requestAndReceiveUTF(String msg) throws IOException {
        outToServer.writeUTF(msg);

        String retMsg = inFromServer.readUTF();
        logger.debug(retMsg);
        return retMsg;
    }

    /**
     * String ?�� 보낸?��.
     * 2byte�? size�?
     * ?��?���??��?�� ?��?�� size�? ?��?��?�� byte buffer?�� ???��?��
     * String?���? �??��
     *
     * @param msg
     * @return
     */
    public String requestAndReceiveStr(String msg) throws IOException {
        byte outBytes[] = msg.getBytes();
        outToServer.writeShort(outBytes.length);
        outToServer.write(outBytes);
        int retSize = inFromServer.readShort();
        byte inBytes[] = new byte[retSize];
        inFromServer.readFully(inBytes, 0, retSize);
        String retStr = new String(inBytes, "UTF8");
//        logger.debug(retStr);
        return retStr;
    }

    public void close() {
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public static void main(String argv[]) throws Exception {
        TCPClient client = new TCPClient("166.104.36.218", 8088);
//        String retStr= client.requestAndReceiveUTF("01045598193/다음달에 가족모임을 성남시에서 하려고 하는데 언제 어디서 하는게 좋을까?");
        String retStr= client.requestAndReceiveUTF("01045598193/다음달에 아내와 제주도로 3일간 여행을 다녀오려고 하는데" +
                " 계획좀 부탁해");
//            String retStr= client.requestAndReceiveUTF("01045598193/다음주에 엑소브레인 실무자 회의를 하려고 하는데 언제 어디서 하면 좋을까?");
        logger.debug(retStr);
//        DictionaryRequest dictionaryRequest = new DictionaryRequest("권우영사전","가족");
//        Gson gson = new Gson();
//        logger.debug(gson.toJson(dictionaryRequest));
//
//        String retStr = client.requestAndReceiveStr(gson.toJson(dictionaryRequest));
//        logger.debug(retStr);

    }
}
