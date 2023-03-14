package io.redstudioragnarok.recrystallizedwing.items;

import io.redstudioragnarok.recrystallizedwing.RCW;
import net.jafama.FastMath;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EnderScepter extends Item {

    public EnderScepter() {
        setCreativeTab(CreativeTabs.TRANSPORTATION);

        maxStackSize = 1;

        if (RCW.enderScepterDurability > 0)
            this.setMaxDamage(RCW.crystalWingDurability - 1);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack itemStack = player.getHeldItem(hand);

        if (!world.isRemote) {
            RayTraceResult rayTraceResult = rayTraceWithoutReach(world, player);
            if ((rayTraceResult != null) && rayTraceResult.typeOfHit.equals(RayTraceResult.Type.BLOCK)) {
                BlockPos chunkCoords;
                chunkCoords = rayTraceResult.getBlockPos();


                if (player.capabilities.isFlying) {
                    chunkCoords = new BlockPos(chunkCoords.getX(), Math.max((int) player.posY, RCW.getHighestSolidBlock(world, chunkCoords, true)), chunkCoords.getZ());
                }

                RCW.spawnExplosionParticleAtEntity(player, 40);

                player.setPositionAndUpdate(chunkCoords.getX() + 0.5D, chunkCoords.getY() + 0.1D, chunkCoords.getZ());

                while (!world.getCollisionBoxes(player, player.getEntityBoundingBox()).isEmpty()) {
                    player.setPositionAndUpdate(player.posX, player.posY + 1.0D, player.posZ);
                }

                world.playSound(null, player.getPosition(), SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.MASTER, 1.0F, 1.0F);

                RCW.spawnExplosionParticleAtEntity(player, 40);

                if (RCW.crystalWingDurability > 0)
                    itemStack.damageItem(1, player);

                player.getCooldownTracker().setCooldown(this, RCW.enderScepterCooldown);
            }
        }

        return new ActionResult<>(EnumActionResult.PASS, itemStack);
    }

    protected RayTraceResult rayTraceWithoutReach(World world, EntityPlayer player) {
        float pitch = player.rotationPitch;
        float yaw = player.rotationYaw;
        double posX = player.posX;
        double poxY = player.posY + (double) player.getEyeHeight();
        double posZ = player.posZ;
        Vec3d vec3d = new Vec3d(posX, poxY, posZ);
        float f2 = MathHelper.cos(-yaw * 0.017453292F - (float) FastMath.PI);
        float f3 = MathHelper.sin(-yaw * 0.017453292F - (float) FastMath.PI);
        float f4 = -MathHelper.cos(-pitch * 0.017453292F);
        float f5 = MathHelper.sin(-pitch * 0.017453292F);
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        Vec3d vec3d1 = vec3d.add(f6 * (RCW.enderScepterReachMult * RCW.enderScepterCreativeReachMult), f5 * (RCW.enderScepterReachMult * RCW.enderScepterCreativeReachMult), f7 * (RCW.enderScepterReachMult * RCW.enderScepterCreativeReachMult));
        return world.rayTraceBlocks(vec3d, vec3d1, false, true, false);
    }
}
