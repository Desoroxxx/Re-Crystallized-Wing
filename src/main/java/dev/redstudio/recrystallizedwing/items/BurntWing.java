package dev.redstudio.recrystallizedwing.items;

import dev.redstudio.recrystallizedwing.config.RCWServerConfig;
import dev.redstudio.recrystallizedwing.utils.RCWUtils;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import static dev.redstudio.recrystallizedwing.RCW.CRYSTAL_WING_ITEM;

public final class BurntWing extends BaseItem {

    public BurntWing(final Properties properties) {
        super(properties, RCWServerConfig.burntWingDurability);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(final Level level, final Player player, final InteractionHand hand) {
        final ItemStack itemStack = player.getItemInHand(hand);

        if (level.isClientSide)
            return InteractionResultHolder.pass(itemStack);

        // If in the nether replace by a normal crystal wing and use it, which if in the nether will replace it by a burning crystal wing
        if (player.getLevel().dimension() == Level.NETHER)
            return CRYSTAL_WING_ITEM.get().use(level, player, hand);

        RCWUtils.randomTeleport(level, player);

        if (RCWServerConfig.burntWingDurability == 1)
            itemStack.hurtAndBreak(2, player, player1 -> player1.broadcastBreakEvent(hand == InteractionHand.MAIN_HAND  ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND));
        else if (RCWServerConfig.burntWingDurability > 0)
            itemStack.hurtAndBreak(1, player, player1 -> player1.broadcastBreakEvent(hand == InteractionHand.MAIN_HAND  ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND));

        player.getCooldowns().addCooldown(this, RCWServerConfig.burntWingCooldown);

        return InteractionResultHolder.success(itemStack);
    }
}
