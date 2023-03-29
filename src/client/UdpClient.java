package client;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class UdpClient {

    private final DatagramSocket datagramSocket;
    private final InetAddress inetAddress;


    public UdpClient(DatagramSocket datagramSocket, InetAddress inetAddress) {
        this.datagramSocket = datagramSocket;
        this.inetAddress = inetAddress;
    }

    public void sendThenReceive(String msg) throws IOException {

        final byte[] buffer = msg.getBytes();

        final DatagramPacket packet = new DatagramPacket(buffer, buffer.length, this.inetAddress, 6969); // destination port and ip
        this.datagramSocket.send(packet);

        // After this we reuse the packet var to listen for echo from server
        this.datagramSocket.receive(packet);

        final String msgEcho = new String(packet.getData(), 0, packet.getLength());
        System.out.println("Server echoed: " + msgEcho);
    }

    public static void main(String[] args) throws SocketException, UnknownHostException {

        // Here we use different port than server's port
        final int clientPort = 6968;
        final DatagramSocket datagramSocket = new DatagramSocket(clientPort);

        // if we don't mention the port here, a random available port will be selected
//        final DatagramSocket datagramSocket = new DatagramSocket();

        final InetAddress serverIp = InetAddress.getLocalHost(); // since server is local
        final UdpClient client = new UdpClient(datagramSocket, serverIp);

        final Scanner in = new Scanner(System.in);

        while (true) {

            System.out.print("Enter a msg to send to server: ");
            String msg = in.nextLine();

            try {

                client.sendThenReceive(msg);

            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }


    }
}
