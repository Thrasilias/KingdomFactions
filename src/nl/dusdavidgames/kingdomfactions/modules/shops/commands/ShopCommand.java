package nl.dusdavidgames.kingdomfactions.modules.shops.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import nl.dusdavidgames.kingdomfactions.modules.command.KingdomFactionsCommand;
import nl.dusdavidgames.kingdomfactions.modules.command.SubCommand;
import nl.dusdavidgames.kingdomfactions.modules.exception.KingdomFactionsException;
import nl.dusdavidgames.kingdomfactions.modules.nexus.build.type.BuildLevel;
import nl.dusdavidgames.kingdomfactions.modules.nexus.build.type.BuildingType;
import nl.dusdavidgames.kingdomfactions.modules.player.PlayerModule;
import nl.dusdavidgames.kingdomfactions.modules.shops.ShopsModule;
import nl.dusdavidgames.kingdomfactions.modules.shops.shop.AddItemMenu;
import nl.dusdavidgames.kingdomfactions.modules.utils.Messages;

public class ShopCommand extends KingdomFactionsCommand {

    public ShopCommand(String name, String permission, String info, String usage, boolean sub, boolean allowConsole) {
        super(name, permission, info, usage, sub, allowConsole);
    }

    @Override
    public void init() {
        this.registerSub(new SubCommand("additem", "kingdomfactions.command.shop.additem", "Voeg een item toe aan de shop!") {

            @Override
            public void execute(String[] args) {
                if (!(getSender() instanceof Player)) {
                    getSender().sendMessage(Messages.getInstance().getPrefix()
                            + "Dit command is niet voor de console beschikbaar!");
                    return;
                }

                if (args.length < 6) {
                    getSender().sendMessage(Messages.getInstance().getPrefix()
                            + "/shop additem <type> <level> <buyprice> <sellprice> <displayname> [limit] [extraData]");
                    getSender().sendMessage(Messages.getInstance().getPrefix()
                            + "Example: /shop additem nexus 1 1000 -1 false 1 Spider");
                    return;
                }

                Player player = (Player) getSender();

                String type = getArgs()[1].toUpperCase();
                String level = "LEVEL_" + getArgs()[2];
                int buyPrice;
                int sellPrice;
                boolean useDisplayname;

                try {
                    buyPrice = Integer.parseInt(getArgs()[3]);
                    sellPrice = Integer.parseInt(getArgs()[4]);
                    useDisplayname = Boolean.parseBoolean(getArgs()[5]);
                } catch (NumberFormatException e) {
                    player.sendMessage(Messages.getInstance().getPrefix() + "Ongeldige prijs of displaynaam.");
                    return;
                }

                if (!isValidType(type, player) || !isValidLevel(level, player)) {
                    return;
                }

                int limit = args.length > 6 ? Integer.parseInt(getArgs()[6]) : -1;
                String extraData = args.length > 7 ? getArgs()[7] : " ";

                new AddItemMenu(PlayerModule.getInstance().getPlayer(player), type, level, buyPrice, sellPrice,
                        useDisplayname, extraData, limit);
            }

            private boolean isValidType(String type, Player player) {
                for (BuildingType t : BuildingType.values()) {
                    if (t.name().equalsIgnoreCase(type)) {
                        return true;
                    }
                }
                player.sendMessage(Messages.getInstance().getPrefix() + "Type niet gevonden! Types: "
                        + BuildingType.values().toString());
                return false;
            }

            private boolean isValidLevel(String level, Player player) {
                for (BuildLevel levels : BuildLevel.values()) {
                    if (levels.name().equalsIgnoreCase(level)) {
                        return true;
                    }
                }
                player.sendMessage(Messages.getInstance().getPrefix() + "Level niet gevonden! Levels: "
                        + BuildLevel.values().toString());
                return false;
            }
        });

        this.registerSub(new SubCommand("open", "kingdomfactions.command.shop.open", "Open een shop menu!") {

            @Override
            public void execute(String[] args) {
                if (!(getSender() instanceof Player)) {
                    getSender().sendMessage(Messages.getInstance().getPrefix() + "Dit command is niet voor de console beschikbaar!");
                    return;
                }

                if (args.length < 3) {
                    getSender().sendMessage(Messages.getInstance().getPrefix() + "Gebruik: /shop open <type> <level>");
                    return;
                }

                String buildingName = getArgs()[1];
                BuildingType bType = getBuildingType(buildingName);
                if (bType == null) {
                    getSender().sendMessage(Messages.getInstance().getPrefix() + "Invalid building type: " + buildingName);
                    return;
                }

                String level = "LEVEL_" + getArgs()[2];
                BuildLevel sLevel = getBuildLevel(level);
                if (sLevel == null) {
                    getSender().sendMessage(Messages.getInstance().getPrefix() + "Invalid build level: " + level);
                    return;
                }

                Player player = (Player) getSender();
                player.openInventory(ShopsModule.getInstance().getShop(bType, sLevel).getShopInventory());
                broadcast(PlayerModule.getInstance().getPlayer(getSender()).getFormattedName() + ChatColor.YELLOW
                        + " heeft een shop geopend type " + bType + " level " + sLevel + ".");
            }

            private BuildingType getBuildingType(String buildingName) {
                try {
                    return BuildingType.valueOf(buildingName.toUpperCase());
                } catch (Exception e) {
                    return null;
                }
            }

            private BuildLevel getBuildLevel(String level) {
                try {
                    return BuildLevel.valueOf(level);
                } catch (Exception e) {
                    return null;
                }
            }
        });

        this.registerSub(new SubCommand("reload", "kingdomfactions.command.shop.reload", "reload de shops!") {

            @Override
            public void execute(String[] args) {
                getSender().sendMessage(Messages.getInstance().getPrefix() + "Shops herladen...");
                ShopsModule.getInstance().reload(getSender());
            }
        });
    }

    @Override
    public void execute() throws KingdomFactionsException {
        // No implementation needed as it's handled by subcommands
    }
}
