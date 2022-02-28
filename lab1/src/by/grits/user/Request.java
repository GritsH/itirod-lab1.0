package by.grits.user;

import java.net.InetAddress;

public class Request {
    private String body;
    private InetAddress fromAddress;
    private int fromPort;

    public Request(String body, InetAddress fromAddress, int fromPort) {
        this.body = body;
        this.fromAddress = fromAddress;
        this.fromPort = fromPort;
    }

    public String getBody() {
        return body;
    }

    public InetAddress getFromAddress() {
        return fromAddress;
    }

    public int getFromPort() {
        return fromPort;
    }
}
