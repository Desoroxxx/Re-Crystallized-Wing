package io.redstudioragnarok.recrystallizedwing.items;

import io.redstudioragnarok.recrystallizedwing.RCW;
import io.redstudioragnarok.recrystallizedwing.config.RCWConfig;
import net.jafama.FastMath;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import static io.redstudioragnarok.recrystallizedwing.RCW.teleportPlayer;

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
            final RayTraceResult rayTraceResult = rayTraceWithExtendedReach(world, player);

            if ((rayTraceResult != null) && rayTraceResult.typeOfHit.equals(RayTraceResult.Type.BLOCK)) {
                final BlockPos.MutableBlockPos target = new BlockPos.MutableBlockPos(rayTraceResult.getBlockPos());

                if (player.capabilities.isFlying)
                    target.setY(Math.max((int) player.posY, RCW.getHighestSolidBlock(world, target, true)));

                teleportPlayer(world, player, target.getX(), target.getY(), target.getZ(), 40);

                if (RCWConfig.common.crystalwingdurability > 0)
                    itemStack.damageItem(1, player);

                player.getCooldownTracker().setCooldown(this, RCWConfig.common.endersceptercooldown);

                return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
            }
        }

        return new ActionResult<>(EnumActionResult.PASS, itemStack);
    }

    /**
     * Performs a ray trace with extended reach in the world using the player's position, rotation and by calculating the reach with the config options.
     *
     * @param world The world in which to perform the ray trace
     * @param player The player whose position and rotation to use for the ray trace
     * @return A RayTraceResult object containing information about the block that was hit (if any)
     */
    private RayTraceResult rayTraceWithExtendedReach(final World world, final EntityPlayer player) {
        final Vec3d startPosition = new Vec3d(player.posX, player.posY + player.getEyeHeight(), player.posZ);

        final float yaw = player.rotationYaw;
        final float cosYaw = MathHelper.cos(-yaw * 0.017453292F - (float) FastMath.PI);
        final float sinYaw = MathHelper.sin(-yaw * 0.017453292F - (float) FastMath.PI);

        final float pitch = player.rotationPitch;
        final float cosPitch = -MathHelper.cos(-pitch * 0.017453292F);
        final float sinPitch = MathHelper.sin(-pitch * 0.017453292F);

        final float reachMultiplier = RCWConfig.common.enderscepterreachmult * RCWConfig.common.endersceptercreativereachmult;

        final Vec3d endPosition = startPosition.add((sinYaw * cosPitch) * reachMultiplier, sinPitch * reachMultiplier, (cosYaw * cosPitch) * reachMultiplier);

        return world.rayTraceBlocks(startPosition, endPosition, false, true, false);
    }
}
