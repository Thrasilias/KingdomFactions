package nl.dusdavidgames.kingdomfactions.modules.utils.book;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

public class BookBuilder {

    private BookMeta bookMeta;
    private ArrayList<IPage> pages = new ArrayList<IPage>();

    public BookBuilder() {
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK, 1);
        this.bookMeta = (BookMeta) book.getItemMeta();
    }

    public BookBuilder setTitle(String title) {
        this.bookMeta.setTitle(title);
        return this;
    }

    public BookBuilder setAuthor(String name) {
        this.bookMeta.setAuthor(name);
        return this;
    }

    public BookBuilder addPage(String... page) {
        return this.addPage(new Page(page));
    }

    public BookBuilder addPages(IPage... pages) {
        for (IPage p : pages) {
            this.addPage(p);
        }
        return this;
    }

    public BookBuilder addPage(IPage page) {
        this.pages.add(page);
        return this;
    }

    public BookBuilder setLore(List<String> lore) {
        this.bookMeta.setLore(lore);
        return this;
    }

    public BookBuilder setLore(String... list) {
        this.bookMeta.setLore(Arrays.asList(list));
        return this;
    }

    public void setPage(int i, Page page) {
        this.pages.set(i, page);
    }

    public IPage getPage(int i) {
        return this.pages.get(i);
    }

    public ItemStack build() {
        ItemStack itemStack = new ItemStack(Material.WRITTEN_BOOK, 1);

        // Create pages for the book
        this.createPages(this.pages);
        itemStack.setItemMeta(this.bookMeta);

        return itemStack;
    }

    private void createPages(List<IPage> list) {
        for (IPage unknownPage : list) {
            if (unknownPage instanceof Page) {
                Page p = (Page) unknownPage;
                StringBuilder builder = new StringBuilder();

                for (String line : p.lines) {
                    builder.append(line);
                    builder.append("\n");
                }
                this.bookMeta.addPage(builder.toString());
            } else if (unknownPage instanceof SpecialPage) {
                SpecialPage special = (SpecialPage) unknownPage;

                // Convert the lines to NMS components and serialize them
                NMS_TextComponent[] lines = special.getLines().toArray(new NMS_TextComponent[0]);
                Component component = new TextComponent(lines);
                String serializedComponent = ComponentSerializer.toString(component);

                this.bookMeta.addPage(serializedComponent);
            }
        }
    }
}
