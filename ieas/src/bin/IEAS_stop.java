package bin;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

// 실행중인 IEAS 기능들을 종료합니다.
public class IEAS_stop {
	public static void main(String[] args) {
		try {
			DatagramSocket client = new DatagramSocket();
			InetAddress addr = InetAddress.getByName("127.0.0.1");
			
			byte[] buffer = IEAS_start.STOP_COMMAND.getBytes();
			DatagramPacket packet = new DatagramPacket(buffer, 0, buffer.length, 
					addr, IEAS_start.IEAS_PORT);
			client.send(packet);
			client.close();
			
			System.out.println("Stop Completed.");
		}
		catch(Exception e) {
			System.out.println("Stop Failed.");
		}
		
	}
}
