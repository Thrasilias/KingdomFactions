package nl.dusdavidgames.kingdomfactions.modules.utils.nms;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import org.bukkit.Bukkit;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.game.ClientboundPlayerListHeaderFooterPacket;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import nl.dusdavidgames.kingdomfactions.modules.player.player.online.KingdomFactionsPlayer;

public class NMSMethods {

    public void sendTabTitle(KingdomFactionsPlayer player, String header, String footer) {
        if (header == null) {
            header = "";
        }
        header = ChatColor.translateAlternateColorCodes('&', header);
        if (footer == null) {
            footer = "";
        }
        footer = ChatColor.translateAlternateColorCodes('&', footer);

        header = header.replaceAll("%player%", player.getPlayer().getDisplayName());
        footer = footer.replaceAll("%player%", player.getPlayer().getDisplayName());
        try {
            Component tabHeader = new TextComponent(header);
            Component tabFooter = new TextComponent(footer);

            ClientboundPlayerListHeaderFooterPacket packet = new ClientboundPlayerListHeaderFooterPacket(tabHeader, tabFooter);
            player.sendPacket(packet);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void sendTitle(KingdomFactionsPlayer p, String titleText, int inFadeTicks, int ShowTicks, int outFadeTicks) {
        Component title = new TextComponent(titleText);
        ClientboundSetTitleTextPacket titlePacket = new ClientboundSetTitleTextPacket(title);
        p.getEntityPlayer().playerConnection.sendPacket(titlePacket);
    }

    public void sendSubTitle(KingdomFactionsPlayer p, String titleText, int inFadeTicks, int ShowTicks, int outFadeTicks) {
        Component subtitle = new TextComponent(titleText);
        ClientboundSetSubtitleTextPacket subtitlePacket = new ClientboundSetSubtitleTextPacket(subtitle);
        p.getEntityPlayer().playerConnection.sendPacket(subtitlePacket);
    }

    private Class<?> getNMSClass(String name) {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        try {
            return Class.forName("net.minecraft.server." + version + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void sendActionBar(KingdomFactionsPlayer player, String actionBar) {
        Component cbc = new TextComponent(actionBar);
        ClientboundSystemChatPacket packet = new ClientboundSystemChatPacket(cbc, (byte) 2);
        player.sendPacket(packet);
    }
}
