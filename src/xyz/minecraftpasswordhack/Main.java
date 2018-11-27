package xyz.minecraftpasswordhack;

import java.util.logging.Logger;

import xyz.minecraftpasswordhack.objects.*;
import xyz.minecraftpasswordhack.utils.PasswordUtil;

/*
 * Projekt napisany w celach edukacyjnych.
 * Pokazujacy, ze maszynki do "hakowania" jednak moga dzialac.
 * Tworca nie odpowiada za szkody wyrzadzone ta maszynki.
 * Tworca: gucciMatix, kod poprawial Unix.
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