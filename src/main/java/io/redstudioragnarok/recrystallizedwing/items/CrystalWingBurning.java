package io.redstudioragnarok.recrystallizedwing.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

import static io.redstudioragnarok.recrystallizedwing.RCW.crystalWingBurnt;

public class CrystalWingBurning extends Item {

    public CrystalWingBurning() {
        maxStackSize = 1;
        this.setCreativeTab(CreativeTabs.TRANSPORTATION);
    }

    @Override
    public void onUpdate(ItemStack itemstack, World world, Entity entity, int i, boolean flag) {
        if (entity.isInWater()) {
            if (entity instanceof EntityPlayer) {
                EntityPlayer entityplayer = (EntityPlayer) entity;
                world.playSound(null, entity.getPosition(), SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.PLAYERS , 1.0F, 1.0F);
                //entityplayer.addStat(FBP.burntWing, 1);
                replaceWings(entityplayer.inventory);
            }
            return;
        }
        if (!entity.isBurning()) {
            entity.setFire(2);
        }
    }

    private void replaceWings(InventoryPlayer inventoryplayer) {
        for (int i = 0; i < inventoryplayer.getSizeInventory(); i++) {
            if (inventoryplayer.getStackInSlot(i) == null)
                continue;

            ItemStack itemstack = inventoryplayer.getStackInSlot(i);

            if (itemstack.getItem() instanceof CrystalWingBurning) {
                itemstack = new ItemStack(crystalWingBurnt);
                inventoryplayer.setInventorySlotContents(i, itemstack);
            }
        }

    }
}
