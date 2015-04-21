package mc.adambor.ArenaDS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import mc.alk.arena.BattleArena;
import mc.alk.arena.objects.ArenaPlayer;
import mc.alk.arena.objects.arenas.Arena;
import mc.alk.arena.objects.events.ArenaEventHandler;
import mc.alk.arena.objects.teams.ArenaTeam;
import mc.alk.arena.objects.victoryconditions.VictoryCondition;

public class ArenaDeathSnatch extends Arena {
	
	public static int pointsToWin;
	HashMap<Item, ArenaTeam> items = new HashMap<Item, ArenaTeam>();
	List<Item> itm = new ArrayList<Item>();
	Victory scores;
	BukkitTask eff;
	Random rand;
	
	
	public void onOpen(){
        VictoryCondition vc = getMatch().getVictoryCondition(Victory.class);
        scores = (Victory) (vc != null ? vc : new Victory(getMatch()));
        getMatch().addVictoryCondition(scores);
        scores.getObjective().setDisplayPlayers(false);
        rand = new Random();
	}
	public void onStart(){
		initEff();
        scores.getObjective().setDisplayName(ChatColor.YELLOW+"DeathSnatch");
	}
	private void initEff() {
		eff = new BukkitRunnable(){
			@Override
			public void run() {
				for(Item i:itm){
					if(i != null){
						for(ArenaPlayer ap:match.getPlayers()){
							if(ap.getTeam() == items.get(i)){
								for(int e=0;e<10;e++){
									ap.getPlayer().playEffect(i.getLocation().add(rand.nextDouble()-0.5, rand.nextDouble(), rand.nextDouble()-0.5), Effect.NOTE, 0);
								}
							} else {
								ap.getPlayer().playEffect(i.getLocation(), Effect.MOBSPAWNER_FLAMES, 0);
							}
						}
					}
				}
			}
		}.runTaskTimer(Main.plugin, 0, 20);
	}
	@ArenaEventHandler
	public void onDeath(PlayerDeathEvent evnt){
		ArenaPlayer ap = BattleArena.toArenaPlayer(evnt.getEntity());
		Item item = ap.getLocation().getWorld().dropItem(ap.getLocation(), ap.getTeam().getHeadItem());
		items.put(item, ap.getTeam());
		itm.add(item);
	}
	@Override
	public void onCancel(){
		for(Item it:itm){
			it.remove();
		}
		if(eff != null) eff.cancel();
	}
	@Override
	public void onFinish(){
		for(Item it:itm){
			it.remove();
		}
		if(eff != null) eff.cancel();
	}
	@ArenaEventHandler
	public void onPickup(PlayerPickupItemEvent evnt){
		if(itm.contains(evnt.getItem())){
			itm.remove(evnt.getItem());
			ArenaTeam team = items.get(evnt.getItem());
			if(team != BattleArena.toArenaPlayer(evnt.getPlayer()).getTeam()){
				evnt.getPlayer().playSound(evnt.getPlayer().getLocation(), Sound.LEVEL_UP, 10, 2);
				scores.addScore(BattleArena.toArenaPlayer(evnt.getPlayer()).getTeam(), BattleArena.toArenaPlayer(evnt.getPlayer()));
				if(scores.scores.getPoints(BattleArena.toArenaPlayer(evnt.getPlayer()).getTeam()) >= pointsToWin) setWinner(BattleArena.toArenaPlayer(evnt.getPlayer()).getTeam());
			} else {
				evnt.getPlayer().playSound(evnt.getPlayer().getLocation(), Sound.LEVEL_UP, 10, 1.5F);
			}
			evnt.setCancelled(true);
			evnt.getItem().remove();
		}
	}
}
