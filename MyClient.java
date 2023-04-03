import java.io.*;
import java.net.*;

import org.w3c.dom.TypeInfo;

import java.net.Socket;
import java.net.InetAddress;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.concurrent.TimeUnit;
import java.io.InputStreamReader;
import java.io.BufferedReader;
 
public class MyClient {
    public static void main(String[] args) {
    //while(true){
    	try {
    		Socket s = new Socket("localhost", 50000);
    		
    		
			// Client端循环内容：
			// REDY
   			// 获取JOBN
			// Gets Capable
   			// 获取 DATA X Y
			// OK
   			// 获取服务器列表
			// OK
   			// 获取一个“.”
			// SCHD


    		//BufferedReader bin = new BufferedReader(new InputStreamReader(s.getInputStream() ) );
    		BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream() ) );
    		DataOutputStream out = new DataOutputStream(s.getOutputStream());
    		
    		out.write(("HELO" + "\n").getBytes());
    		
    		String str = in.readLine();
    		print(str);

    		String username = System.getProperty("user.name");
    		out.write(("AUTH " + username + "\n").getBytes() );
    		str = in.readLine();
			print(str);

			int jobID = 0;
			int serverCount = 0;
			int coreCount = 0;
			String largestType = null;
			boolean flag = true;

			out.write( ("REDY\n").getBytes() );
			str = in.readLine();
			print(str);

			
			out.write( ("OK\n").getBytes() );
			str = in.readLine();
			print(str);
			
			
			//loop X times, each time a line of records
			while(true){
				if (str.equals("NONE")) break; // for NONE
				else if (str.equals("JCPL")) continue; // for JCPL
				else { // for JOBN
					String[] jobInfo = str.split(" "); 

					jobID = Integer.parseInt(jobInfo[2]);

					out.write( ("GETS Capable\n").getBytes() );
					str = in.readLine(); // DATA X Y
					String[] serverInfoList = str.split(" "); 

					out.write( ("OK\n").getBytes() );

					for (int i = 0; i < Integer.parseInt(serverInfoList[1]); i++){
						if (flag) {
							str = in.readLine(); // server information each line
							print(str);

							if(Integer.parseInt(serverInfoList[2]) > coreCount) {

							}
						}
					}
					flag = false;
				}
			}

    		
    		//send: OK
			out.write( ("OK\n").getBytes() );
			str = in.readLine();
			print(str);


			while(true){
				if (str.equals("NONE")) break; // for NONE
				else if (str.equals(".")) continue;
				else { // for SCHD
					String[] jobInfo = str.split(" "); 

					jobID = Integer.parseInt(jobInfo[2]);

					out.write( ("SCHD\n").getBytes() );
					str = in.readLine(); // DATA X Y
					String[] serverInfoList = str.split(" "); 

					out.write( ("OK\n").getBytes() );

					for (int i = 0; i < Integer.parseInt(serverInfoList[1]); i++){
						if (flag) {
							str = in.readLine(); // server information each line
							print(str);

							if(Integer.parseInt(serverInfoList[2]) > coreCount) {
							
							}
						}
					}
					flag = false;
				}
			}
    		
    		//rec: .  (received thing here is a dot)
		
		    //find a way to locate the largest server type ans serverID
		
		    //do the SCHD

    		
    		out.write( ("QUIT\n").getBytes() );
    		str = in.readLine();
    		print(str);

    		in.close();
    		out.close();
    		s.close();
    		
    	}
    	catch(IOException e){ e.printStackTrace(); System.exit(1); }
 	}

	public static void print(String str) {
		System.out.println(str);
	}
}
         