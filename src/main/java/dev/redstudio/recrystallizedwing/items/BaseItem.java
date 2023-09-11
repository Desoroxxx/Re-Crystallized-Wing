package dev.redstudio.recrystallizedwing.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

/**
 * A base class for all the items aimed at reducing duplicated code.
 *
 * @author Luna Lage (Desoroxxx)
 * @since 1.2
 */
public class BaseItem extends Item {

    public BaseItem() {
        setCreativeTab(CreativeTabs.TRANSPORTATION);

        maxStackSize = 1;
    }

    public BaseItem(final int durability) {
        this();

        if (durability > 1)
            setMaxDamage(durability - 1);
        else if (durability == 1)
            setMaxDamage(1);
    }
}
