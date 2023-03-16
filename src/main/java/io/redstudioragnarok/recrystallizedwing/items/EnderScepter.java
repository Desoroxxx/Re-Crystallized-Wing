package io.redstudioragnarok.recrystallizedwing.items;

import io.redstudioragnarok.recrystallizedwing.config.RCWConfig;
import io.redstudioragnarok.recrystallizedwing.utils.RCWUtils;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EnderScepter extends Item {

    public EnderScepter() {
        setCreativeTab(CreativeTabs.TRANSPORTATION);

        maxStackSize = 1;

        if (RCWConfig.common.enderscepterdurability > 0)
            this.setMaxDamage(RCWConfig.common.crystalwingdurability - 1);
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

                RCWUtils.teleportPlayer(world, player, target.getX(), target.getY(), target.getZ(), 40);

                if (RCWConfig.common.crystalwingdurability > 0)
                    itemStack.damageItem(1, player);

                player.getCooldownTracker().setCooldown(this, RCWConfig.common.endersceptercooldown);

                return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
            }
        }

        return new ActionResult<>(EnumActionResult.PASS, itemStack);
    }
}
