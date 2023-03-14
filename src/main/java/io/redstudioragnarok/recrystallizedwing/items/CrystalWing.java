package io.redstudioragnarok.recrystallizedwing.items;

import io.redstudioragnarok.recrystallizedwing.RCW;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import static io.redstudioragnarok.recrystallizedwing.RCW.crystalWingBurning;

public class CrystalWing extends Item {

    public CrystalWing() {
        setCreativeTab(CreativeTabs.TRANSPORTATION);

        maxStackSize = 1;

        if (RCW.crystalWingDurability > 0)
            this.setMaxDamage(RCW.crystalWingDurability - 1);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack itemStack = player.getHeldItem(hand);

        if (!world.isRemote) {
            if (player.dimension == 0) {
                BlockPos targetLocation = player.getBedLocation(player.dimension);

                boolean isSafe;

                if (targetLocation == null) {
                    targetLocation = world.getSpawnPoint();
                    isSafe = false;
                } else {
                    IBlockState iBlockState = world.getBlockState(targetLocation);
                    targetLocation = iBlockState.getBlock().getBedSpawnPosition(iBlockState, world, targetLocation, null);
                    isSafe = true;
                }

                while (!isSafe) {
                    BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos(targetLocation);

                    mutablePos.move(EnumFacing.SOUTH);

                    targetLocation = mutablePos.toImmutable();

                    isSafe = RCW.verifyRespawnCoordinates(world, targetLocation);
                }

                player.sendStatusMessage(new TextComponentTranslation("teleport.chatMessage"), RCW.showInActionBar);

                RCW.spawnExplosionParticleAtEntity(player, 80);

                player.setPositionAndUpdate(targetLocation.getX(), targetLocation.getY(), targetLocation.getZ());

                while (!world.getCollisionBoxes(player, player.getEntityBoundingBox()).isEmpty()) {
                    player.setPositionAndUpdate(player.posX + 0.5, player.posY + 1, player.posZ + 0.5);
                }

                world.playSound(null, player.getPosition(), SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.MASTER, 1.0F, 1.0F);

                RCW.spawnExplosionParticleAtEntity(player, 80);
            } else if (player.dimension == -1) {
                itemStack = null;
                world.playSound(null, player.getPosition(), SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.PLAYERS , 1.0F, 1.0F);
                itemStack = new ItemStack(crystalWingBurning, 1);
                return new ActionResult<>(EnumActionResult.FAIL, itemStack);
            } else {
                RCW.randomTeleport(world, player);
            }

            if (RCW.crystalWingDurability > 0)
                itemStack.damageItem(1, player);

            player.getCooldownTracker().setCooldown(this, RCW.crystalWingCooldown);
        }

        return new ActionResult<>(EnumActionResult.PASS, itemStack);
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
}