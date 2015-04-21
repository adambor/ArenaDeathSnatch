package mc.adambor.ArenaDS;

import mc.alk.arena.BattleArena;
import mc.alk.arena.objects.victoryconditions.VictoryType;
import mc.alk.arena.util.Log;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	static Main plugin;
	@Override
	public void onEnable(){
		plugin = this;
		loadConfig();
		VictoryType.register(Victory.class, this);
		BattleArena.registerCompetition(this, "ArenaDeathSnatch", "ads", ArenaDeathSnatch.class, new ADS_CommandExecutor());
		Log.info("[" + getName()+ "] v" + getDescription().getVersion()+ " enabled!");
	}

	@Override
	public void onDisable(){
		Log.info("[" + getName()+ "] v" + getDescription().getVersion()+ " stopping!");
	}
	public void loadConfig(){
		saveDefaultConfig();
        FileConfiguration conf = plugin.getConfig();
		ArenaDeathSnatch.pointsToWin = conf.getInt("scoreToWin", 30);
	}
	@Override
	public void reloadConfig(){
		super.reloadConfig();
	    loadConfig();
	}
}

