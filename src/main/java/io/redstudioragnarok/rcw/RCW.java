package io.redstudioragnarok.rcw;

import io.redstudioragnarok.rcw.items.CrystalWing;
import io.redstudioragnarok.rcw.items.CrystalWingBurning;
import io.redstudioragnarok.rcw.utils.ModReference;
import net.minecraft.advancements.Advancement;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = ModReference.id, name = ModReference.name, version = ModReference.version)
public class RCW {

    public static boolean showInActionBar = false;

    public static int uses = 0;

    public static Item crystalWing, crystalWingBurning, crystalWingBurnt, enderScepter;

    // Todo: Register Advancements.
    public static Advancement burntWing;

    // Todo: Move this somewhere else.
    public static void spawnExplosionParticleAtEntity(Entity entity) {
        for (int i = 0; i < 20; ++i) {
            double d0 = entity.world.rand.nextGaussian() * 0.02D;
            double d1 = entity.world.rand.nextGaussian() * 0.02D;
            double d2 = entity.world.rand.nextGaussian() * 0.02D;
            double d3 = 10.0D;
            entity.world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, (entity.posX + (entity.world.rand.nextFloat() * entity.width * 2.0F)) - entity.width - (d0 * d3), (entity.posY + (entity.world.rand.nextFloat() * entity.height)) - (d1 * d3), (entity.posZ + (entity.world.rand.nextFloat() * entity.width * 2.0F)) - entity.width - (d2 * d3), d0, d1, d2);
        }
    }

    // Todo: Move this somewhere else.
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

    public static void initItems() {

    }

    // Todo: Move this somewhere else.
    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> itemRegistryEvent) {
        crystalWing = new CrystalWing();
        crystalWing.setTranslationKey("crystal_wing");
        crystalWing.setRegistryName("crystal_wing");
        /*
        crystalWingBurning = (new CrystalWingBurning()).setTranslationKey("crystal_wing_burning");
        crystalWingBurnt = (new CrystalWingBurnt()).setTranslationKey("crystal_wing_burnt");
        enderScepter = (new EnderScepter()).setTranslationKey("ender_scepter");
         */
        itemRegistryEvent.getRegistry().registerAll(crystalWing/*, crystalWingBurning, crystalWingBurnt, enderScepter*/);
    }


    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void registerRenders(ModelRegistryEvent modelRegistryEvent) {
        ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
        mesher.register(crystalWing, 0, new ModelResourceLocation("rcw:crystal_wing", "inventory"));
    }
}
