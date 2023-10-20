package dev.redstudio.recrystallizedwing.items;


import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

/**
 * A base class for all the items aimed at reducing duplicated code.
 *
 * @author Luna Lage (Desoroxxx)
 * @since 1.2
 */
public class BaseItem extends Item {

    public BaseItem(final Properties properties, final int durability) {
        super(properties.tab(CreativeModeTab.TAB_TRANSPORTATION).durability(durability > 1 ? durability - 1 : durability == 1 ? 1 : 0));
    }
}
