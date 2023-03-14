package io.redstudioragnarok.recrystallizedwing;

import io.redstudioragnarok.recrystallizedwing.items.CrystalWing;
import io.redstudioragnarok.recrystallizedwing.items.CrystalWingBurning;
import io.redstudioragnarok.recrystallizedwing.items.CrystalWingBurnt;
import io.redstudioragnarok.recrystallizedwing.items.EnderScepter;
import io.redstudioragnarok.recrystallizedwing.utils.ModReference;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

@Mod(modid = ModReference.id, name = ModReference.name, version = ModReference.version)
@Mod.EventBusSubscriber
public class RCW {

    // Todo: Make config system with default Forge
    // Todo: Add Crystal Wing to loot tables and Burnt Wing to loot tables in place where it's hot
    // Todo: Add Ender Scepter to the end loot tables
    // Todo: Add the achievements for getting the ender scepter & extinguishing the burning crystal wing

    public static boolean showInActionBar = false;
    public static boolean onlyTeleportIfSafe = true;

    public static int crystalWingDurability = 8;
    public static int enderScepterDurability = 8;
    public static int teleDistance = 500;
    public static int enderScepterReachMult = 25;
    public static int enderScepterCreativeReachMult = 4;

    public static Item crystalWing, crystalWingBurning, crystalWingBurnt, enderScepter;

    public RCW() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static void spawnExplosionParticleAtEntity(final Entity entity, final int amount) {
        Random random = new Random();
        double velocity = random.nextGaussian() / 8;
        double xOffset = random.nextGaussian() / 12;
        double yOffset = random.nextGaussian() / 12;
        double zOffset = random.nextGaussian() / 12;
        ((WorldServer)entity.getEntityWorld()).spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, entity.posX, entity.posY, entity.posZ, amount, xOffset, yOffset, zOffset, velocity);
    }

    // Todo: Move to somewhere better
    public static boolean verifyRespawnCoordinates(final World world, final BlockPos blockPos) {
        IBlockState iBlockState = world.getBlockState(blockPos);
        Block block = iBlockState.getBlock();

        Material floorBlockMaterial = world.getBlockState(blockPos.down()).getBlock().getMaterial(iBlockState);
        Material bottomBlockMaterial = block.getMaterial(iBlockState);
        Material topBlockMaterial = world.getBlockState(blockPos.up()).getBlock().getMaterial(iBlockState);

        boolean floorSafe =  floorBlockMaterial.isSolid() || floorBlockMaterial.isLiquid();
        boolean bottomSafe = !bottomBlockMaterial.isSolid() && !bottomBlockMaterial.isLiquid();
        boolean topSafe = !topBlockMaterial.isSolid() && !topBlockMaterial.isLiquid();

        return floorSafe && bottomSafe && topSafe;
    }

    // Todo: Move to somewhere better
    public static int getHighestSolidBlock(final World world, final BlockPos blockPos, final Boolean skipNonNormalCubeAndWood) {
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos(blockPos);
        mutablePos.setY(world.getActualHeight());

        if (!skipNonNormalCubeAndWood) {
            while ((mutablePos.getY() > 0) && world.isAirBlock(mutablePos)) {
                mutablePos.move(EnumFacing.DOWN);
            }
        } else {
            while ((mutablePos.getY() > 0) && (world.isAirBlock(mutablePos) && !world.isBlockNormalCube(mutablePos, true) || world.getBlockState(mutablePos).getBlock().isWood(world, mutablePos))) {
                mutablePos.move(EnumFacing.DOWN);
            }
        }

        return mutablePos.getY();
    }


    @SubscribeEvent
    public static void registerItems(final RegistryEvent.Register<Item> itemRegistryEvent) {
        crystalWing = new CrystalWing().setTranslationKey("crystal_wing").setRegistryName(ModReference.id, "crystal_wing");
        crystalWingBurning = new CrystalWingBurning().setTranslationKey("crystal_wing_burning").setRegistryName(ModReference.id, "crystal_wing_burning");
        crystalWingBurnt = new CrystalWingBurnt().setTranslationKey("crystal_wing_burnt").setRegistryName(ModReference.id, "crystal_wing_burnt");
        enderScepter = new EnderScepter().setTranslationKey("ender_scepter").setRegistryName(ModReference.id, "ender_scepter");

        itemRegistryEvent.getRegistry().registerAll(crystalWing, crystalWingBurning, crystalWingBurnt, enderScepter);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void registerRenders(final ModelRegistryEvent modelRegistryEvent) {
        ModelLoader.setCustomModelResourceLocation(crystalWing, 0, new ModelResourceLocation(crystalWing.delegate.name(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(crystalWingBurning, 0, new ModelResourceLocation(crystalWingBurning.delegate.name(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(crystalWingBurnt, 0, new ModelResourceLocation(crystalWingBurnt.delegate.name(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(enderScepter, 0, new ModelResourceLocation(enderScepter.delegate.name(), "inventory"));
    }

    // Todo: Move to somewhere better
    public static BlockPos randomTeleport(final World world, final EntityPlayer player) {
        final Random random = new Random();

        boolean isSafe = false;

        RCW.spawnExplosionParticleAtEntity(player, 80);

        while (!isSafe) {
            int randomX = (int) ((player.posX + random.nextInt(RCW.teleDistance * 2)) - RCW.teleDistance);
            int randomZ = (int) ((player.posZ + random.nextInt(RCW.teleDistance * 2)) - RCW.teleDistance);

            BlockPos blockPos = new BlockPos(randomX, getHighestSolidBlock(world, new BlockPos(randomX, 0, randomZ), false), randomZ);

            player.setPositionAndUpdate(randomX + 0.5, blockPos.getY(), randomZ + 0.5);

            while (!world.getCollisionBoxes(player, player.getEntityBoundingBox()).isEmpty()) {
                player.setPositionAndUpdate(player.posX, player.posY + 1.0, player.posZ);
            }

            isSafe = RCW.verifyRespawnCoordinates(world, new BlockPos(blockPos.getX(), blockPos.getY() + 1, blockPos.getZ()));
        }

        world.playSound(null, player.getPosition(), SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.MASTER, 1.0F, 1.0F);

        RCW.spawnExplosionParticleAtEntity(player, 80);

        return null;
    }
}
