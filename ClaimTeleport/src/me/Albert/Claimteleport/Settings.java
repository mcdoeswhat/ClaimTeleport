package me.Albert.Claimteleport;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

public class Settings {
	private File SettingsFile = new File(Main.getInstance().getDataFolder(), "Settings.yml");
	private YamlConfiguration config;
	public String teleportSuccess;
	public String teleporting;
	public String playermoved;
	public String incooldown;
	public String noPermission;
	public String invalidClaimID;
	public String notNumeric;
	public String nullInput;
	public int delay;
	public boolean sound;
	public String invalidplayer;
	public Settings() {
		reloadSettings();
	}
	
	public  void reloadSettings(){
		if (!this.SettingsFile.exists()) {
			Main.getInstance().saveResource("Settings.yml", true);
		}
		
		this.config = YamlConfiguration.loadConfiguration(this.SettingsFile);
		this.delay =  this.config.getInt("delay");
		this.playermoved = ChatColor.translateAlternateColorCodes('&', this.config.getString("Messages.playermoved"));
		this.incooldown = ChatColor.translateAlternateColorCodes('&', this.config.getString("Messages.incooldown"));
		this.teleporting = ChatColor.translateAlternateColorCodes('&', this.config.getString("Messages.Teleporting"));
		this.teleportSuccess = ChatColor.translateAlternateColorCodes('&', this.config.getString("Messages.TeleportSuccess"));
		this.noPermission = ChatColor.translateAlternateColorCodes('&', this.config.getString("Messages.NoPermission"));
		this.invalidClaimID = ChatColor.translateAlternateColorCodes('&', this.config.getString("Messages.InvalidClaimID"));
		this.notNumeric = ChatColor.translateAlternateColorCodes('&', this.config.getString("Messages.NotNumeric"));
		this.nullInput = ChatColor.translateAlternateColorCodes('&', this.config.getString("Messages.NullInput"));
		this.sound =  this.config.getBoolean("Sound.Enabled");
		this.invalidplayer = ChatColor.translateAlternateColorCodes('&', this.config.getString("Messages.InvalidPlayer"));
	
}
}