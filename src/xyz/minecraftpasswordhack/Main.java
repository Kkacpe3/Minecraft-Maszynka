package xyz.minecraftpasswordhack;

import java.util.logging.Logger;

import xyz.minecraftpasswordhack.objects.Bot;
import xyz.minecraftpasswordhack.objects.Config;
import xyz.minecraftpasswordhack.utils.PasswordUtil;

/*
 * Projekt napisany w celach edukacyjnych.
 * Pokazuj¹cy, ¿e maszynki do "hakowania" jednak moga dzialaæ.
 * Twórca nie odpowiada za szkody wyrz¹dzone t¹ maszynk¹.
 * Twórca: gucciMatix
 */

public class Main {

	private static Logger logger = Logger.getLogger("Minecraft Password Hack");
	private static Config config = new Config();
	
	public static void main(String[] args) {
		config.loadConfiguration();
		PasswordUtil.loadPasswordFromFile();
		new Bot(config.getIp(), config.getPort(), config.getNick()).connect();
		
		while (true) { }
	}
	
	public static Config getConfig() {
		return config;
	}
	
	public static Logger getLogger() {
		return logger;
	}
}