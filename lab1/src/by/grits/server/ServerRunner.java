package by.grits.server;

import java.io.IOException;

public class ServerRunner extends Thread {

    public static void main(String[] args) throws IOException {
        Thread thread = new Thread(new Server());
        thread.start();
    }
}