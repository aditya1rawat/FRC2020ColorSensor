package frc.robot;

import java.io.*;
import java.net.*;

public class ColorServer {
    ServerSocket welcomeSocket;
    String clientSentence;
    String capitalizedSentence;

    String rgb;// = String.join(",", rgbArr);

    public void init()  throws IOException{
        welcomeSocket = new ServerSocket(6789);
    }

    public void colorSend(double r, double g, double b)  throws IOException{

        String[] rgbArr = {""+(int)(r*1), ""+(int)(g*1), ""+(int)(b*1)+", "};
        rgb = String.join(",", rgbArr) + "\n";

        Socket connectionSocket = welcomeSocket.accept();
        //System.out.println("This ran.");

        //connectionSocket.isConnected();
        BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
        DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
        
        while((clientSentence = inFromClient.readLine()) != null) {
        

            //System.out.println("Received: "+clientSentence);
            //capitalizedSentence = clientSentence.toUpperCase() + 'n';
            outToClient.writeBytes(rgb);
        }
        //System.out.println("Connection closed");
        connectionSocket.close();
    }
}   