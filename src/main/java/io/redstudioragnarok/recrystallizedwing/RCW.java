package io.redstudioragnarok.recrystallizedwing;

import io.redstudioragnarok.recrystallizedwing.config.RCWConfig;
import io.redstudioragnarok.recrystallizedwing.items.BurningWing;
import io.redstudioragnarok.recrystallizedwing.items.BurntWing;
import io.redstudioragnarok.recrystallizedwing.items.CrystalWing;
import io.redstudioragnarok.recrystallizedwing.items.EnderScepter;
import io.redstudioragnarok.recrystallizedwing.utils.ModReference;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

//   /$$$$$$$             /$$$$$$                                  /$$               /$$ /$$ /$$                           /$$       /$$      /$$ /$$
//  | $$__  $$           /$$__  $$                                | $$              | $$| $$|__/                          | $$      | $$  /$ | $$|__/
//  | $$  \ $$  /$$$$$$ | $$  \__/  /$$$$$$  /$$   /$$  /$$$$$$$ /$$$$$$    /$$$$$$ | $$| $$ /$$ /$$$$$$$$  /$$$$$$   /$$$$$$$      | $$ /$$$| $$ /$$ /$$$$$$$   /$$$$$$
//  | $$$$$$$/ /$$__  $$| $$       /$$__  $$| $$  | $$ /$$_____/|_  $$_/   |____  $$| $$| $$| $$|____ /$$/ /$$__  $$ /$$__  $$      | $$/$$ $$ $$| $$| $$__  $$ /$$__  $$
//  | $$__  $$| $$$$$$$$| $$      | $$  \__/| $$  | $$|  $$$$$$   | $$      /$$$$$$$| $$| $$| $$   /$$$$/ | $$$$$$$$| $$  | $$      | $$$$_  $$$$| $$| $$  \ $$| $$  \ $$
//  | $$  \ $$| $$_____/| $$    $$| $$      | $$  | $$ \____  $$  | $$ /$$ /$$__  $$| $$| $$| $$  /$$__/  | $$_____/| $$  | $$      | $$$/ \  $$$| $$| $$  | $$| $$  | $$
//  | $$  | $$|  $$$$$$$|  $$$$$$/| $$      |  $$$$$$$ /$$$$$$$/  |  $$$$/|  $$$$$$$| $$| $$| $$ /$$$$$$$$|  $$$$$$$|  $$$$$$$      | $$/   \  $$| $$| $$  | $$|  $$$$$$$
//  |__/  |__/ \_______/ \______/ |__/       \____  $$|_______/    \___/   \_______/|__/|__/|__/|________/ \_______/ \_______/      |__/     \__/|__/|__/  |__/ \____  $$
//                                           /$$  | $$                                                                                                          /$$  \ $$
//                                          |  $$$$$$/                                                                                                         |  $$$$$$/
//                                           \______/                                                                                                           \______/
@Mod(modid = ModReference.id, name = ModReference.name, version = ModReference.version, updateJSON = "https://raw.githubusercontent.com/Red-Studio-Ragnarok/ReCrystallized-Wing/main/update.json")
@Mod.EventBusSubscriber
public class RCW {

    public static Item crystalWing, burningWing, burntWing, enderScepter;

    public static final Random random = new Random();

    public RCW() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent initializationEvent) {
        LootTableList.register(new ResourceLocation(ModReference.id, "end_loot"));
        LootTableList.register(new ResourceLocation(ModReference.id, "nether_loot"));
        LootTableList.register(new ResourceLocation(ModReference.id, "overworld_loot"));
    }

    @SubscribeEvent
    public static void registerItems(final RegistryEvent.Register<Item> itemRegistryEvent) {
        crystalWing = new CrystalWing().setTranslationKey("crystal_wing").setRegistryName(ModReference.id, "crystal_wing");
        burningWing = new BurningWing().setTranslationKey("burning_wing").setRegistryName(ModReference.id, "burning_wing");
        burntWing = new BurntWing().setTranslationKey("burnt_wing").setRegistryName(ModReference.id, "burnt_wing");
        enderScepter = new EnderScepter().setTranslationKey("ender_scepter").setRegistryName(ModReference.id, "ender_scepter");

        itemRegistryEvent.getRegistry().registerAll(crystalWing, burningWing, burntWing, enderScepter);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void registerRenders(final ModelRegistryEvent modelRegistryEvent) {
        ModelLoader.setCustomModelResourceLocation(crystalWing, 0, new ModelResourceLocation(crystalWing.delegate.name(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(burningWing, 0, new ModelResourceLocation(burningWing.delegate.name(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(burntWing, 0, new ModelResourceLocation(burntWing.delegate.name(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(enderScepter, 0, new ModelResourceLocation(enderScepter.delegate.name(), "inventory"));
    }

    @SubscribeEvent
    public void lootTableLoad(LootTableLoadEvent lootTableLoadEvent) {
        if (lootTableLoadEvent.getName().toString().equals("minecraft:chests/end_city_treasure")) {
            LootEntry entry = new LootEntryTable(new ResourceLocation(ModReference.id, "end_loot"), 1, 1, new LootCondition[0], "end_loot");

            LootPool pool = new LootPool(new LootEntry[] {entry}, new LootCondition[0], new RandomValueRange(1, 2), new RandomValueRange(1, 2), "end_loot");

            lootTableLoadEvent.getTable().addPool(pool);
        }
        if (lootTableLoadEvent.getName().toString().equals("minecraft:chests/nether_bridge")) {
            LootEntry entry = new LootEntryTable(new ResourceLocation(ModReference.id, "nether_loot"), 1, 1, new LootCondition[0], "nether_loot");

            LootPool pool = new LootPool(new LootEntry[] {entry}, new LootCondition[0], new RandomValueRange(1, 2), new RandomValueRange(1, 2), "nether_loot");

            lootTableLoadEvent.getTable().addPool(pool);
        }
        if (lootTableLoadEvent.getName().toString().equals("minecraft:chests/desert_pyramid") || lootTableLoadEvent.getName().toString().equals("minecraft:chests/spawn_bonus_chest") || lootTableLoadEvent.getName().toString().equals("minecraft:chests/simple_dungeon") || lootTableLoadEvent.getName().toString().equals("minecraft:chests/simple_dungeon") || lootTableLoadEvent.getName().toString().equals("minecraft:chests/stronghold_library")) {
            LootEntry entry = new LootEntryTable(new ResourceLocation(ModReference.id, "overworld_loot"), 1, 1, new LootCondition[0], "overworld_loot");

            LootPool pool = new LootPool(new LootEntry[] {entry}, new LootCondition[0], new RandomValueRange(1, 2), new RandomValueRange(1, 2), "overworld_loot");

            lootTableLoadEvent.getTable().addPool(pool);
        }
    }

    /**
     * Spawns an explosion particle effect around the given entity in the world.
     *
     * @param entity The entity around which to spawn the particle effect
     * @param amount The number of particles to spawn
     */
    public static void spawnExplosionParticleAtEntity(final Entity entity, final int amount) {
        final double velocity = random.nextGaussian() / 8;
        final double xOffset = random.nextGaussian() / 12;
        final double yOffset = random.nextGaussian() / 12;
        final double zOffset = random.nextGaussian() / 12;

        ((WorldServer)entity.getEntityWorld()).spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, entity.posX, entity.posY, entity.posZ, amount, xOffset, yOffset, zOffset, velocity);
    }

    /**
     * Verifies if the given block position is a safe teleport location for players.
     *
     * @param world The world in which to verify the respawn location
     * @param blockPos The block position to verify
     * @return True if the respawn location is safe, false otherwise
     */
    public static boolean verifyTeleportCoordinates(final World world, final BlockPos blockPos) {
        final IBlockState iBlockState = world.getBlockState(blockPos);

        final Material floorBlockMaterial = world.getBlockState(blockPos.down()).getMaterial();
        final Material bottomBlockMaterial = iBlockState.getMaterial();
        final Material topBlockMaterial = world.getBlockState(blockPos.up()).getMaterial();

        final boolean floorSafe =  floorBlockMaterial.isSolid() || floorBlockMaterial.isLiquid();
        final boolean bottomSafe = !bottomBlockMaterial.isSolid() && !bottomBlockMaterial.isLiquid();
        final boolean topSafe = !topBlockMaterial.isSolid() && !topBlockMaterial.isLiquid();

        return floorSafe && bottomSafe && topSafe;
    }

    /**
     * Finds the highest solid block at the given position in the world.
     *
     * @param world The world in which to search for the highest solid block
     * @param mutablePos A mutable block position of the position for the search
     * @param skipNonNormalCube A boolean flag indicating whether to skip non-normal cubes during the search
     * @return The Y coordinate of the highest solid block, or 0 if no solid block is found
     */
    public static int getHighestSolidBlock(final World world, final BlockPos.MutableBlockPos mutablePos, final Boolean skipNonNormalCube) {
        mutablePos.setY(world.getActualHeight());

        if (!skipNonNormalCube) {
            while ((mutablePos.getY() > 0) && world.isAirBlock(mutablePos))
                mutablePos.move(EnumFacing.DOWN);
        } else {
            while ((mutablePos.getY() > 0) && (world.isAirBlock(mutablePos) && !world.isBlockNormalCube(mutablePos, true)))
                mutablePos.move(EnumFacing.DOWN);
        }

        return mutablePos.getY();
    }

    /**
     * Teleports the given player to a new, randomly selected location within a specified distance from their current location the distance is specified via the config.
     *
     * @param world The world in which to teleport the player
     * @param player The player to teleport
     */
    public static void randomTeleport(final World world, final EntityPlayer player) {
        int randomX = 0;
        int randomZ = 0;

        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos((int) player.posX, (int) player.posY, (int) player.posZ);

        boolean isSafe = false;

        while (!isSafe) {
            randomX = (int) ((player.posX + random.nextInt(RCWConfig.common.randomteleportationdistance * 2)) - RCWConfig.common.randomteleportationdistance);
            randomZ = (int) ((player.posZ + random.nextInt(RCWConfig.common.randomteleportationdistance * 2)) - RCWConfig.common.randomteleportationdistance);

            mutablePos.setPos(randomX, getHighestSolidBlock(world, new BlockPos.MutableBlockPos(randomX, 0, randomZ), false), randomZ);

            isSafe = RCW.verifyTeleportCoordinates(world, mutablePos.add(0, 1, 0));
        }

        teleportPlayer(world, player, randomX + 0.5, mutablePos.getY(), randomZ + 0.5, 80);
    }

    /**
     * Teleports the given player to the specified X, Y, and Z coordinates in the given world and spawns explosion particle effects at both the old and new locations.
     *
     * @param world The world in which to teleport the player
     * @param player The player to teleport
     * @param x The X coordinate of the new location
     * @param y The Y coordinate of the new location
     * @param z The Z coordinate of the new location
     * @param particleAmount The number of explosion particles to spawn
     */
    public static void teleportPlayer(final World world, final EntityPlayer player, final double x, final double y, final double z, final int particleAmount) {
        RCW.spawnExplosionParticleAtEntity(player, particleAmount);

        player.setPositionAndUpdate(x + 0.5, y, z + 0.5);

        while (!world.getCollisionBoxes(player, player.getEntityBoundingBox()).isEmpty())
            player.setPositionAndUpdate(player.posX, player.posY + 1, player.posZ);

        world.playSound(null, player.getPosition(), SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.MASTER, 1, 1);

        RCW.spawnExplosionParticleAtEntity(player, particleAmount);
    }
}
