package io.redstudioragnarok.recrystallizedwing.items;

import io.redstudioragnarok.recrystallizedwing.config.RCWConfig;
import io.redstudioragnarok.recrystallizedwing.utils.RCWUtils;
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

public class CrystalWing extends Item {

    public CrystalWing() {
        setCreativeTab(CreativeTabs.TRANSPORTATION);

        maxStackSize = 1;

        if (RCWConfig.common.durability.crystalwingdurability > 0)
            this.setMaxDamage(RCWConfig.common.durability.crystalwingdurability - 1);
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

                    isSafe = RCWUtils.verifyTeleportCoordinates(world, targetLocation);
                }

                player.sendStatusMessage(new TextComponentTranslation("teleport.chatMessage"), RCWConfig.client.showinactionbar);

                RCWUtils.teleportPlayer(world, player, targetLocation.getX(), targetLocation.getY(), targetLocation.getZ(), 40);
            } else if (player.dimension == -1) {
                world.playSound(null, player.getPosition(), SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.PLAYERS , 1.0F, 1.0F);
                itemStack = new ItemStack(burningWing, 1);
            } else {
                RCWUtils.randomTeleport(world, player);
            }

            if (RCWConfig.common.durability.crystalwingdurability > 0)
                itemStack.damageItem(1, player);

            player.getCooldownTracker().setCooldown(this, RCWConfig.common.cooldown.crystalwingcooldown);

            return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
        }

        return new ActionResult<>(EnumActionResult.PASS, itemStack);
    }
}
