package nl.dusdavidgames.kingdomfactions.modules.utils.book;

import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;

public class ChatPieceHolder {

    // Constructor with text, click event, and hover event
    public ChatPieceHolder(String text, ClickEvent click, HoverEvent hover) {
        this.text = text;
        this.click = click;
        this.hover = hover;
    }

    // Constructor with just text (no click/hover events)
    public ChatPieceHolder(String text) {
        this(text, null, null);
    }

    // Text for the chat component
    private @Setter @Getter String text;

    // Click event (if any)
    private @Setter @Getter ClickEvent click;

    // Hover event (if any)
    private @Setter @Getter HoverEvent hover;

    // Set the click event as opening a URL
    public void setClickLink(String link) {
        setClick(new ClickEvent(ClickEvent.Action.OPEN_URL, link));
    }

    // Set the hover event with a simple text message
    public void setHoverText(String text) {
        setHover(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(text).create()));
    }
}
