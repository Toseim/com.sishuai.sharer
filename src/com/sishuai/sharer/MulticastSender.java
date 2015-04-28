import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Date;

public class MulticastSender {

	public static void server() throws Exception {
		InetAddress group = InetAddress.getByName("224.0.0.2");// 组播地址
		int port = 8647;
		MulticastSocket mss = null;
		try {
			mss = new MulticastSocket(port);
			mss.joinGroup(group);
			System.out.println("发送数据包启动！（启动时间" + new Date() + ")");

			while (true) {
				String message = "192.168.168.107 " + "hahaha";
				byte[] buffer = message.getBytes();
				DatagramPacket dp = new DatagramPacket(buffer, buffer.length,
						group, port);
				mss.send(dp);
				System.out.println("发送数据包给 " + group + ":" + port);
				Thread.sleep(1000);
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (mss != null) {
					mss.leaveGroup(group);
					mss.close();
				}
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
	}

	public static void main(String[] args) throws Exception {
		server();
	}
}
