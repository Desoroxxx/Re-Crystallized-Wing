package dev.redstudio.recrystallizedwing.items;

import dev.redstudio.recrystallizedwing.config.RCWConfig;
import dev.redstudio.recrystallizedwing.utils.RCWUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import static dev.redstudio.recrystallizedwing.RCW.crystalWing;

public final class BurntWing extends BaseItem {

    public BurntWing() {
        super(RCWConfig.common.durability.BurntWingDurability);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(final World world, final EntityPlayer player, final EnumHand hand) {
        ItemStack itemStack = player.getHeldItem(hand);

        if (world.isRemote)
            return new ActionResult<>(EnumActionResult.PASS, itemStack);

        // If in the nether replace by a normal crystal wing and use it, which if in the nether will replace it by a burning crystal wing
        if (player.dimension == -1)
            return crystalWing.onItemRightClick(world, player, hand);

        RCWUtils.randomTeleport(world, player);

        if (RCWConfig.common.durability.BurntWingDurability == 1)
            itemStack.damageItem(2, player);
        else if (RCWConfig.common.durability.BurntWingDurability > 0)
            itemStack.damageItem(1, player);

        player.getCooldownTracker().setCooldown(this, RCWConfig.common.cooldown.burntWingCooldown);

        return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
    }
}
