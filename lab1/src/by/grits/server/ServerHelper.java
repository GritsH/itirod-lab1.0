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

public class ServerHelper {
    public Request readFromSocket(DatagramSocket socket) {
        byte[] buff = new byte[256];
        DatagramPacket packet = new DatagramPacket(buff, buff.length);
        try {
            socket.receive(packet);
        } catch (IOException e) {
            System.out.println("Smt wrong");
            e.printStackTrace();
        }
        String body = new String(packet.getData(), 0, packet.getLength());
        return new Request(body, packet.getAddress(), packet.getPort());
    }

    public boolean create(Request request, GroupService groupService) {
        String[] requestParts = request.getBody().split(":");
        String groupId = requestParts[1];
        if (groupService.findGroupById(groupId) == null) {
            User user = new User("", request.getFromPort(), request.getFromAddress());
            groupService.addGroup(new Group(user, groupId));
            groupService.addUserToGroup(groupId, user);
            return true;
        }
        return false;
    }

    public void sendRequestApprove(Request request, GroupService groupService, DatagramSocket socket) throws IOException {
        String[] requestParts = request.getBody().split(":");
        String groupId = requestParts[1];
        if (groupService.findGroupById(groupId) != null) {
            byte[] buff = (Commands.requestApprove + ":" + groupId).getBytes();
            User groupCreator = groupService.getGroupCreator(groupId);
            InetAddress fromAddress = groupCreator.getAddress();
            int fromPort = groupCreator.getPort();
            DatagramPacket packet = new DatagramPacket(buff, buff.length, fromAddress, fromPort);
            socket.send(packet);
            User user = new User("", request.getFromPort(), request.getFromAddress());
            groupService.addUserToWaitingList(user, groupId);
        }
    }

    public void receiveApproveResult(Request request, GroupService groupService, DatagramSocket socket) throws IOException {
        String[] requestParts = request.getBody().split(":");
        String acceptance = requestParts[1];
        String groupId = requestParts[2];
        User user = groupService.getUserFromWaitingList(groupId);
        String response = "";
        if ("yes".equals(acceptance)) {
            groupService.addUserToGroup(groupId, user);
            groupService.removeUserFromWaitingList(user, groupId);
            response = "approved";
        } else {
            response = "declined";
        }
        byte[] buff = (Commands.joinGroup + ":" + response + ":" +groupId).getBytes();
        InetAddress userAddress = user.getAddress();
        int userPort = user.getPort();
        DatagramPacket packet = new DatagramPacket(buff, buff.length, userAddress, userPort);
        socket.send(packet);
    }
}
