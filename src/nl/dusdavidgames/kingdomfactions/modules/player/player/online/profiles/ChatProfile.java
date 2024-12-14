package nl.dusdavidgames.kingdomfactions.modules.player.player.online.profiles;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import org.bukkit.event.player.AsyncPlayerChatEvent;

import io.netty.channel.ChannelException;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;
import nl.dusdavidgames.kingdomfactions.modules.chat.ChatModule;
import nl.dusdavidgames.kingdomfactions.modules.chat.framework.ChannelList;
import nl.dusdavidgames.kingdomfactions.modules.chat.framework.ChannelRank;
import nl.dusdavidgames.kingdomfactions.modules.chat.framework.ChatChannel;
import nl.dusdavidgames.kingdomfactions.modules.chat.framework.PasswordAttemptSession;
import nl.dusdavidgames.kingdomfactions.modules.chat.framework.channelranks.DDGStaffChannelRank;
import nl.dusdavidgames.kingdomfactions.modules.chat.framework.channelranks.FactionChannelRank;
import nl.dusdavidgames.kingdomfactions.modules.chat.framework.channelranks.KingdomChannelRank;
import nl.dusdavidgames.kingdomfactions.modules.chat.framework.channelranks.RankHolder;
import nl.dusdavidgames.kingdomfactions.modules.chat.framework.channelranks.SpeakerChannelRank;
import nl.dusdavidgames.kingdomfactions.modules.chat.framework.channels.KingdomChannel;
import nl.dusdavidgames.kingdomfactions.modules.exception.chat.ChannelNotFoundException;
import nl.dusdavidgames.kingdomfactions.modules.player.player.online.KingdomFactionsPlayer;

public class ChatProfile extends Profile {

    public ChatProfile(KingdomFactionsPlayer player) {
        super(player);
    }

    private @Getter @Setter ChatChannel current;
    private @Getter ArrayList<RankHolder> holders = new ArrayList<>();
    private @Getter @Setter KingdomFactionsPlayer replyPlayer;

    private Queue<String> lastChats = new LinkedList<>();

    // Get list of channels player is part of
    public ChannelList getChannels() {
        ChannelList channels = new ChannelList();
        for (ChatChannel c : ChatModule.getInstance().getChannels()) {
            if (c.getJoinedPlayers().contains(this.player)) {
                channels.add(c);
            }
        }
        return channels;
    }

    // Add player to a chat channel
    public void addChannel(ChatChannel channel) {
        channel.join(this.player, false);
    }

    // Remove player from a chat channel
    public void removeChannel(ChatChannel channel) {
        channel.leave(this.player);
    }

    // Set rank for player in a specific channel
    public void setRank(ChannelRank rank, ChatChannel channel) {
        if (!hasRank(channel)) {
            this.holders.add(new RankHolder(this, channel.getName(), rank));
        }
    }

    // Remove rank from player in a specific channel
    public void removeRank(ChatChannel channel) {
        RankHolder holder = getRankHolder(channel);
        if (holder != null) {
            holder.remove();
        }
    }

    // Get rank of player in a specific channel
    public ChannelRank getRank(ChatChannel channel) {
        RankHolder rank = getRankHolder(channel);
        return rank != null ? rank.getRank() : null;
    }

    // Get rank holder of player in a specific channel
    public RankHolder getRankHolder(ChatChannel channel) {
        for (RankHolder h : holders) {
            if (channel.getName().equalsIgnoreCase(h.getChannel())) {
                return h;
            }
        }
        return null;
    }

    // Check if player has a rank in the channel
    public boolean hasRank(ChatChannel channel) {
        return getRank(channel) != null;
    }

    // Check if player is currently typing a password
    public boolean isTypingPassword() {
        return player.getAction() instanceof PasswordAttemptSession;
    }

    // Check if player has joined a channel
    public boolean hasJoinedChannel(ChatChannel channel) {
        return channel.getJoinedPlayers().contains(this.getPlayer());
    }

    // Check if player may join a channel based on whitelist
    public boolean mayJoinChannel(ChatChannel channel) {
        return channel.getWhitelist().contains(this.getPlayer().getUuid());
    }

    // Wipe all channels and set the default channels for the player
    public void wipeChannels() {
        try {
            // Remove player from all channels
            ChatModule.getInstance().getChannels().forEach(channel -> channel.leave(player, false, false));
            // Disallow player from all channels
            ChatModule.getInstance().getChannels().forEach(channel -> channel.disAllow(player));

            holders.clear();

            // Join default channels
            ChatChannel radius = ChatModule.getInstance().getChannelByName("Radius");
            KingdomChannel kingdom = (KingdomChannel) ChatModule.getInstance().getChannelByName(player.getKingdom().getName());

            radius.allow(player);
            radius.join(player, false);
            kingdom.allow(player);
            kingdom.join(player, false);

            // Set ranks based on player's status
            if (player.isStaff()) {
                player.getChatProfile().setRank(new DDGStaffChannelRank(new KingdomChannelRank(player.getKingdomRank())), kingdom);
                player.getChatProfile().setRank(new DDGStaffChannelRank(new SpeakerChannelRank()), radius);
                if (player.hasFaction()) {
                    player.getFaction().getChannel().allow(player);
                    player.getFaction().getChannel().join(player, false);
                    player.getChatProfile().setRank(new DDGStaffChannelRank(new FactionChannelRank(player.getFactionRank())), player.getFaction().getChannel());
                }
            } else {
                player.getChatProfile().setRank(new KingdomChannelRank(player.getKingdomRank()), kingdom);
                player.getChatProfile().setRank(new SpeakerChannelRank(), radius);
                if (player.hasFaction()) {
                    player.getFaction().getChannel().allow(player);
                    player.getFaction().getChannel().join(player, false);
                    player.getChatProfile().setRank(new FactionChannelRank(player.getFactionRank()), player.getFaction().getChannel());
                }
            }

            player.getChatProfile().setCurrent(kingdom);

        } catch (ChannelException | ChannelNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Check if player may chat based on cooldown and spam filter
    public boolean mayChat(AsyncPlayerChatEvent e) {
        if (this.player.hasCooldown("chatcooldown")) {
            if (this.player.hasPermission("kingdomfactions.chat.nocooldown") || this.player.isStaff()) {
                this.player.removeCooldown("chatcooldown");
            } else {
                this.player.sendMessage(getChatCooldownMessage(this.player.getCooldown("chatcooldown").getCooldown()));
                e.setCancelled(true);
                return false;
            }
        }

        if (this.player.hasPermission("kingdomfactions.chat.nofilter") || this.player.isStaff()) {
            lastChats.clear();
            return true;
        }

        if (lastChats.contains(e.getMessage())) {
            this.player.sendMessage(ChatColor.RED + "Spammen is niet toegestaan.");
            e.setCancelled(true);
            return false;
        } else {
            if (lastChats.size() > 4) {
                lastChats.poll();
            }
            lastChats.add(e.getMessage());
            return true;
        }
    }

    // Check if player may use a specific message based on cooldown and spam filter
    public boolean mayUseMsg(String message) {
        if (this.player.hasCooldown("chatcooldown")) {
            if (this.player.hasPermission("kingdomfactions.chat.nocooldown") || this.player.isStaff()) {
                this.player.removeCooldown("chatcooldown");
            } else {
                this.player.sendMessage(getChatCooldownMessage(this.player.getCooldown("chatcooldown").getCooldown()));
                return false;
            }
        }

        if (this.player.hasPermission("kingdomfactions.chat.nofilter") || this.player.isStaff()) {
            lastChats.clear();
            return true;
        }

        if (lastChats.contains(message)) {
            this.player.sendMessage(ChatColor.RED + "Spammen is niet toegestaan.");
            return false;
        } else {
            if (lastChats.size() > 4) {
                lastChats.poll();
            }
            lastChats.add(message);
            return true;
        }
    }

    // Get the cooldown message
    private String getChatCooldownMessage(int cooldownTime) {
        if (cooldownTime == 1) {
            return ChatColor.RED + "Je moet nog " + ChatColor.GRAY + "1 seconde" + ChatColor.RED + " wachten";
        } else {
            return ChatColor.RED + "Je moet nog " + ChatColor.GRAY + cooldownTime + " seconden" + ChatColor.RED + " wachten";
        }
    }
}
