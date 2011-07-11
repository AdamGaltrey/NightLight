package couk.Adamki11s.NightLight;

import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class NightLightPlayerListener extends PlayerListener {
	
	public static HashMap<Player, Location> previousLoc = new HashMap<Player, Location>();
	public static HashMap<Player, Integer> previousBlock = new HashMap<Player, Integer>();
	public static HashMap<Player, Byte> previousBlockData = new HashMap<Player, Byte>();
	public static HashMap<Player, Sign> signData = new HashMap<Player, Sign>();
	
	public void onPlayerJoin(PlayerJoinEvent evt){
		NightLight.pluginEnabled.put(evt.getPlayer(), false);
	}
	
	public void onPlayerQuit(PlayerQuitEvent evt){
		Player p = evt.getPlayer();
		Location l = p.getLocation();
		l.setY(l.getY() - 1);
		if(l.getWorld().getBlockAt(l).getType() == Material.GLOWSTONE){
			if(NightLight.pluginEnabled.get(p)){
				l.getWorld().getBlockAt(l).setTypeId(previousBlock.get(p));
			}
		}
	}

	public void onPlayerMove(PlayerMoveEvent evt){
		
		if(NightLight.pluginEnabled.get(evt.getPlayer())){
			
				Location loc = evt.getPlayer().getLocation();
			
				Player p = evt.getPlayer();
				World w = p.getWorld();
				
				loc.setY(loc.getY() - 1);
				
				if(previousLoc.containsKey(p)){
					if(!signData.containsKey(p)){
						p.sendBlockChange(previousLoc.get(p), previousBlock.get(p), (byte)previousBlockData.get(p));
					} else {
						Sign oldSign = signData.get(p);
						
						Sign newSign = (Sign) w.getBlockAt(previousLoc.get(p)).getState();
						p.sendBlockChange(previousLoc.get(p), previousBlock.get(p), (byte)previousBlockData.get(p));
						for(int i = 0; i <= 3; i++){
							newSign.setLine(i, oldSign.getLine(i));newSign.update();
						}
						signData.remove(p);
					}
				}		
				
				
				if(w.getBlockAt(loc).getType() != Material.AIR){
					
					if(w.getBlockAt(loc).getTypeId() == 323 || w.getBlockAt(loc).getTypeId() == 68 || w.getBlockAt(loc).getType() == Material.SIGN || w.getBlockAt(loc).getType() == Material.SIGN_POST){
						signData.put(p, (Sign)w.getBlockAt(loc).getState());
					}
						
					previousBlock.put(p, w.getBlockAt(loc).getTypeId());
					previousBlockData.put(p, w.getBlockAt(loc).getData());
					previousLoc.put(p, loc);
					p.sendBlockChange(loc, Material.GLOWSTONE, (byte)0);
				}
		}
	}
	
}
