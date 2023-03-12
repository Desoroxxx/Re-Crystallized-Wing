package io.redstudioragnarok.recrystallizedwing.items;

import io.redstudioragnarok.recrystallizedwing.RCW;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import static io.redstudioragnarok.recrystallizedwing.RCW.crystalWingBurning;

public class CrystalWing extends Item {

    public CrystalWing() {
        maxStackSize = 1;
        this.setCreativeTab(CreativeTabs.TRANSPORTATION);

        if (RCW.crystalWingDurability > 0) {
            this.setMaxDamage(RCW.crystalWingDurability - 1);
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack itemStack = player.getHeldItem(hand);

        if (!world.isRemote && isCooledDown(itemStack)) {
            if (player.dimension == -1) {
                itemStack = null;
                world.playSound(null, player.getPosition(), SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.PLAYERS , 1.0F, 1.0F);
                itemStack = new ItemStack(crystalWingBurning, 1);
                return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemStack);
            }

            BlockPos chunkCoords = player.getBedLocation(player.dimension);

            if (chunkCoords == null)
                chunkCoords = world.getSpawnPoint();

            chunkCoords = RCW.verifyRespawnCoordinates(world, chunkCoords, false);

            if (chunkCoords == null)
                chunkCoords = world.getSpawnPoint();

            player.sendStatusMessage(new TextComponentTranslation("teleport.chatMessage"), RCW.showInActionBar);

            player.rotationPitch = 0.0F;
            player.rotationYaw = 0.0F;
            player.setPositionAndUpdate(chunkCoords.getX(), chunkCoords.getY() + 0.1D, chunkCoords.getZ());

            while (!world.getCollisionBoxes(player, player.getEntityBoundingBox()).isEmpty()) {
                player.setPositionAndUpdate(player.posX, player.posY + 1.0D, player.posZ);
            }

            world.playSound(null, player.getPosition(), SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);
            RCW.spawnExplosionParticleAtEntity(player);

            if (RCW.crystalWingDurability > 0)
                itemStack.damageItem(1, player);

            setCoolDown(itemStack, (short) 40);
        }

        return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStack);
    }

    @Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int i, boolean flag) {
        if (itemStack.hasTagCompound()) {
            NBTTagCompound tag = itemStack.getTagCompound();
            short cooldown = tag.getShort("cooldown");
            if (cooldown >= 0)
                tag.setShort("cooldown", (short) (cooldown - 1));
        } else {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setShort("cooldown", (short) 0);
            itemStack.setTagCompound(tag);
        }
    }

    private void setCoolDown(ItemStack itemStack, short cooldown) {
        if (itemStack.hasTagCompound()) {
            NBTTagCompound tag = itemStack.getTagCompound();
            tag.setShort("cooldown", cooldown);
        } else {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setShort("cooldown", cooldown);
            itemStack.setTagCompound(tag);
        }
    }

    private boolean isCooledDown(ItemStack itemStack) {
        short cooldown = (short) 0;
        if (itemStack.hasTagCompound()) {
            NBTTagCompound tag = itemStack.getTagCompound();
            cooldown = tag.getShort("cooldown");
        } else {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setShort("cooldown", (short) 0);
            itemStack.setTagCompound(tag);
        }
        return cooldown <= 0;
    }
}
