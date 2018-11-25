package xyz.minecraftpasswordhack.objects;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

import xyz.minecraftpasswordhack.utils.Utils;

public class Config {

	private String ip;
	private int port;
	private String nick;
	private String loginCommand;
	private String loginSucessMessage;
	private int commandSentTime;
	
	public Config() { }
	
	public void loadConfiguration() {
		this.scanAll();
		try {
			Scanner s = new Scanner(new File("Minecraft Password Hack", "config.yml"));
			while (s.hasNext()) {
				String[] args = s.next().split("=");
				args[0] = args[0].toLowerCase();
				
				if (args[0].equals("hostname")) {
					this.ip = args[1];
				} else if (args[0].equals("port")) {
					this.port = Integer.parseInt(args[1]);
				} else if (args[0].equals("username")) {
					this.nick = args[1];
				} else if (args[0].equals("login-command")) {
					this.loginCommand = args[1];
				} else if (args[0].equals("login-success-message")) {
					this.loginSucessMessage = args[1];
				} else if (args[0].equals("command-send-time")) {
					this.commandSentTime = Integer.parseInt(args[1]);
				}
			}
			s.close();
		} catch (Exception e) {
			Utils.handleError(e.toString());
		}
	}
	
	public int getCommandSentTime() {
		return commandSentTime;
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

	public String getLoginCommand() {
		return loginCommand;
	}

	public String getLoginSucessMessage() {
		return loginSucessMessage;
	}

	private void scanAll() {
		File dir = new File("Minecraft Password Hack");
		if (!dir.exists()) make(0);
		File cfg = new File("Minecraft Password Hack", "config.yml");
		if (!cfg.exists()) make(1);
		File list = new File("Minecraft Password Hack", "passwords.txt");
		if (!list.exists()) make(2);
		File log = new File("Minecraft Password Hack", "logs.txt");
		if (!log.exists()) make(3);
	}
	
	private void make(int i) {
		try {
			if (i == 0) { //Minecraft Password Hack directory
				new File("Minecraft Password Hack").mkdirs();
			} else if (i == 1) { //config.yml file
				File f = new File("Minecraft Password Hack", "config.yml");
				f.createNewFile();
				FileWriter fw = new FileWriter(f);
				String newLine = System.lineSeparator();
				fw.write("hostname=127.0.0.1" + newLine);
				fw.write("port=25565" + newLine);
				fw.write("username=gucciMatix" + newLine);
				fw.write("login-command=/login" + newLine);
				fw.write("login-success-message=Zostales zalogowany!" + newLine);
				fw.write("command-send-time=1500");
				fw.close();
			} else if (i == 2) { //passwords.txt file
				new File("Minecraft Password Hack", "passwords.txt").createNewFile();
			} else if (i == 3) { //logs.txt file
				new File("Minecraft Password Hack", "logs.txt").createNewFile();
			}
		} catch (Exception e) {
			Utils.handleError(e.toString());
		}
	}
	
	public void writeToLogs(String str) {
		try {
			File f = new File("Minecraft Password Hack", "logs.txt");
			FileWriter fw = new FileWriter(f);
			fw.write(str);
			fw.close();
		} catch (Exception e) {
			Utils.handleError(e.toString());
		}
	}
}