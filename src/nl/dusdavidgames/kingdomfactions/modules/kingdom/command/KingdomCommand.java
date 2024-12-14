package nl.dusdavidgames.kingdomfactions.modules.kingdom.command;

import net.md_5.bungee.api.ChatColor;
import nl.dusdavidgames.kingdomfactions.modules.command.KingdomFactionsCommand;
import nl.dusdavidgames.kingdomfactions.modules.command.SubCommand;
import nl.dusdavidgames.kingdomfactions.modules.database.mysql.databases.KingdomDatabase;
import nl.dusdavidgames.kingdomfactions.modules.exception.KingdomFactionsException;
import nl.dusdavidgames.kingdomfactions.modules.exception.player.UnkownPlayerException;
import nl.dusdavidgames.kingdomfactions.modules.kingdom.KingdomModule;
import nl.dusdavidgames.kingdomfactions.modules.kingdom.KingdomRank;
import nl.dusdavidgames.kingdomfactions.modules.kingdom.kingdom.Kingdom;
import nl.dusdavidgames.kingdomfactions.modules.player.PlayerModule;
import nl.dusdavidgames.kingdomfactions.modules.player.player.IPlayerBase;
import nl.dusdavidgames.kingdomfactions.modules.player.player.online.KingdomFactionsPlayer;
import nl.dusdavidgames.kingdomfactions.modules.utils.Messages;

public class KingdomCommand extends KingdomFactionsCommand {

    public KingdomCommand(String name, String permission, String info, String usage, boolean sub, boolean allowConsole) {
        super(name, permission, info, usage, sub, allowConsole);
    }

    @Override
    public void init() {
        registerSub(new SubCommand("setkingdom", "kingdomfactions.command.kingdom.setkingdom", "Zet iemand in een kingdom!") {
            @Override
            public void execute(String[] args) {
                IPlayerBase player = getPlayerBaseFromArgs(args, 1);
                if (player == null) return;

                Kingdom kingdom = KingdomModule.getInstance().getKingdom(args[2]);
                if (kingdom == null) {
                    sendMessage("Ongeldig Kingdom! Kies uit: HYVAR, EREDON, TILIFIA, MALZAN, ADAMANTIUM en DOK");
                    return;
                }

                player.setKingdom(kingdom);
                sendMessage("Je hebt " + player.getName() + " naar het Kingdom " + player.getKingdom().getType().getPrefix() + ChatColor.WHITE + "gezet!");
            }
        });

        registerSub(new SubCommand("setrank", "kingdomfactions.command.kingdom.setrank", "Zet iemand's rank in een Kingdom") {
            @Override
            public void execute(String[] args) {
                IPlayerBase player = getPlayerBaseFromArgs(args, 1);
                if (player == null) return;

                KingdomRank rank = KingdomRank.getRank(args[2]);
                player.setKingdomRank(rank);
                sendMessage("Je hebt " + player.getName() + " gepromoveerd tot " + rank);
            }
        });

        registerSub(new SubCommand("setspawn", "kingdomfactions.command.kingdom.setspawn", "Zet de spawn van een kingdom!") {
            @Override
            public void execute(String[] args) {
                Kingdom kingdom = KingdomModule.getInstance().getKingdom(args[1]);
                if (kingdom != null) {
                    kingdom.setSpawn(getPlayer().getLocation());
                    KingdomDatabase.getInstance().setSpawn(kingdom.getType().toString(), getPlayer().getLocation());
                    sendMessage("Je hebt de Spawn van " + kingdom.getType().getPrefix() + ChatColor.WHITE + "gezet!");
                } else {
                    sendMessage("Ongeldig Kingdom! Kies uit: HYVAR, EREDON, TILIFIA, MALZAN, ADAMANTIUM en DOK");
                }
            }
        });

        registerSub(new SubCommand("spawn", "kingdomfactions.command.kingdom.spawn", "Teleporteer naar de spawn van een Kingdom") {
            @Override
            public void execute(String[] args) {
                try {
                    KingdomFactionsPlayer player = PlayerModule.getInstance().getPlayer(args[2]);
                    if (player == null) {
                        sendMessage("Deze speler is offline!");
                    } else {
                        Kingdom kingdom = KingdomModule.getInstance().getKingdom(args[1]);
                        if (kingdom != null && kingdom.getSpawn() != null) {
                            player.teleport(kingdom.getSpawn());
                            sendMessage("Je hebt " + player.getName() + " naar de spawn van " + kingdom.getType().getPrefix() + "geteleporteerd!");
                        } else {
                            sendMessage("Dit Kingdom heeft geen spawn gezet!");
                        }
                    }
                } catch (ArrayIndexOutOfBoundsException | KingdomFactionsException e) {
                    Kingdom kingdom = KingdomModule.getInstance().getKingdom(args[1]);
                    if (kingdom != null && kingdom.getSpawn() != null) {
                        getPlayer().teleport(kingdom.getSpawn());
                        sendMessage("Teleporteren naar de spawn van " + kingdom.getType().getPrefix());
                    } else {
                        sendMessage("Ongeldig Kingdom of geen spawn gezet!");
                    }
                }
            }
        });

        registerSub(new SubCommand("check", "kingdomfactions.command.kingdom.check", "Verkrijg informatie over een speler") {
            @Override
            public void execute(String[] args) throws UnkownPlayerException {
                IPlayerBase player = PlayerModule.getInstance().getPlayerBase(args[1]);
                sendMessage("---------------");
                sendMessage("Kingdom: " + player.getKingdom().getType().getPrefix());
                if (player.getKingdomRank() != KingdomRank.SPELER) {
                    sendMessage("Kingdom Rank: " + player.getKingdomRank().getPrefix());
                }
                sendMessage("---------------");
            }
        });

        registerSub(new SubCommand("setcapital", "kingdomfactions.command.kingdom.setcapital", "Zet de hoofdstad van een Kingdom!") {
            @Override
            public void execute(String[] args) {
                if (!getPlayer().hasAction()) {
                    Kingdom kingdom = KingdomModule.getInstance().getKingdom(args[1]);
                    if (kingdom != null) {
                        KingdomModule.getInstance().createAction(kingdom, getPlayer());
                        sendMessage("Je hebt de hoofdstad actie gestart voor " + kingdom.getType().getPrefix());
                    } else {
                        sendMessage("Ongeldig Kingdom!");
                    }
                } else {
                    sendMessage("Je hebt al een actie lopen!!");
                }
            }
        });
    }

    private IPlayerBase getPlayerBaseFromArgs(String[] args, int index) {
        try {
            return PlayerModule.getInstance().getPlayerBase(args[index]);
        } catch (UnkownPlayerException e) {
            sendMessage("Kon deze speler niet vinden.");
            return null;
        }
    }

    private void sendMessage(String message) {
        getPlayer().sendMessage(Messages.getInstance().getPrefix() + message);
    }

    @Override
    public void execute() {
        // This method is not used in this context, you can leave it empty or remove it if necessary.
    }
}
