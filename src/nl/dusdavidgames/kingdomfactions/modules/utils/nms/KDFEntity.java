package nl.dusdavidgames.kingdomfactions.modules.utils.nms;

import org.bukkit.craftbukkit.v1_20_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import net.minecraft.world.entity.EquipmentSlot;

public class KDFEntity {

    public KDFEntity(Entity entity) {
        this.entity = entity;
    }
    
    private @Getter Entity entity;
    
    public void setItem(EquipmentSlot slot, ItemStack item) {
        this.getNMSEntity().setItemSlot(slot, CraftItemStack.asNMSCopy(item));
    }

    private net.minecraft.world.entity.Entity getNMSEntity() {
        return ((CraftEntity) entity).getHandle();
    }
}
