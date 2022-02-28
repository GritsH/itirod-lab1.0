package by.grits.client;

public class SecondClientRunner {

    public static void main(String[] args) throws Exception {
        Client client = new Client(8888);
        Thread thread = new Thread(client);
        thread.start();
        client.startClient();
    }
}
