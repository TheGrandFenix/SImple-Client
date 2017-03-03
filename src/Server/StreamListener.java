package Server;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class StreamListener implements Runnable {
    //Define variables
    private ObjectInputStream in;
    private ClientCore clientProcess;

    StreamListener(ClientCore clientProcess, ObjectInputStream in) {
        //Initialize core class pointer and input stream
        this.clientProcess = clientProcess;
        this.in = in;
    }

    public void run() {
        //Listen for incoming messages
        listen();
    }

    private void listen() {
        while (clientProcess.running) {
            try {
                //Try to stream incoming message to console
                clientProcess.streamIn((Message) in.readObject());
            } catch (IOException | ClassNotFoundException e) {
                //Catch and output exceptions when reading from stream
                System.err.println("Server connection failed...");
                clientProcess.closeStreams();
                clientProcess.closeSocket();
                System.exit(0);
            }
        }
    }
}
