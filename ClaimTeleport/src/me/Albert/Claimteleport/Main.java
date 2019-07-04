package me.Albert.Claimteleport;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import me.ryanhamshire.GriefPrevention.GriefPrevention;

public class Main extends org.bukkit.plugin.java.JavaPlugin implements Listener {
	private static Settings settings;
	private static SoundManager soundmanager;
	private static Main instance;
	private static Essentials ess;
	public void onEnable(){	
		new Metrics(this);	
		
		Plugin griefPreventionPlugin = Bukkit.getPluginManager().getPlugin("GriefPrevention");
		ess = (Essentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		if ((griefPreventionPlugin == null)) {
			 Bukkit.getConsoleSender().sendMessage("§cGriefPrevention not found!ClaimTeleport Disabled!");
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}
		else {
		org.bukkit.Bukkit.getConsoleSender().sendMessage("§a[ClaimTeleport] Loaded");
		instance = this;
		settings = new Settings();
		soundmanager = new SoundManager();
		
		}
		
	}
	public static Settings getSettings() {
		return settings;
		
	}
	public static Main getInstance() {
		return instance;
	}
	
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if ( cmd.getName().equalsIgnoreCase("ctpreload")) {
			settings.reloadSettings();
			sender.sendMessage("§aConfig Reloaded");
			return true;
		} 
		 if (sender instanceof Player) {		 
		// TODO Auto-generated method stub    
		Player p = (Player)sender;
		UUID uuid = p.getUniqueId();
		if(p.hasPermission("ctp.use") == true) {
		if (args.length==1) {
		if (Utils.isNumeric(args[0]) == true) {
		int input = Integer.parseInt(args[0])-1;
		int range = GriefPrevention.instance.dataStore.getPlayerData(uuid).getClaims().size();
		 if ( cmd.getName().equalsIgnoreCase("claimteleport") && input<range  && input >=0){
			 if (args[0] != null) { 
				teleport(p,input,p.getUniqueId());
		 }
			 
		 }  if (input>=range || input<0) {
			 p.sendMessage(settings.invalidClaimID);
			 soundmanager.playSound(sender, Sound.ENTITY_VILLAGER_NO, 10.0F, 1.1F);
		 }
		}
	} if (args.length==2 && Utils.isNumeric(args[1]) == true && p.hasPermission("ctp.other")) {
		
		int id = Integer.parseInt(args[1])-1;
		User player = null;
		try {
            UUID uuid1 = UUID.fromString(args[0]);
            player = ess.getUser(uuid1);
        }catch (IllegalArgumentException ignored) { // Thrown if invalid UUID from string, check by name.
            player = ess.getOfflineUser(args[0]);
        } 
		
		OfflinePlayer target = null;
		UUID uuid2 = null;
		int range = 0;
		 if (player!=null) {
			 target = Bukkit.getOfflinePlayer(player.getName());
			 uuid2 = target.getUniqueId();
			range = GriefPrevention.instance.dataStore.getPlayerData(uuid2).getClaims().size();
		 
		if (target.hasPlayedBefore()==true && id<range && id>=0) {
		teleport(p,id,uuid2);
	}
		 }
		if (player==null) {
			p.sendMessage(settings.invalidplayer);
			soundmanager.playSound(sender, Sounds.VILLAGER_NO.bukkitSound(), 10.0F, 1.1F);
		}
		if (id>=range && player!=null || id<0) {			
			p.sendMessage(settings.invalidClaimID);
			soundmanager.playSound(sender, Sounds.VILLAGER_NO.bukkitSound(), 10.0F, 1.1F);
		}
	}
	if (args.length==0 || args.length>2) {
			p.sendMessage(settings.nullInput);
			soundmanager.playSound(sender, Sounds.VILLAGER_NO.bukkitSound(), 10.0F, 1.1F);
			
		}
	if ( args.length==2 && !Utils.isNumeric(args[1]) || args.length==1 && !Utils.isNumeric(args[0]) 
			) {
		p.sendMessage(settings.notNumeric);
		soundmanager.playSound(sender, Sounds.VILLAGER_NO.bukkitSound(), 10.0F, 1.1F);
	}
		if (!p.hasPermission("ctp.other") && args.length==2 && Utils.isNumeric(args[1]) == true) {
			p.sendMessage(settings.noPermission);
			soundmanager.playSound(sender, Sounds.VILLAGER_NO.bukkitSound(), 10.0F, 1.1F);
		}
	} if (!p.hasPermission("ctp.use")) {
			p.sendMessage(settings.noPermission);
			soundmanager.playSound(sender, Sounds.VILLAGER_NO.bukkitSound(), 10.0F, 1.1F);
		}
		 } else {
			 sender.sendMessage("§cOnly ingame players can excute this command");
		 }
		 
		 
		return true;
		 
	}
	
	public HashMap<String, Long> cooldowns = new HashMap<String, Long>();
	
	public void teleport(Player sender,int claimid,UUID uuid) {
		Location a =sender.getLocation();
		Location claimloc1 = GriefPrevention.instance.dataStore.getPlayerData(uuid).getClaims().get(claimid).getGreaterBoundaryCorner();
		Location claimloc2 = GriefPrevention.instance.dataStore.getPlayerData(uuid).getClaims().get(claimid).getLesserBoundaryCorner();
		int x1 = claimloc1.getBlockX();
		int z1 = claimloc1.getBlockZ();
		int x2 = claimloc2.getBlockX();
		int z2 = claimloc2.getBlockZ();
		int x = (x1-x2)/2+x2;
		int z = (z1-z2)/2+z2;
		int y = sender.getWorld().getHighestBlockAt(x,z).getY()+1;
		World w = claimloc1.getWorld();
		Location safe = new Location(w,x+0.5,y,z+0.5);
		if (sender.hasPermission("ctp.cooldownbypass")) {
			sender.teleport(safe);
			teleportsound(sender);
			sender.sendMessage(settings.teleportSuccess);
			
		} else
		if(cooldowns.containsKey(sender.getName())) {
            long secondsLeft = ((cooldowns.get(sender.getName())/1000)+settings.delay) - (System.currentTimeMillis()/1000);
            if(secondsLeft>0) {
                // Still cooling down
                sender.sendMessage(settings.incooldown);
            } else {
            	cooldowns.remove(sender.getName());
            }
        } else {
        cooldowns.put(sender.getName(), System.currentTimeMillis());
		if (settings.delay > 0) {
			sender.sendMessage(settings.teleporting.replace("%s%", String.valueOf(settings.delay)));
			
		}
		getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			public void run() {
		    if (sender.getLocation().getWorld() == a.getWorld()) {
		    	if (sender.getLocation().distanceSquared(a) < 2) {
		    cooldowns.remove(sender.getName());
			sender.teleport(safe);
			teleportsound(sender);
			sender.sendMessage(settings.teleportSuccess);
		    	}
		    	else {
					sender.sendMessage(settings.playermoved);
					cooldowns.remove(sender.getName());
				}
			} else {
				sender.sendMessage(settings.playermoved);
				cooldowns.remove(sender.getName());
			}
			}
			}, 20L * settings.delay);
        }
		
	}
	@SuppressWarnings("deprecation")
	public void teleportsound(Player p) {
		getServer().getScheduler().scheduleAsyncDelayedTask(this, new Runnable() {
			public void run() {
		   soundmanager.playSound(p, Sounds.ENDERMAN_TELEPORT.bukkitSound(), 10.0F, 1.4F);
			}
			}, 2L);
		
	}
	
	
	public void onDisable() {
		settings = null;
		instance = null;
	}

}
