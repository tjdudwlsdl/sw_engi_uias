package bin;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Timer;

import javax.management.monitor.MonitorSettingException;

import environment_data_analyzer.AnalysisTask;
import environment_data_collector.WeatherCollectionTask;

// IEAS 기능들을 실행합니다.
public class IEAS_start {
	public static final int IEAS_PORT = 61000;
	public static final String STOP_COMMAND = "stop_ieas";
	
	private static DatagramSocket isRun;
	
	private static void monitoring() throws MonitorSettingException {
		try {
			isRun = new DatagramSocket(IEAS_PORT);
		} catch(SocketException e) {
			throw new MonitorSettingException();
		}
	}
	
	private static void close() {
		if(isRun != null) {
			isRun.close();
		}
	}
	
	public static void main(String[] args) {
		// 중복 실행 방지
		try {
			monitoring();
		} catch(MonitorSettingException e) {
			System.out.println("Already ieas started.");
			System.exit(0);
		}
		
		Timer weatherCollect = new Timer();
		Timer analyzer = new Timer();
		
		weatherCollect.schedule(new WeatherCollectionTask(), 0, WeatherCollectionTask.INTERVAL);
		analyzer.schedule(new AnalysisTask(), AnalysisTask.INTERVAL);
		
		System.out.println("ieas start.");
		// 종료 명령 대기
		try {
			byte[] buffer = new byte[256];
			DatagramPacket packet = new DatagramPacket(buffer, 256);
			
			while(true) {
				isRun.receive(packet);
				int dataLength = packet.getLength();
				
				String data = new String(packet.getData(), 0, dataLength);
				if(data.equals(STOP_COMMAND)) {
					break;
				}
			}
		} 
		catch(Exception e) {
			// ?
		} 
		finally {
			try {
				weatherCollect.cancel();
				analyzer.cancel();
				close();
			} catch(Exception ignore) { }
		}
		
		
	}
}
