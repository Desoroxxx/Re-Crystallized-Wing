package dev.redstudio.recrystallizedwing.items;

import dev.redstudio.recrystallizedwing.RCW;
import dev.redstudio.recrystallizedwing.config.RCWConfig;
import dev.redstudio.recrystallizedwing.utils.RCWUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public final class CrystalWing extends BaseItem {

    private boolean playNotes;

    private int notesStartTick;

    public CrystalWing() {
        super(RCWConfig.common.durability.crystalWingDurability);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(final World world, final EntityPlayer player, final EnumHand hand) {
        ItemStack itemStack = player.getHeldItem(hand);

        if (world.isRemote)
            return new ActionResult<>(EnumActionResult.PASS, itemStack);

        if (player.dimension == 0) {
            BlockPos targetLocation = player.getBedLocation(player.dimension);

            if (targetLocation == null) {
                targetLocation = world.getSpawnPoint();

                final BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos(targetLocation);

                while (!RCWUtils.verifyTeleportCoordinates(world, mutablePos)) {
                    mutablePos.move(EnumFacing.SOUTH);
                }

                targetLocation = mutablePos.toImmutable();
            } else {
                final IBlockState blockState = world.getBlockState(targetLocation);
                targetLocation = blockState.getBlock().getBedSpawnPosition(blockState, world, targetLocation, null);
            }

            player.sendStatusMessage(new TextComponentTranslation("teleport.chatMessage"), RCWConfig.common.showInActionBar);

            RCWUtils.teleportPlayer(world, player, targetLocation, 40);

            if (RCWConfig.common.nostalgicSounds) {
                playNotes = true;
                notesStartTick = player.ticksExisted;
            }
        } else if (player.dimension == -1) {
            world.playSound(null, player.getPosition(), SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.PLAYERS, 1.0F, 1.0F);
            itemStack = new ItemStack(RCW.burningWing, 1);
        } else {
            RCWUtils.randomTeleport(world, player);
        }

        if (RCWConfig.common.durability.crystalWingDurability == 1)
            itemStack.damageItem(2, player);
        else if (RCWConfig.common.durability.crystalWingDurability > 0)
            itemStack.damageItem(1, player);

        player.getCooldownTracker().setCooldown(this, RCWConfig.common.cooldown.crystalWingCooldown);

        return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
    }

    @Override
    public void onUpdate(final ItemStack itemStack, final World world, final Entity entity, final int itemSlot, final boolean flag) {
        if (world.isRemote || !playNotes)
            return;

        final EntityPlayer player = (EntityPlayer) entity;

        switch ((player.ticksExisted - notesStartTick)) {
            case 1:
                RCWUtils.playPlingAtPitch(world, player, 0.79F);
                break;
            case 5:
                RCWUtils.playPlingAtPitch(world, player, 1.18F);
                break;
            case 7:
                RCWUtils.playPlingAtPitch(world, player, 1.49F);
                break;
        }
    }

    public EnumRarity getForgeRarity(final ItemStack itemStack) {
        return EnumRarity.UNCOMMON;
    }
}
