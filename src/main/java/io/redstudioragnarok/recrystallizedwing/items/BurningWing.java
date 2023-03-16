package io.redstudioragnarok.recrystallizedwing.items;

import io.redstudioragnarok.recrystallizedwing.utils.RCWUtils;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

import static io.redstudioragnarok.recrystallizedwing.RCW.burntWing;

public class BurningWing extends Item {

    public BurningWing() {
        setCreativeTab(CreativeTabs.TRANSPORTATION);

        maxStackSize = 1;
    }

    @Override
    public void onUpdate(final ItemStack itemStack, final World world, final Entity entity, final int itemSlot, final boolean flag) {
        if (!world.isRemote) {
            if (entity.isInWater()) {
                if (entity instanceof EntityPlayer) {
                    EntityPlayer player = (EntityPlayer) entity;

                    world.playSound(null, player.getPosition(), SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.MASTER, 1.0F, 1.0F);
                    RCWUtils.spawnExplosionParticleAtEntity(player, 160);

                    player.inventory.setInventorySlotContents(itemSlot, new ItemStack(burntWing));
                }
            } else if (!entity.isBurning())
                entity.setFire(2);
        }
    }
}
