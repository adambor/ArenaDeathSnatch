package mc.adambor.ArenaDS;

import org.bukkit.entity.Player;

import mc.alk.arena.executors.CustomCommandExecutor;
import mc.alk.arena.executors.MCCommand;

public class ADS_CommandExecutor extends CustomCommandExecutor {
	@MCCommand(cmds={"setScoreToWin"}, admin=true)
	public static boolean setScoreToWin(Player sender, Integer score){
		Main.plugin.getConfig().set("scoreToWin", score);
		Main.plugin.saveConfig();
		Main.plugin.reloadConfig();
	    return sendMessage(sender, "&2Score to win this game is now: "+score);
	}
}
