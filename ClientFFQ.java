import java.io.*;
import java.net.*;

import org.w3c.dom.TypeInfo;

import java.net.Socket;
import java.net.InetAddress;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.concurrent.TimeUnit;

import javax.lang.model.util.ElementScanner6;
import javax.print.attribute.standard.JobName;

import java.io.InputStreamReader;
import java.io.BufferedReader;

public class ClientFFQ {
    public static void main(String[] args) {
        // while(true){
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

            // BufferedReader bin = new BufferedReader(new
            // InputStreamReader(s.getInputStream() ) );
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            DataOutputStream out = new DataOutputStream(s.getOutputStream());

            out.write(("HELO" + "\n").getBytes());

            String str = in.readLine();
            print(str);

            String username = System.getProperty("user.name");
            out.write(("AUTH " + username + "\n").getBytes());
            str = in.readLine();
            print(str);

            int serverCount = 0;
            int coreCount = 0;
            String largestType = null;
            int jobID = 0;
            /*
             * out.write( ("REDY\n").getBytes() );
             * str = in.readLine();
             * print(str);
             */

            /*
             * useless coding
             * out.write( ("OK\n").getBytes() );
             * str = in.readLine();
             * print(str);
             */

            // loop X times, each time a line of records
            if (str.equals("OK")) {
                try {

                    while (true) {
                        boolean flag = false;
                        out.flush();
                        out.write(("REDY\n").getBytes());
                        str = in.readLine();
                        print(str + "-----143");
                        String[] jobInfoArray = str.split(" ");// *get jobInfo
                        if (jobInfoArray[0].equals("NONE"))
                            break;
                        // for NONE
                        else if (jobInfoArray[0].equals("JCPL"))
                            continue; // for JCPL
                        else if (jobInfoArray[0].equals("JOBN") || jobInfoArray[0].equals("JOBP")) { // for JOBN
                            // String[] jobInfo = str.split(" ");

                            jobID = Integer.parseInt(jobInfoArray[2]);
                            print(jobID + "----72");

                            // out.write( ("GETS All\n").getBytes() );
                            out.write(("GETS Capable " + jobInfoArray[4] + " " + jobInfoArray[5] + " " + jobInfoArray[6]
                                    + "\n").getBytes());
                            String serverData = "";
                            serverData = in.readLine(); // DATA X Y
                            print(serverData);
                            String[] serverDataArray = serverData.split(" ");

                            out.write(("OK\n").getBytes());
                            String[] servers = new String[Integer.parseInt(serverDataArray[1])];

                            for (int i = 0; i < Integer.parseInt(serverDataArray[1]); i++) {
                                // if (flag) {
                                String serverInfo = "";
                                serverInfo = in.readLine(); // server information each line
                                // print(serverInfo);
                                servers[i] = serverInfo;

                                // if(Integer.parseInt(serverInfoArray[2]) > coreCount) {

                                // }
                            }

                            // }
                            out.write(("OK\n").getBytes());
                            str = in.readLine();
                            // print(str);
                            if (str.equals(".")) {
                                for (String server : servers) {
                                    String[] serverInfoAarry = server.split(" ");
                                    if (Integer.parseInt(serverInfoAarry[4]) >= Integer.parseInt(jobInfoArray[4])
                                            && Integer.parseInt(serverInfoAarry[5]) >= Integer.parseInt(jobInfoArray[5])
                                            && Integer.parseInt(serverInfoAarry[6]) >= Integer
                                                    .parseInt(jobInfoArray[6])) {
                                        if (!serverInfoAarry[2].equals("unavailable")
                                                && (Integer.parseInt(serverInfoAarry[7]) == 0
                                                        || Integer.parseInt(serverInfoAarry[8]) == 0)) {

                                            out.write(("SCHD " + jobID + " " + serverInfoAarry[0] + " "
                                                    + serverInfoAarry[1]
                                                    + "\n").getBytes());
                                            flag = true;
                                            break;

                                        }
                                    }

                                }
                            }
                            print("----122" + flag);

                            if (flag == false) {
                                // for (String server : servers) {
                                // String[] serverInfoAarry = server.split(" ");
                                // if (serverInfoAarry[2].equals("active") ||
                                // serverInfoAarry[2].equals("booting"))

                                // {
                                // out.write(("SCHD " + jobID + " " + serverInfoAarry[0] + " "
                                // + serverInfoAarry[1] + "\n").getBytes());
                                // break;
                                // }
                                // }
                                out.write(("ENQJ GQ\n").getBytes());
                                str = in.readLine();
                                print(str + "-----145");
                                continue;

                            }
                            print("----129");

                        } else if (jobInfoArray[0].equals("CHKQ")) {
                            out.flush();
                            out.write(("LSTQ GQ #\n").getBytes());
                            int queueJobCount = Integer.parseInt(in.readLine());
                            print(queueJobCount + "-----232");
                            for (int i = 0; i < queueJobCount; i++) {
                                out.write(("DEQJ GQ " + i + "\n").getBytes());
                                str = in.readLine();
                                print(str + "-----235");
                            }
                            continue;
                        }
                        str = in.readLine();
                        print(str + "-----239");

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            }

            // send: OK
            /*
             * out.write( ("OK\n").getBytes() );
             * str = in.readLine();
             * print(str);
             */

            // (str.equals("NONE"))
            // break; // for NONE
            // else if (str.equals("."))
            // continue;
            // else { // for SCHD
            // String[] jobInfo = str.split(" ");

            // jobID = Integer.parseInt(jobInfo[2]);

            // out.write(("SCHD\n").getBytes());
            // str = in.readLine(); // DATA X Y
            // String[] serverInfoList = str.split(" ");

            // out.write(("OK\n").getBytes());

            // for (int i = 0; i < Integer.parseInt(serverInfoList[1]); i++) {
            // if (flag) {
            // str = in.readLine(); // server information each line
            // print(str);

            // if (Integer.parseInt(serverInfoList[2]) > coreCount) {

            // }
            // }
            // }
            // flag = false;
            // }
            // }

            // rec: . (received thing here is a dot)

            // find a way to locate the largest server type ans serverID

            // do the SCHD

            out.write(("QUIT\n").getBytes());
            str = in.readLine();
            print(str);

            in.close();
            out.close();
            s.close();

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void print(String str) {
        System.out.println(str);
    }
}
