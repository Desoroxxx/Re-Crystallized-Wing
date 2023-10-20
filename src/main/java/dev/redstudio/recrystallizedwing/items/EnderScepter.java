package dev.redstudio.recrystallizedwing.items;

import dev.redstudio.recrystallizedwing.config.RCWServerConfig;
import dev.redstudio.recrystallizedwing.utils.RCWUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

public final class EnderScepter extends BaseItem {

    public EnderScepter(final Properties properties) {
        super(properties.rarity(Rarity.UNCOMMON), RCWServerConfig.enderScepterDurability);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(final Level level, final Player player, final InteractionHand hand) {
        final ItemStack itemStack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            final HitResult hitResult = RCWUtils.rayTraceWithExtendedReach(level, player);

            if (hitResult.getType().equals(HitResult.Type.BLOCK)) {
                final BlockPos.MutableBlockPos target = new BlockPos(hitResult.getLocation()).mutable();

                if (player.getAbilities().flying)
                    target.setY(Math.max((int) player.getY(), RCWUtils.getHighestSolidBlock(level, target, true)));

                RCWUtils.teleportPlayer(level, player, target, 40);

                if (RCWServerConfig.enderScepterDurability == 1)
                    itemStack.hurtAndBreak(2, player, player1 -> player1.broadcastBreakEvent(hand == InteractionHand.MAIN_HAND  ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND));
                else if (RCWServerConfig.enderScepterDurability > 0)
                    itemStack.hurtAndBreak(1, player, player1 -> player1.broadcastBreakEvent(hand == InteractionHand.MAIN_HAND  ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND));

                player.getCooldowns().addCooldown(this, RCWServerConfig.enderScepterCooldown);

                return InteractionResultHolder.success(itemStack);
            }
        }

        return InteractionResultHolder.pass(itemStack);
    }
}
