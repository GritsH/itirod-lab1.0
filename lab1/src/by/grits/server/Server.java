package by.grits.server;

import by.grits.Commands;
import by.grits.group.Group;
import by.grits.group.GroupService;
import by.grits.user.Request;
import by.grits.user.User;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;

public class Server extends Thread {
    private final DatagramSocket socket;
    private final GroupService groupService;
    private final ServerHelper serverHelper;

    public Server() throws SocketException {
        this.socket = new DatagramSocket(8888);
        this.groupService = new GroupService();
        this.serverHelper = new ServerHelper();
    }

    @Override
    public void run() {
        boolean run = true;
        while (run) {
            try {
                Request request = serverHelper.readFromSocket(socket);
                String[] requestParts = request.getBody().split(":");
                System.out.println("Received cmd>> " + Arrays.toString(requestParts));

                if (Commands.createGroup.equals(requestParts[0])) {
                    if (serverHelper.create(request, groupService)) {
                        byte[] buff = (Commands.joinGroup + ":" + "approved" + ":" + requestParts[1]).getBytes();
                        InetAddress fromAddress = request.getFromAddress();
                        int fromPort = request.getFromPort();
                        DatagramPacket packet = new DatagramPacket(buff, buff.length, fromAddress, fromPort);
                        socket.send(packet);
                    } else {
                        System.out.println("could not create group");
                    }
                }
                if (Commands.joinGroup.equals(requestParts[0])) {
                    serverHelper.sendRequestApprove(request, groupService, socket);
                }
                if (Commands.approveResult.equals(requestParts[0])) {
                    serverHelper.receiveApproveResult(request, groupService, socket);
                    byte[] buff = Commands.startMessaging.getBytes();
                    Group group = groupService.findGroupById(requestParts[2]);
//                    if(group.getUsers().size()>=2){
//                    }
                    for (User user : group.getUsers()) {
                        DatagramPacket packet = new DatagramPacket(buff, buff.length, user.getAddress(), user.getPort());
                        socket.send(packet);
                    }

                }
                if (Commands.sendGroupMessage.equals(requestParts[0])) {
                    byte[] buff = (Commands.sendGroupMessage + ":" + requestParts[1]).getBytes();
                    Group group = groupService.findGroupById(requestParts[2]);
                    for (User user : group.getUsers()) {
                        DatagramPacket packet = new DatagramPacket(buff, buff.length, user.getAddress(), user.getPort());
                        socket.send(packet);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                run = false;
            }

        }

    }

}
