import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.Color;
import java.awt.Dimension;

class ColorClient {
    String sentence;
    String modifiedSentence;
    BufferedReader inFromUser;
    Socket clientSocket;
    JFrame frame;
    JPanel panel;
	


    public void init() throws UnknownHostException, IOException {
        //inFromUser = new BufferedReader(new InputStreamReader(System.in));

        
        //Making frame
        frame = new JFrame();
        frame.setSize(800,800);
        panel = new JPanel();
        frame.add(panel);
        panel.setBounds(0,0, 0, 0);
        //panel.setPreferredSize(new Dimension(800,800));
        frame.setVisible(true);
    }

    public void colorRecv() throws IOException {
        clientSocket = new Socket("169.254.137.141", 6789);
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        sentence = "Empty";//inFromUser.readLine();

        outToServer.writeBytes(sentence + '\n');

        modifiedSentence = inFromServer.readLine();
        //System.out.println("FROM SERVER: " + modifiedSentence);




        String rgb = modifiedSentence;
        String[] rgbArr = rgb.split(",");

        System.out.println("FROM SERVER: " + rgb);

        int r = Integer.parseInt(rgbArr[0]);
        int g = Integer.parseInt(rgbArr[1]);
        int b = Integer.parseInt(rgbArr[2]);

        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel.setBackground(new Color(r,g,b));
        frame.setTitle(r+", "+g+", "+b);
        frame.repaint();
        close();
    }

    public void close() throws IOException {
        clientSocket.close();
    }


	public static void main(String[] args){
		ColorClient color = new ColorClient();
		
		
		try{
			
			color.init();
		
		}catch(Exception e){
			
			
			}
		
		while (true){
			
			try{
				
				color.colorRecv();
				//Thread.sleep(100);
				
			}catch(Exception e){
				
				//e.printStackTrace();
				
			}
		}
	}	
	
}