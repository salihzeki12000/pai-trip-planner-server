package services.tcpCommunication;

import com.google.gson.Gson;
import org.apache.log4j.Logger;
import services.InterfaceService;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 15. 2. 2
 * Time: ?��?�� 4:01
 * To change this templa-/te use File | Settings | File Templates.
 */
public class ServiceServer implements Runnable {


    private ServerSocket serverSocket = null;
    private InterfaceService service;
    private static Logger logger = Logger.getLogger(ServiceServer.class);
    private int desiredPort;

    public ServiceServer(InterfaceService serviceHandler) {
        this.service = serviceHandler;
        this.desiredPort = serviceHandler.desiredPort();
    }

    public void init(int port) {
        try {
            serverSocket = new ServerSocket(port);
            logger.debug("ServiceServer " + service.getName() + " start at port " + port);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void init() {
        try {
            serverSocket = new ServerSocket(desiredPort);
            logger.debug("ServiceServer " + service.getName() + " start at port " + desiredPort);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


    public void run() {

        Gson gson = new Gson();
        Socket connectionSocket = null;
        while (true) {
            try {
                connectionSocket = serverSocket.accept();
                logger.debug("acccepted " + connectionSocket);


                DataInputStream inFromClient =
                        new DataInputStream(connectionSocket.getInputStream());

                DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
                String msgFromClient = inFromClient.readUTF();
                logger.info(msgFromClient);
                String returnMessage = service.parseAndResponse(msgFromClient);
                logger.info(returnMessage);
                outToClient.writeUTF(returnMessage);
            } catch (Exception e) {
                DataOutputStream outToClient ;
                try {
                    outToClient = new DataOutputStream(connectionSocket.getOutputStream());
                    outToClient.writeUTF("Wrong message type ");
                    connectionSocket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.

            }
        }

    }


    public static void main(String argv[]) throws Exception {

        while (true) {
            ServiceServer dictionaryServiceServer = new ServiceServer(new EchoService());
            try {
                dictionaryServiceServer.init(8089);
                dictionaryServiceServer.run();
            } catch (Exception e) {

                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

    }
}
