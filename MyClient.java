import java.io.*;
import java.net.*;

import org.w3c.dom.TypeInfo;

import java.net.Socket;
import java.net.InetAddress;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.concurrent.TimeUnit;

//import javax.imageio.stream.MemoryCacheImageInputStream;

import java.io.InputStreamReader;
import java.io.BufferedReader;
 
public class MyClient {
    public static void main(String[] args) {
    //while(true){
    	try {
    		Socket s = new Socket("localhost", 50000);
    		
    		
			// Client\u7aef\u5faa\u73af\u5185\u5bb9\uff1a
			// REDY
   			// \u83b7\u53d6JOBN
			// Gets Capable
   			// \u83b7\u53d6 DATA X Y
			// OK
   			// \u83b7\u53d6\u670d\u52a1\u5668\u5217\u8868
			// OK
   			// \u83b7\u53d6\u4e00\u4e2a\u201c.\u201d
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
		int core = 0;
		int memory = 0;
		int disk = 0;
		String serverName = "";
		int serverNum = 0;
		int counter = 0;

		//loop X times, each time a line of records
		while(true){
		
			out.write( ("REDY\n").getBytes() );
			str = in.readLine();
			print(str);
		
			if (str.equals("NONE")) break; // for NONE
			else if (str.startsWith("JCPL")) continue; // for JCPL
			else { // for JOBN
				String[] jobInfo = str.split(" "); 

				jobID = Integer.parseInt(jobInfo[2]);
				core = Integer.parseInt(jobInfo[4]);
				memory = Integer.parseInt(jobInfo[5]);
				disk = Integer.parseInt(jobInfo[6]);

				out.write( ("GETS Capable "+ core + " " + memory + " " + disk + "\n").getBytes() );//find a way to locate the largest server type ans serverID
				str = in.readLine(); // DATA X Y
				String[] serverInfoList = str.split(" "); 
				
				out.write( ("OK\n").getBytes() );
				

				for (int i = 0; i < Integer.parseInt(serverInfoList[1]); i++){
					
					str = in.readLine(); // server information each line
					print(str);
					
					String[] listN = str.split(" ");
					serverName = listN[0];
					
					serverNum = Integer.parseInt(listN[1]);
				
				}

				out.write( ("OK\n").getBytes() );
				str = in.readLine();

				out.write( ("SCHD " + jobID + " " + serverName + " " + (counter% (serverNum+1) ) + "\n").getBytes() );//do the SCHD
				str = in.readLine();
				
				counter++;
				
			}
			
		}

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