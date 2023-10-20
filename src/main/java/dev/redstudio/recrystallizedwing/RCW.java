package dev.redstudio.recrystallizedwing;

import dev.redstudio.recrystallizedwing.config.RCWClientConfig;
import dev.redstudio.recrystallizedwing.config.RCWServerConfig;
import dev.redstudio.recrystallizedwing.items.BurningWing;
import dev.redstudio.recrystallizedwing.items.BurntWing;
import dev.redstudio.recrystallizedwing.items.CrystalWing;
import dev.redstudio.recrystallizedwing.items.EnderScepter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forgespi.locating.IModFile;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.event.level.BlockEvent.FluidPlaceBlockEvent;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.forgespi.language.IModFileInfo;
import net.minecraftforge.forgespi.locating.IModFile;
import dev.redstudio.recrystallizedwing.utils.ModFilePackResources;


import java.util.HashMap;
import java.util.Map;

import static dev.redstudio.recrystallizedwing.utils.ModReference.*;

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
@Mod(ID)
@Mod.EventBusSubscriber
public final class RCW {

    private static final Map<ResourceLocation, ResourceLocation> LOOT_TABLE_MAP = new HashMap<>();
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ID);

    public static final RegistryObject<Item> CRYSTAL_WING_ITEM = ITEMS.register("crystal_wing", () -> new CrystalWing(new CrystalWing.Properties()));
    public static final RegistryObject<Item> BURNING_WING = ITEMS.register("burning_wing", () -> new BurningWing(new BurningWing.Properties()));
    public static final RegistryObject<Item> BURNT_WING = ITEMS.register("burnt_wing", () -> new BurntWing(new BurntWing.Properties()));
    public static final RegistryObject<Item> ENDER_SCEPTER = ITEMS.register("ender_scepter", () -> new EnderScepter(new EnderScepter.Properties()));

    public RCW() {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, RCWClientConfig.SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, RCWServerConfig.SPEC);
    }

    static {
        LOOT_TABLE_MAP.put(new ResourceLocation("minecraft", "chests/desert_pyramid"), new ResourceLocation(ID, "overworld_loot"));
        LOOT_TABLE_MAP.put(new ResourceLocation("minecraft", "chests/spawn_bonus_chest"), new ResourceLocation(ID, "overworld_loot"));
        LOOT_TABLE_MAP.put(new ResourceLocation("minecraft", "chests/simple_dungeon"), new ResourceLocation(ID, "overworld_loot"));
        LOOT_TABLE_MAP.put(new ResourceLocation("minecraft", "chests/stronghold_library"), new ResourceLocation(ID, "overworld_loot"));
        LOOT_TABLE_MAP.put(new ResourceLocation("minecraft", "chests/nether_bridge"), new ResourceLocation(ID, "nether_loot"));
        LOOT_TABLE_MAP.put(new ResourceLocation("minecraft", "chests/end_city_treasure"), new ResourceLocation(ID, "end_loot"));
    }

    @SubscribeEvent
    public static void lootTableLoad(final LootTableLoadEvent event) {
        final ResourceLocation lootTableResourceLocation = LOOT_TABLE_MAP.get(event.getName());

        if (lootTableResourceLocation == null)
            return;

        final LootPoolEntryContainer.Builder<?> entryBuilder = LootTableReference.lootTableReference(lootTableResourceLocation).setQuality(1);

        final LootPool.Builder poolBuilder = new LootPool.Builder()
                .name(lootTableResourceLocation.getPath() + "_loot")
                .setRolls(UniformGenerator.between(1, 2))
                .setBonusRolls(UniformGenerator.between(1, 2))
                .add(entryBuilder);

        event.getTable().addPool(poolBuilder.build());
    }

    @EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
    public static final class ModBusEvents {

        /**
         * Taken from <a href="https://github.com/Creators-of-Create/Create/blob/mc1.19/dev/src/main/java/com/simibubi/create/foundation/events/CommonEvents.java#L198">Create</a>
         */
        @SubscribeEvent
        public static void addPackFinders(final AddPackFindersEvent addPackFindersEvent) {
            if (addPackFindersEvent.getPackType() != PackType.CLIENT_RESOURCES)
                return;

            final IModFileInfo modFileInfo = ModList.get().getModFileById(ID);

            if (modFileInfo == null) {
                LOGGER.error("Could not find Create mod file info; built-in resource packs will be missing!");
                return;
            }

            final IModFile modFile = modFileInfo.getFile();

            addResourcePack(addPackFindersEvent, modFile, "rcw_nostalgic_models", "RCW Nostalgic Models");
            addResourcePack(addPackFindersEvent, modFile, "rcw_programmer_art", "RCW Wing Programmer Art");
        }

        private static void addResourcePack(final AddPackFindersEvent addPackFindersEvent, final IModFile modFile, final String resourcePackPath, final String resourcePackName) {
            addPackFindersEvent.addRepositorySource((consumer, constructor) -> consumer.accept(Pack.create(new ResourceLocation(ID, resourcePackPath).toString(), false, () -> new ModFilePackResources(resourcePackName, modFile, "resourcepacks/" + resourcePackPath), constructor, Pack.Position.TOP, PackSource.DEFAULT)));
        }
    }
}
