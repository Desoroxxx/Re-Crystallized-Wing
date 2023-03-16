package io.redstudioragnarok.recrystallizedwing.items;

import io.redstudioragnarok.recrystallizedwing.RCW;
import io.redstudioragnarok.recrystallizedwing.config.RCWConfig;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import static io.redstudioragnarok.recrystallizedwing.RCW.burningWing;
import static io.redstudioragnarok.recrystallizedwing.RCW.teleportPlayer;

public class CrystalWing extends Item {

    public CrystalWing() {
        setCreativeTab(CreativeTabs.TRANSPORTATION);

        maxStackSize = 1;

        if (RCWConfig.common.crystalwingdurability > 0)
            this.setMaxDamage(RCWConfig.common.crystalwingdurability - 1);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(final World world, final EntityPlayer player, final EnumHand hand) {
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

                    isSafe = RCW.verifyTeleportCoordinates(world, targetLocation);
                }

                player.sendStatusMessage(new TextComponentTranslation("teleport.chatMessage"), RCWConfig.client.showinactionbar);

                teleportPlayer(world, player, targetLocation.getX(), targetLocation.getY(), targetLocation.getZ(), 40);
            } else if (player.dimension == -1) {
                world.playSound(null, player.getPosition(), SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.PLAYERS , 1.0F, 1.0F);
                itemStack = new ItemStack(burningWing, 1);
            } else {
                RCW.randomTeleport(world, player);
            }

            if (RCWConfig.common.crystalwingdurability > 0)
                itemStack.damageItem(1, player);

            player.getCooldownTracker().setCooldown(this, RCWConfig.common.crystalwingcooldown);

            return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
        }

        return new ActionResult<>(EnumActionResult.PASS, itemStack);
    }
}
