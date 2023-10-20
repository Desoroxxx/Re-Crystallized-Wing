package dev.redstudio.recrystallizedwing.items;

import dev.redstudio.recrystallizedwing.config.RCWClientConfig;
import dev.redstudio.recrystallizedwing.config.RCWServerConfig;
import dev.redstudio.recrystallizedwing.utils.RCWUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import static dev.redstudio.recrystallizedwing.RCW.BURNING_WING;


public final class CrystalWing extends BaseItem {

    private boolean playNotes;

    private int notesStartTick;

    public CrystalWing(final Properties properties) {
        super(properties.rarity(Rarity.UNCOMMON), RCWServerConfig.crystalWingDurability);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(final Level level, final Player player, final InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        if (level.isClientSide)
            return InteractionResultHolder.pass(itemStack);

        if (player.getLevel().dimension() == Level.OVERWORLD) {
            BlockPos targetLocation = player.getSleepingPos().orElse(null);

            if (targetLocation == null) {
                targetLocation = level.getSharedSpawnPos();

                final BlockPos.MutableBlockPos mutablePos = targetLocation.mutable();

                while (!RCWUtils.verifyTeleportCoordinates(level, mutablePos))
                    mutablePos.move(Direction.SOUTH);

                targetLocation = mutablePos.immutable();
            } else {
                final BlockState blockState = level.getBlockState(targetLocation);
                targetLocation = new BlockPos(blockState.getRespawnPosition(EntityType.PLAYER, level, targetLocation, ((ServerPlayer) player).getRespawnAngle(), player).orElse(new Vec3(0, 0, 0)));
            }

            player.displayClientMessage(Component.translatable("recrystallizedwing.teleport.chatMessage"), RCWClientConfig.showInActionBar);

            RCWUtils.teleportPlayer(level, player, targetLocation, 40);

            if (RCWServerConfig.nostalgicSounds) {
                playNotes = true;
                notesStartTick = player.tickCount;
            }
        } else if (player.getLevel().dimension() == Level.NETHER) {
            level.playSound(null, player.blockPosition(), SoundEvents.FLINTANDSTEEL_USE, SoundSource.PLAYERS, 1, 1);
            itemStack = new ItemStack(BURNING_WING.get(), 1);
        } else {
            RCWUtils.randomTeleport(level, player);
        }

        if (RCWServerConfig.crystalWingDurability == 1)
            itemStack.hurtAndBreak(2, player, player1 -> player1.broadcastBreakEvent(hand == InteractionHand.MAIN_HAND  ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND));
        else if (RCWServerConfig.crystalWingDurability > 0)
            itemStack.hurtAndBreak(1, player, player1 -> player1.broadcastBreakEvent(hand == InteractionHand.MAIN_HAND  ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND));

        player.getCooldowns().addCooldown(this, RCWServerConfig.crystalWingCooldown);

        return InteractionResultHolder.success(itemStack);
    }

    @Override
    public void inventoryTick(final ItemStack itemStack, final Level world, final Entity entity, final int itemSlot, final boolean flag) {
        if (world.isClientSide || !playNotes)
            return;

        final Player player = (Player) entity;

        switch ((player.tickCount - notesStartTick)) {
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
}
