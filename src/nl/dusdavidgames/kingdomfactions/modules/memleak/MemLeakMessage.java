package nl.dusdavidgames.kingdomfactions.modules.memleak;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Queue;

import org.bukkit.command.CommandSender;

import lombok.Getter;
import lombok.Setter;
import nl.dusdavidgames.kingdomfactions.KingdomFactionsPlugin;
import nl.dusdavidgames.kingdomfactions.modules.chat.ChatModule;
import nl.dusdavidgames.kingdomfactions.modules.command.CommandModule;
import nl.dusdavidgames.kingdomfactions.modules.empirewand.SpellModule;
import nl.dusdavidgames.kingdomfactions.modules.faction.FactionModule;
import nl.dusdavidgames.kingdomfactions.modules.kingdom.KingdomModule;
import nl.dusdavidgames.kingdomfactions.modules.kingdom.kingdom.Kingdom;
import nl.dusdavidgames.kingdomfactions.modules.nexus.NexusModule;
import nl.dusdavidgames.kingdomfactions.modules.player.PlayerModule;
import nl.dusdavidgames.kingdomfactions.modules.player.deathban.DeathBanModule;
import nl.dusdavidgames.kingdomfactions.modules.player.player.online.KingdomFactionsPlayer;
import nl.dusdavidgames.kingdomfactions.modules.shops.ShopsModule;
import nl.dusdavidgames.kingdomfactions.modules.shops.shop.Shop;
import nl.dusdavidgames.kingdomfactions.modules.shops.shoplogger.ShopLogger;

public class MemLeakMessage {

    private static @Getter @Setter MemLeakMessage instance;

    public MemLeakMessage() {
        setInstance(this);
    }

    /**
     * Sends various statistics to the command sender to help monitor system performance and detect memory leaks.
     * @param sender The command sender to whom the statistics will be sent.
     */
    public void sendMessage(CommandSender sender) {
        sender.sendMessage("--------");
        sendData("Channels", ChatModule.getInstance().getChannels(), sender);
        sendData("Commands", CommandModule.getInstance().getCommand(), sender);
        sendData("Data list", KingdomFactionsPlugin.getInstance().getDataManager().getDataList(), sender);
        sendData("Spells", SpellModule.getInstance().getSpells(), sender);
        sendData("Factions", FactionModule.getInstance().getFactions(), sender);
        sendData("Kingdoms", KingdomModule.getInstance().getKingdoms(), sender);
        sendData("Kingdom guards", getKingdomGuards(), sender);
        sendData("Nexuses", NexusModule.getInstance().getNexuses(), sender);
        sendData("Deathbans", DeathBanModule.getInstance().getBans(), sender);
        sendData("Players", PlayerModule.getInstance().getPlayers(), sender);
        sendData("Shops", ShopsModule.getInstance().getShops(), sender);
        sendData("Shop logs", ShopLogger.getInstance().getShopLogs(), sender);
    }

    /**
     * Helper method to send the size of different types of collections to the sender.
     * @param name The name of the data to send.
     * @param list The collection whose size will be sent.
     * @param sender The command sender to whom the data will be sent.
     */
    private <T> void sendData(String name, Iterable<T> list, CommandSender sender) {
        if (list != null) {
            sender.sendMessage(name + " : " + ((Collection<?>)list).size());
        } else {
            sender.sendMessage(name + " : Error fetching data.");
        }
    }

    /**
     * Helper method to send the size of a queue to the sender.
     * @param name The name of the data to send.
     * @param queue The queue whose size will be sent.
     * @param sender The command sender to whom the data will be sent.
     */
    private void sendData(String name, Queue<?> queue, CommandSender sender) {
        if (queue != null) {
            sender.sendMessage(name + " : " + queue.size());
        } else {
            sender.sendMessage(name + " : Error fetching data.");
        }
    }

    /**
     * Gets the total number of kingdom guards from all kingdoms.
     * @return The total number of kingdom guards.
     */
    private int getKingdomGuards() {
        int guardCount = 0;
        for (Kingdom kingdom : KingdomModule.getInstance().getKingdoms()) {
            if (kingdom.getNexus() != null) {
                guardCount += kingdom.getNexus().getGuards().size();
            }
        }
        return guardCount;
    }

    /**
     * Gets the total number of shop items from all shops.
     * @return The total number of shop items.
     */
    private int getShopItems() {
        int itemCount = 0;
        if (ShopsModule.getInstance().getShops() != null) {
            for (Shop shop : ShopsModule.getInstance().getShops()) {
                itemCount += shop.getShopItemAmount();
            }
        }
        return itemCount;
    }

    /**
     * Gets the total number of cooldowns from all players.
     * @return The total number of player cooldowns.
     */
    private int getCooldowns() {
        int cooldownCount = 0;
        for (KingdomFactionsPlayer player : PlayerModule.getInstance().getPlayers()) {
            cooldownCount += player.getCooldowns().size();
        }
        return cooldownCount;
    }

    /**
     * Gets the total number of chat profile holders from all players.
     * @return The total number of chat profile holders.
     */
    private int getChatHolders() {
        int holderCount = 0;
        for (KingdomFactionsPlayer player : PlayerModule.getInstance().getPlayers()) {
            holderCount += player.getChatProfile().getHolders().size();
        }
        return holderCount;
    }

    /**
     * Gets the total number of metadata items from all players.
     * @return The total number of player metadata.
     */
    private int getMetaData() {
        int metaDataCount = 0;
        for (KingdomFactionsPlayer player : PlayerModule.getInstance().getPlayers()) {
            metaDataCount += player.getMetaData().size();
        }
        return metaDataCount;
    }
}
