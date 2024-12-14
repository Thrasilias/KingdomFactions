package nl.dusdavidgames.kingdomfactions.modules.utils.book;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class SpecialPage implements IPage {

    public SpecialPage() {
        this.lines = new ArrayList<>();
    }

    @Setter
    @Getter
    private List<BaseComponent> lines;

    // Set a specific line with text, click event, and hover event
    public void setLine(int line, String text, ClickEvent clickEvent, HoverEvent hoverEvent) {
        TextComponent textComponent = new TextComponent(text + "\n");
        if (clickEvent != null) {
            textComponent.setClickEvent(clickEvent);
        }
        if (hoverEvent != null) {
            textComponent.setHoverEvent(hoverEvent);
        }
        this.lines.set(line, textComponent);
    }

    // Get a specific line from the page
    public BaseComponent getLine(int line) {
        return this.lines.get(line);
    }

    // Add a line with only text
    public void addLine(String text) {
        this.addLine(text, null, null);
    }

    // Add a line with text, click event, and hover event
    public void addLine(String text, ClickEvent clickEvent, HoverEvent hoverEvent) {
        TextComponent textComponent = new TextComponent(text + "\n");
        if (clickEvent != null) {
            textComponent.setClickEvent(clickEvent);
        }
        if (hoverEvent != null) {
            textComponent.setHoverEvent(hoverEvent);
        }
        this.lines.add(textComponent);
    }

    // Add a line with multiple chat pieces
    public void addLine(ChatPieceHolder... pieces) {
        List<TextComponent> componentList = new ArrayList<>();
        for (ChatPieceHolder holder : pieces) {
            TextComponent textComponent = new TextComponent(holder.getText());
            if (holder.getClick() != null) {
                textComponent.setClickEvent(holder.getClick());
            }
            if (holder.getHover() != null) {
                textComponent.setHoverEvent(holder.getHover());
            }
            componentList.add(textComponent);
        }
        componentList.add(new TextComponent(" " + "\n"));
        this.lines.add(new TextComponent(componentList.toArray(new BaseComponent[0])));
    }

    // Add a single chat piece holder
    public void addLine(ChatPieceHolder holder) {
        this.addLine(holder);
    }

    // Add a blank line
    public void addBlankLine() {
        TextComponent textComponent = new TextComponent(" \n");
        this.lines.add(textComponent);
    }

    // Clear all lines
    public void clear() {
        this.lines.clear();
    }

    // Iterator for the lines
    public Iterator<BaseComponent> iterator() {
        return this.lines.iterator();
    }
}
