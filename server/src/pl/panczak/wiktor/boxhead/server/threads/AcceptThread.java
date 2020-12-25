package pl.panczak.wiktor.boxhead.server.threads;

import pl.panczak.wiktor.boxhead.server.Launcher;
import java.net.*;
import java.util.LinkedList;
import java.io.DataOutputStream;

public class AcceptThread extends Thread{
    public volatile LinkedList<DataOutputStream> outputs = new LinkedList<>();
    public volatile LinkedList<ReceiveThread> receiveThreads = new LinkedList<>();

    public void run(){
        try{
            ServerSocket server = new ServerSocket(6432);
            while(Launcher.running){
                Socket socket = server.accept();
                System.out.println("new client connected!");
                DataOutputStream output = new DataOutputStream(socket.getOutputStream());
                outputs.add(output);
                output.writeUTF(Launcher.world.toString());
                ReceiveThread receiveThread = new ReceiveThread(socket);
                receiveThreads.add(receiveThread);
                receiveThread.start();
            }
        }catch (Exception e){
            Launcher.running = false;
            e.printStackTrace();
        }
    }
}
