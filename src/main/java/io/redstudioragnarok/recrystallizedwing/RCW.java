package io.redstudioragnarok.recrystallizedwing;

import io.redstudioragnarok.recrystallizedwing.items.CrystalWing;
import io.redstudioragnarok.recrystallizedwing.items.CrystalWingBurning;
import io.redstudioragnarok.recrystallizedwing.items.CrystalWingBurnt;
import io.redstudioragnarok.recrystallizedwing.items.EnderScepter;
import io.redstudioragnarok.recrystallizedwing.utils.ModReference;
import net.minecraft.advancements.Advancement;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = ModReference.id, name = ModReference.name, version = ModReference.version)
@Mod.EventBusSubscriber
public class RCW {

    // Todo: Make config system with default Forge
    // Todo: Add Crystal Wing to loot tables and Burnt Wing to loot tables in place where it's hot
    // Todo: Add Ender Scepter to the end loot tables
    // Todo: Add the achievements

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

    // Todo: Make the particles actually spawn
    public static void spawnExplosionParticleAtEntity(Entity entity) {
        for (int i = 0; i < 20; ++i) {
            double d0 = entity.world.rand.nextGaussian() * 0.02D;
            double d1 = entity.world.rand.nextGaussian() * 0.02D;
            double d2 = entity.world.rand.nextGaussian() * 0.02D;
            double d3 = 10.0D;
            entity.world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, (entity.posX + (entity.world.rand.nextFloat() * entity.width * 2.0F)) - entity.width - (d0 * d3), (entity.posY + (entity.world.rand.nextFloat() * entity.height)) - (d1 * d3), (entity.posZ + (entity.world.rand.nextFloat() * entity.width * 2.0F)) - entity.width - (d2 * d3), d0, d1, d2);
        }
    }

    // Todo: Move to somewhere better
    public static BlockPos verifyRespawnCoordinates(World world, BlockPos blockPos, boolean checkForSafety) {
        IBlockState iBlockState = world.getBlockState(blockPos);
        Block block = iBlockState.getBlock();

        if (block.equals(Blocks.BED) || block.isBed(iBlockState, world, blockPos, null)) {
            return block.getBedSpawnPosition(iBlockState, world, blockPos, null);
        } else {
            Material bottomBlockMaterial = block.getMaterial(iBlockState);
            Material topBlockMaterial = world.getBlockState(blockPos.up()).getBlock().getMaterial(iBlockState);

            boolean bottomSafe = !bottomBlockMaterial.isSolid() && !bottomBlockMaterial.isLiquid();
            boolean topSafe = !topBlockMaterial.isSolid() && !topBlockMaterial.isLiquid();

            return checkForSafety && bottomSafe && topSafe ? blockPos : null;
        }
    }

    // Todo: Move to somewhere better
    public static int getHighestSolidBlock(World world, BlockPos pos, Boolean skipNonNormalCubeAndWood) {
        int x = pos.getX();
        int z = pos.getZ();
        int y = world.getActualHeight();

        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        mutablePos.setPos(x, y, z);

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
    public static void registerItems(RegistryEvent.Register<Item> itemRegistryEvent) {
        crystalWing = new CrystalWing().setTranslationKey("crystal_wing").setRegistryName(ModReference.id, "crystal_wing");
        crystalWingBurning = new CrystalWingBurning().setTranslationKey("crystal_wing_burning").setRegistryName(ModReference.id, "crystal_wing_burning");
        crystalWingBurnt = new CrystalWingBurnt().setTranslationKey("crystal_wing_burnt").setRegistryName(ModReference.id, "crystal_wing_burnt");
        enderScepter = new EnderScepter().setTranslationKey("ender_scepter").setRegistryName(ModReference.id, "ender_scepter");

        itemRegistryEvent.getRegistry().registerAll(crystalWing, crystalWingBurning, crystalWingBurnt, enderScepter);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void registerRenders(ModelRegistryEvent modelRegistryEvent) {
        ModelLoader.setCustomModelResourceLocation(crystalWing, 0, new ModelResourceLocation(crystalWing.delegate.name(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(crystalWingBurning, 0, new ModelResourceLocation(crystalWingBurning.delegate.name(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(crystalWingBurnt, 0, new ModelResourceLocation(crystalWingBurnt.delegate.name(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(enderScepter, 0, new ModelResourceLocation(enderScepter.delegate.name(), "inventory"));
    }
}
