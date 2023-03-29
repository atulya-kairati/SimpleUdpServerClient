package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.text.MessageFormat;

public class UdpServer {

    private final byte[] buffer = new byte[1024];
    private final DatagramSocket datagramSocket;

    public UdpServer(DatagramSocket datagramSocket) {
        this.datagramSocket = datagramSocket;
    }

    public void receiveThenSend() {

        // We want to continuously listen for incoming messages we need an infinite loop

        while (true) {
            try {
                // Create a packet to receive data into
                final DatagramPacket incomingPacket = new DatagramPacket(buffer, buffer.length);

                System.out.println("Server listening...");
                this.datagramSocket.receive(incomingPacket);  // blocking function

                // Retrieve information received form the client
                final InetAddress clientIp = incomingPacket.getAddress();
                final Integer clientPort = incomingPacket.getPort();
                final String msgFromClient = new String(incomingPacket.getData(), 0, incomingPacket.getLength());

                // log
                System.out.println(MessageFormat.format("Msg from client({0}:{1}) -> {2}", clientIp, clientPort.toString(), msgFromClient));

                // Since we aren't modifying the buffer we can directly send it by wrapping it into DatagramPacket
                final DatagramPacket outgoingPacket = new DatagramPacket(buffer, buffer.length, clientIp, clientPort);
                // send packet back to the client
                this.datagramSocket.send(outgoingPacket);

            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public static void main(String[] args) {

        try {

            DatagramSocket datagramSocket = new DatagramSocket(6969);
            UdpServer server = new UdpServer(datagramSocket);
            server.receiveThenSend();

        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
}
