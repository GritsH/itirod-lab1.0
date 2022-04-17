package by.grits.user;

import java.net.InetAddress;

public class User {
    private String name;
    private int port;
    private InetAddress address;

    public User(String name, int port, InetAddress address) {
        this.name = name;
        this.port = port;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public int getPort() {
        return port;
    }

    public InetAddress getAddress() {
        return address;
    }
}
