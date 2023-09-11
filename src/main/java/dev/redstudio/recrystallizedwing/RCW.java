package dev.redstudio.recrystallizedwing;

import dev.redstudio.recrystallizedwing.items.BurningWing;
import dev.redstudio.recrystallizedwing.items.BurntWing;
import dev.redstudio.recrystallizedwing.items.CrystalWing;
import dev.redstudio.recrystallizedwing.items.EnderScepter;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;

import static dev.redstudio.recrystallizedwing.utils.ModReference.ID;
import static dev.redstudio.recrystallizedwing.utils.ModReference.NAME;
import static dev.redstudio.recrystallizedwing.utils.ModReference.VERSION;

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
@Mod.EventBusSubscriber
@Mod(modid = ID, name = NAME, version = VERSION, updateJSON = "https://raw.githubusercontent.com/Red-Studio-Ragnarok/ReCrystallized-Wing/main/update.json")
public final class RCW {

    private static final Map<String, ResourceLocation> LOOT_TABLE_MAP = new HashMap<>();

    private static final ResourceLocation OVERWORLD_LOOT_TABLE = new ResourceLocation(ID, "overworld_loot");
    private static final ResourceLocation NETHER_LOOT_TABLE = new ResourceLocation(ID, "nether_loot");
    private static final ResourceLocation END_LOOT_TABLE = new ResourceLocation(ID, "end_loot");

    public static Item crystalWing, burningWing, burntWing, enderScepter;

    static {
        LOOT_TABLE_MAP.put("minecraft:chests/desert_pyramid", OVERWORLD_LOOT_TABLE);
        LOOT_TABLE_MAP.put("minecraft:chests/spawn_bonus_chest", OVERWORLD_LOOT_TABLE);
        LOOT_TABLE_MAP.put("minecraft:chests/simple_dungeon", OVERWORLD_LOOT_TABLE);
        LOOT_TABLE_MAP.put("minecraft:chests/stronghold_library", OVERWORLD_LOOT_TABLE);
        LOOT_TABLE_MAP.put("minecraft:chests/nether_bridge", NETHER_LOOT_TABLE);
        LOOT_TABLE_MAP.put("minecraft:chests/end_city_treasure", END_LOOT_TABLE);
    }

    @Mod.EventHandler
    public static void init(final FMLInitializationEvent initializationEvent) {
        LootTableList.register(OVERWORLD_LOOT_TABLE);
        LootTableList.register(NETHER_LOOT_TABLE);
        LootTableList.register(END_LOOT_TABLE);
    }

    @SubscribeEvent
    public static void lootTableLoad(final LootTableLoadEvent lootTableLoadEvent) {
        final ResourceLocation lootTableResourceLocation = LOOT_TABLE_MAP.get(lootTableLoadEvent.getName().toString());

        if (lootTableResourceLocation == null)
            return;

        final String lootTableName = lootTableResourceLocation.getPath() + "_loot";

        final LootEntry lootEntryTable = new LootEntryTable(lootTableResourceLocation, 1, 1, new LootCondition[0], lootTableName);
        final LootPool lootPool = new LootPool(new LootEntry[]{lootEntryTable}, new LootCondition[0], new RandomValueRange(1, 2), new RandomValueRange(1, 2), lootTableName);

        lootTableLoadEvent.getTable().addPool(lootPool);
    }

    @SubscribeEvent
    public static void registerItems(final RegistryEvent.Register<Item> itemRegistryEvent) {
        crystalWing = new CrystalWing().setTranslationKey("crystal_wing").setRegistryName(ID, "crystal_wing");
        burningWing = new BurningWing().setTranslationKey("burning_wing").setRegistryName(ID, "burning_wing");
        burntWing = new BurntWing().setTranslationKey("burnt_wing").setRegistryName(ID, "burnt_wing");
        enderScepter = new EnderScepter().setTranslationKey("ender_scepter").setRegistryName(ID, "ender_scepter");

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
}
