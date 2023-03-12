package io.redstudioragnarok.recrystallizedwing.items;

import io.redstudioragnarok.recrystallizedwing.RCW;
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
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

import java.util.Random;

import static io.redstudioragnarok.recrystallizedwing.RCW.crystalWing;

public class CrystalWingBurnt extends Item {

    private final Random rand;

    public CrystalWingBurnt() {
        rand = new Random();
        this.setCreativeTab(CreativeTabs.TRANSPORTATION);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack itemStack = player.getHeldItem(hand);

        if (!world.isRemote) {
            if (player.dimension == -1) {
                return crystalWing.onItemRightClick(world, player, hand);
            }
            world.playSound(null, player.getPosition(), SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.PLAYERS , 1.0F, 1.0F);
            int dX = (int) ((player.posX + rand.nextInt(RCW.teleDistance * 2)) - RCW.teleDistance);
            int dZ = (int) ((player.posZ + rand.nextInt(RCW.teleDistance * 2)) - RCW.teleDistance);
            BlockPos c = new BlockPos(dX, RCW.getHighestSolidBlock(world, new BlockPos(dX, 0, dZ), false), dZ);

            player.rotationPitch = 0.0F;
            player.rotationYaw = 0.0F;
            player.setPositionAndUpdate(dX + 0.5D, c.getY() + 0.1D, dZ);

            while (!world.getCollisionBoxes(player, player.getEntityBoundingBox()).isEmpty()) {
                player.setPositionAndUpdate(player.posX, player.posY + 1.0D, player.posZ);
            }

            world.playSound(null, player.getPosition(), SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);
            RCW.spawnExplosionParticleAtEntity(player);
        }

        return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStack);
    }
}
