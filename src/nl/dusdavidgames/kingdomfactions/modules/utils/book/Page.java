package nl.dusdavidgames.kingdomfactions.modules.utils.book;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.bukkit.ChatColor;

import lombok.Getter;
import lombok.Setter;

public class Page implements IPage {

    // Constructors
    public Page(List<String> lines) {
        this.lines = lines;
    }

    public Page(String... lines) {
        this.lines = Arrays.asList(lines);
    }

    public Page() {
        this.lines = new ArrayList<>();
    }

    @Setter
    @Getter
    private ChatColor pageColor;

    @Setter
    @Getter
    private List<String> lines;

    // Methods for line manipulation
    public void setLine(int line, String text) {
        if (line >= 0 && line < lines.size()) {
            this.lines.set(line, text);
        }
    }

    public String getLine(int line) {
        if (line >= 0 && line < lines.size()) {
            return this.lines.get(line);
        }
        return null;
    }

    public void addLine(String line) {
        if (this.pageColor == null) {
            this.lines.add(line);
        } else {
            this.lines.add(this.pageColor + line); // Add color to the line
        }
    }

    public void addBlankLine() {
        this.lines.add("");
    }

    public void clear() {
        this.lines.clear();
    }

    public Iterator<String> iterator() {
        return this.lines.iterator();
    }
}
