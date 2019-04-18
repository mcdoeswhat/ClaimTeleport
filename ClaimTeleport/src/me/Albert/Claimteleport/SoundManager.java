package me.Albert.Claimteleport;

import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SoundManager {

	private static Settings settings;
	
	public void playSound(CommandSender sender, Sound sound, float volume, float pitch) {
		settings = new Settings();
		if (sender instanceof Player) {
			if (settings.sound==true) {
				Player player = (Player) sender;
				player.playSound(player.getLocation(), sound, volume, pitch);
			}
		}
	}
}
