package dev.redstudio.recrystallizedwing.items;

import dev.redstudio.recrystallizedwing.utils.RCWUtils;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import static dev.redstudio.recrystallizedwing.RCW.BURNT_WING;

public final class BurningWing extends BaseItem {

    public BurningWing(final Properties properties) {
        super(properties, 1);
    }

    @Override
    public void inventoryTick(final ItemStack itemStack, final Level world, final Entity entity, final int itemSlot, final boolean flag) {
        if (world.isClientSide)
            return;

        if (entity.isInWater()) {
            if (!(entity instanceof Player player))
                return;

            world.playSound(null, player.blockPosition(), SoundEvents.LAVA_EXTINGUISH, SoundSource.MASTER, 1, 1);
            RCWUtils.spawnExplosionParticleAtEntity(player, 160);

            player.getInventory().setItem(itemSlot, new ItemStack(BURNT_WING.get()));
        } else if (!entity.isOnFire())
            entity.setSecondsOnFire(2);
    }
}
