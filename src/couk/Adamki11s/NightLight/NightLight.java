package couk.Adamki11s.NightLight;

import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class NightLight extends JavaPlugin {

	String prefix = "[NightLight]";
	Logger log = Logger.getLogger("Minecraft");
	PluginDescriptionFile pdf;
	String version;
	
	public static HashMap<Player, Boolean> pluginEnabled = new HashMap<Player, Boolean>();
	
	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
		log.info(String.format("%s NightLight version %s disabled successfully.", prefix, version));
	}

	@Override
	public void onEnable() {
		// TODO Auto-generated method stub
		pdf = this.getDescription();
		version = pdf.getVersion();
		log.info(String.format("%s NightLight version %s enabled successfully.", prefix, version));
		this.getServer().getPluginManager().registerEvent(Event.Type.PLAYER_MOVE, new NightLightPlayerListener(), Event.Priority.Normal, this);
		this.getServer().getPluginManager().registerEvent(Event.Type.PLAYER_JOIN, new NightLightPlayerListener(), Event.Priority.Normal, this);
		this.getServer().getPluginManager().registerEvent(Event.Type.PLAYER_QUIT, new NightLightPlayerListener(), Event.Priority.Normal, this);
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(label.equalsIgnoreCase("nightlight") || label.equalsIgnoreCase("nl")){
			toggle((Player)sender);
			return true;
		}
		return false;
	}
	
	private void toggle(Player p){
		if(!pluginEnabled.get(p)){
			pluginEnabled.put(p, true);
			p.sendMessage(ChatColor.YELLOW + "NightLight enabled. Use /nl or /nightlight to toggle.");
		} else {
			pluginEnabled.put(p, false);
			p.sendMessage(ChatColor.YELLOW + "NightLight disabled. Use /nl or /nightlight to toggle.");
			Location l = p.getLocation();l.setY(l.getY() - 1);
			p.getWorld().getBlockAt(l).setTypeId(NightLightPlayerListener.previousBlock.get(p));
		}
	}

}
