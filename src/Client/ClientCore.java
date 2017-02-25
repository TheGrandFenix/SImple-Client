package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientCore implements Runnable {
    //Define networking variables
    private Socket socket;
    private StreamListener listener;
    private DataInputStream in;
    private DataOutputStream out;

    //Define console input
    private Scanner console;

    //Define running variable
    volatile boolean running = true;

    ClientCore(String address, int port){
        //Start console input
        console = new Scanner(System.in);

        //Initialize socket variable and connect to given address and port
        socket = startSocket(address, port);

        //Start data streams
        startStreams();

        //Start listener
        listener = new StreamListener(this, in);
        new Thread(listener).start();

        //Output successful connection status
        System.out.println("Successfully connected to " + socket.getRemoteSocketAddress() + "...");
    }

    public void run() {
        //Start streaming messages from console to server
        streamOut();
    }

    private void streamOut() {
        while (running) {
            //Send console input to server
            outputConsoleNextLine();
        }
    }

    void streamIn(String input) {
        //Display incoming messages
        System.out.println(input);
    }

    private void outputConsoleNextLine() {
        try {
            //Try to send next line from console to out stream
            String msg = console.nextLine();
            if (!msg.equals("")) {
                out.writeUTF(msg);
                out.flush();
            }
        } catch (IOException e) {
            //Catch and output errors when sending message
            System.err.println("Failed to send message from console...");
            e.printStackTrace();
            closeStreams();
            closeSocket();
            System.exit(0);
        }
    }

    private Socket startSocket(String socketAddress, int socketPort) {
        try {
            //Try to create a socket and connect to server with given address and port
            return new Socket(socketAddress, socketPort);
        } catch (IOException e) {
            //Catch and output socket creation exceptions
            System.err.println("Failed to connect to server...");
            e.printStackTrace();
            System.exit(0);
        }
        //Return null if socket creation fails
        return null;
    }

    private void startStreams() {
        try {
            //Try to open streams and initialize in/out variables
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            //Catch and output errors when opening streams
            System.err.println("Failed to start streams...");
            e.printStackTrace();
            System.exit(0);
        }
    }

    void closeSocket() {
        try {
            //Try to close socket
            socket.close();
        } catch (IOException e) {
            //Catch and output errors when closing socket
            System.err.println("Failed to close socket...");
            e.printStackTrace();
            System.exit(0);
        }
    }

    void closeStreams() {
        try {
            in.close();
            out.close();
        } catch (IOException e) {
            System.err.println("Failed to close streams...");
            e.printStackTrace();
            System.exit(0);
        }
    }

}
