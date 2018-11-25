package xyz.minecraftpasswordhack.utils;

import xyz.minecraftpasswordhack.Main;

public class Utils {

	public static void handleError(String t) {
		Main.getLogger().info(t);
		System.exit(0);
	}
}