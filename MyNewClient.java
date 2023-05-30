import java.io.*;
import java.net.*;

import org.w3c.dom.TypeInfo;

import java.net.Socket;
import java.net.InetAddress;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.io.InputStreamReader;
import java.io.BufferedReader;

public class MyNewClient {
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

            int jobID = 0;
            int serverCount = 0;
            int coreCount = 0;
            String largestType = null;

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
                boolean inialData = false;

                while (true) {
                    boolean flag = false;
                    out.flush();
                    out.write(("REDY\n").getBytes());
                    str = in.readLine();
                    // print(str + "-----70");
                    String[] jobInfoArray = str.split(" ");// *get jobInfo
                    if (jobInfoArray[0].equals("NONE"))
                        break; // for NONE
                    else if (jobInfoArray[0].equals("JCPL"))
                        continue; // for JCPL

                    else { // for JOBN
                           // String[] jobInfo = str.split(" ");

                        jobID = Integer.parseInt(jobInfoArray[2]);
                        // print(jobID+"----72");

                        LinkedList<String> iniaLinkedList = new LinkedList<>();
                        if (!inialData) {
                            out.write(("GETS All\n").getBytes());
                            String serverData = "";
                            serverData = in.readLine(); // DATA X Y
                            print(serverData);
                            String[] serverDataArray = serverData.split(" ");

                            out.write(("OK\n").getBytes());
                            String[] servers = new String[Integer.parseInt(serverDataArray[1])];

                            for (int i = 0; i < Integer.parseInt(serverDataArray[1]); i++) {
                                String serverInfo = in.readLine();
                                iniaLinkedList.add(serverInfo);
                            }

                            out.write(("OK\n").getBytes());
                            str = in.readLine();
                            inialData = true;
                        }

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
                            int miniFit = Integer.MAX_VALUE;
                            for (String server : servers) { // Finding a server

                                String[] serverInfoAarry = server.split(" ");
                                if (Integer.parseInt(serverInfoAarry[4]) >= Integer.parseInt(jobInfoArray[4]) // Findind
                                                                                                              // minimun
                                                                                                              // value
                                        && Integer.parseInt(serverInfoAarry[5]) >= Integer.parseInt(jobInfoArray[5])
                                        && Integer.parseInt(serverInfoAarry[6]) >= Integer.parseInt(jobInfoArray[6])) {
                                    int z = Integer.parseInt(serverInfoAarry[4]) - Integer.parseInt(jobInfoArray[4]);
                                    if (z <= miniFit) {
                                        if (!serverInfoAarry[2].equals("unavailable")
                                                && (Integer.parseInt(serverInfoAarry[7]) == 0
                                                        || Integer.parseInt(serverInfoAarry[8]) == 0)) {
                                            z = miniFit;
                                        }
                                    }
                                }
                            }

                            for (String server : servers) {

                                String[] serverInfoAarry = server.split(" ");
                                if (Integer.parseInt(serverInfoAarry[4]) >= Integer.parseInt(jobInfoArray[4])// Finding
                                                                                                             // a
                                                                                                             // suitable
                                                                                                             // server
                                        && Integer.parseInt(serverInfoAarry[5]) >= Integer.parseInt(jobInfoArray[5])
                                        && Integer.parseInt(serverInfoAarry[6]) >= Integer.parseInt(jobInfoArray[6])) {
                                    int z = Integer.parseInt(serverInfoAarry[4]) - Integer.parseInt(jobInfoArray[4]);
                                    if (z <= miniFit) {
                                        if (!serverInfoAarry[2].equals("unavailable")
                                                && (Integer.parseInt(serverInfoAarry[7]) == 0
                                                        || Integer.parseInt(serverInfoAarry[8]) == 0)) {
                                            out.write(("SCHD " + jobID + " " + serverInfoAarry[0] + " "
                                                    + serverInfoAarry[1] + "\n").getBytes());
                                            flag = true;

                                            break;
                                        }
                                    }
                                }
                            }

                            print("----122" + flag);

                            if (flag == false) { // Server not find
                                for (String server : servers) {
                                    String[] serverInfoAarry = server.split(" ");
                                    int z = Integer.parseInt(serverInfoAarry[4]) - Integer.parseInt(jobInfoArray[4]);
                                    if (z <= miniFit) {
                                        if (!serverInfoAarry[2].equals("unavailable")
                                                && (Integer.parseInt(serverInfoAarry[7]) == 0
                                                        || Integer.parseInt(serverInfoAarry[8]) == 0)) {
                                            z = miniFit;
                                        }

                                    }

                                }

                                for (String server : servers) {

                                    String[] serverInfoAarry = server.split(" ");
                                    if (serverInfoAarry[2].equals("active") || serverInfoAarry[2].equals("booting")) {
                                        int z = Integer.parseInt(serverInfoAarry[4])
                                                - Integer.parseInt(jobInfoArray[4]);
                                        if (z <= miniFit) {

                                            out.write(("SCHD " + jobID + " " + serverInfoAarry[0] + " "
                                                    + serverInfoAarry[1] + "\n").getBytes());

                                            break;

                                        }
                                    }
                                }

                            }
                            // print("----129");

                        }
                        str = in.readLine();
                        // print(str + "-----130");
                    }
                    // print("------132");

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
