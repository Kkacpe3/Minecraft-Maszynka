package xyz.minecraftpasswordhack.utils;

import java.io.File;
import java.util.*;

import xyz.minecraftpasswordhack.Main;

public class PasswordUtil {

	private static final List<String> passwords = new ArrayList<>();
	private static String currentPassword = "";
	private static int nextPassword = 0;
	
	public static void loadPasswordFromFile() {
		try {
			Scanner s = new Scanner(new File("Minecraft Password Hack", "passwords.txt"));
			while (s.hasNext()) {
				passwords.add(s.next());
			}
			s.close();
		} catch (Exception e) {
			Utils.handleError(e.toString());
		}
		if (passwords.size() <= 0) {
			Utils.handleError("Brak hasel!");
		}
		Main.getLogger().info("### Zaladowano " + passwords.size() + " hasel! ###");
	}
	
	public static String getCurrentPassword() {
		return currentPassword;
	}

	public static String getPasswordCommand() {
		String password = "";
		String str = "";
		try {
			password = passwords.get(nextPassword);
			currentPassword = passwords.get(nextPassword);
			str = Main.getConfig().getLoginCommand() + " " + password;
			nextPassword++;
		} catch (IndexOutOfBoundsException e) {
			str = "KONIEC_TEGO";
		}
		return str;
	}
}