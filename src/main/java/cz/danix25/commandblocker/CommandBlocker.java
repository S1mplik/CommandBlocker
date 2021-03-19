package cz.danix25.commandblocker;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandBlocker extends JavaPlugin implements Listener {

    @Override

    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(this, this);
        getCommand("cb").setExecutor(this);
    }


    @Override
    public void onDisable() {

    }

    @Override

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (cmd.getName().equalsIgnoreCase("cb")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(
                        ChatColor.translateAlternateColorCodes('&', "[&6&lCommandBlocker&7] &cYou do not have permission (not human)"));
                return true;
            }

            Player p = (Player) sender;

            if(args.length <=0) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "[&6&lCommandBlocker&7] &cUse /cb reload"));
                return true;
            }

            if(args[0].equalsIgnoreCase("reload")) {
                reloadConfig();
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "[&6&lCommandBlocker&7] &aSuccesfull reload configuration"));
                return true;
            }
        }
        return true;
    }

    @EventHandler

    public void onCommandEvent(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();
        if (p.hasPermission("cb.*") || p.hasPermission("cb.bypass"))
            return;
        String cmd = e.getMessage().replaceFirst("/", "").split(" ")[0];
        for (String block : getBlockedCommands()) {
            if (block.equalsIgnoreCase(cmd)) {
                if (!p.hasPermission("cb." + block)) {
                    e.setCancelled(true);
                    p.sendMessage(getBlockedCommandMessage());
                    return;
                }
            }
        }

        for (String block : getBlockedCommands()) {
            if(block.equalsIgnoreCase(cmd)) {
                if(!p.hasPermission(("cb." + block))) {
                    e.setCancelled(true);
                    p.sendMessage(getBlockedCommandMessage());
                }
            }
        }
    }

    public List<String> getBlockedCommands() {
        return getConfig().getStringList("BlockedCommands");
    }




    public String getBlockedCommandMessage() {
        return ChatColor.translateAlternateColorCodes('&', getConfig().getString("Message"));
    }

}

