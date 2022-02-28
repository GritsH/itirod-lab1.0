package by.grits.client;

import by.grits.Commands;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client extends Thread {
    private DatagramSocket socket;
    private InetAddress address;
    private int port;
    private String groupId;
    private ClientHelper clientHelper;

    public Client(int port) throws Exception {
        this.port = port;
        this.address = InetAddress.getByName("localhost");
        this.socket = new DatagramSocket();
        this.clientHelper = new ClientHelper();
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void startClient() throws IOException {
        Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);
        System.out.println("1.Create group\n2.Join group");
        String choice = scanner.nextLine();
        if (choice.equals("1")) {
            clientHelper.create(scanner, socket, address, port);
        }
        if (choice.equals("2")) {
            clientHelper.join(scanner, socket, address, port);
        }
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);
        while (true) {
            if (socket != null) {
                try {
                    String received = clientHelper.receivePacketFromServer(socket);
                    String[] requestParts = received.split(":");

                    if (Commands.requestApprove.equals(requestParts[0])) {
                        clientHelper.approveRequest(requestParts, scanner, socket, address, port);
                    }
                    if (Commands.joinGroup.equals(requestParts[0])) {
                        setGroupId(requestParts[2]);
                        System.out.println(received);
                    }
                    if (Commands.startMessaging.equals(requestParts[0])) {
                        clientHelper.startMessaging(scanner, socket, address, port, getGroupId());
                    }
                    if (Commands.sendGroupMessage.equals(requestParts[0])) {
                        System.out.println("Group message: " + requestParts[1]);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
