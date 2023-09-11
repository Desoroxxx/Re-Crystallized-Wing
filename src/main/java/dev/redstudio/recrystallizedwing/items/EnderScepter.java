package dev.redstudio.recrystallizedwing.items;

import dev.redstudio.recrystallizedwing.config.RCWConfig;
import dev.redstudio.recrystallizedwing.utils.RCWUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public final class EnderScepter extends BaseItem {

    public EnderScepter() {
        super(RCWConfig.common.durability.enderScepterDurability);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(final World world, final EntityPlayer player, final EnumHand hand) {
        final ItemStack itemStack = player.getHeldItem(hand);

        if (!world.isRemote) {
            final RayTraceResult rayTraceResult = RCWUtils.rayTraceWithExtendedReach(world, player);

            if ((rayTraceResult != null) && rayTraceResult.typeOfHit.equals(RayTraceResult.Type.BLOCK)) {
                final BlockPos.MutableBlockPos target = new BlockPos.MutableBlockPos(rayTraceResult.getBlockPos());

                if (player.capabilities.isFlying)
                    target.setY(Math.max((int) player.posY, RCWUtils.getHighestSolidBlock(world, target, true)));

                RCWUtils.teleportPlayer(world, player, target, 40);

                if (RCWConfig.common.durability.enderScepterDurability == 1)
                    itemStack.damageItem(2, player);
                else if (RCWConfig.common.durability.enderScepterDurability > 0)
                    itemStack.damageItem(1, player);

                player.getCooldownTracker().setCooldown(this, RCWConfig.common.cooldown.enderScepterCooldown);

                return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
            }
        }

        return new ActionResult<>(EnumActionResult.PASS, itemStack);
    }

    public EnumRarity getForgeRarity(final ItemStack itemStack) {
        return EnumRarity.UNCOMMON;
    }
}
