package by.grits.client;

import by.grits.Commands;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class ClientHelper {

    public String receivePacketFromServer(DatagramSocket socket) throws IOException {
        byte[] buff = new byte[512];
        DatagramPacket packet = new DatagramPacket(buff, buff.length);
        socket.receive(packet);
        return new String(packet.getData(), 0, packet.getLength());
    }

    public void sendPacketToServer(byte[] buf, DatagramSocket socket, InetAddress address, int port) throws IOException {
        socket.send(new DatagramPacket(buf, buf.length, address, port));
    }

    public void join(Scanner scanner, DatagramSocket socket, InetAddress address, int port) throws IOException {
        System.out.println("Enter group name: ");
        String inputGroupName = scanner.nextLine();
        byte[] buff = ("join:" + inputGroupName).getBytes();
        sendPacketToServer(buff, socket, address, port);
    }

    public void approveRequest(String[] requestParts, Scanner scanner, DatagramSocket socket, InetAddress address, int port) throws IOException {
        System.out.println("Join request to group: " + requestParts[1]);
        System.out.println("Enter yes/no: ");
        String response = scanner.nextLine();
        byte[] sendResponse = (Commands.approveResult + ":" + response + ":" + requestParts[1]).getBytes();
        sendPacketToServer(sendResponse, socket, address, port);
    }

    public void create(Scanner scanner, DatagramSocket socket, InetAddress address, int port) throws IOException {
        System.out.println("Enter id: ");
        String id = scanner.nextLine();
        byte[] buff = ("create:" + id).getBytes();
        sendPacketToServer(buff, socket, address, port);
    }

    public void startMessaging(Scanner scanner, DatagramSocket socket, InetAddress address, int port, String id) {
        Thread messaging = new Thread(() -> {
            while (true) {
//                System.out.println("Your massage: ");
                String message = scanner.nextLine();
                byte[] buff = (Commands.sendGroupMessage + ":" + message + ":" + id).getBytes();
                try {
                    sendPacketToServer(buff, socket, address, port);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        messaging.start();
    }

}
