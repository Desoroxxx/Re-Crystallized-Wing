package io.redstudioragnarok.recrystallizedwing;

import io.redstudioragnarok.recrystallizedwing.items.BurningWing;
import io.redstudioragnarok.recrystallizedwing.items.BurntWing;
import io.redstudioragnarok.recrystallizedwing.items.CrystalWing;
import io.redstudioragnarok.recrystallizedwing.items.EnderScepter;
import io.redstudioragnarok.recrystallizedwing.utils.ModReference;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
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
public final class RCW {

    public static Item crystalWing, burningWing, burntWing, enderScepter;

    public RCW() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Mod.EventHandler
    public void init(final FMLInitializationEvent initializationEvent) {
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
    public void lootTableLoad(LootTableLoadEvent lootTableLoadEvent) {
        if (lootTableLoadEvent.getName().toString().equals("minecraft:chests/end_city_treasure")) {
            final LootEntry lootEntryTable = new LootEntryTable(new ResourceLocation(ModReference.id, "end_loot"), 1, 1, new LootCondition[0], "end_loot");

            final LootPool lootPool = new LootPool(new LootEntry[] {lootEntryTable}, new LootCondition[0], new RandomValueRange(1, 2), new RandomValueRange(1, 2), "end_loot");

            lootTableLoadEvent.getTable().addPool(lootPool);
        } else if (lootTableLoadEvent.getName().toString().equals("minecraft:chests/nether_bridge")) {
            final LootEntry lootEntryTable = new LootEntryTable(new ResourceLocation(ModReference.id, "nether_loot"), 1, 1, new LootCondition[0], "nether_loot");

            final LootPool lootPool = new LootPool(new LootEntry[] {lootEntryTable}, new LootCondition[0], new RandomValueRange(1, 2), new RandomValueRange(1, 2), "nether_loot");

            lootTableLoadEvent.getTable().addPool(lootPool);
        } else if (lootTableLoadEvent.getName().toString().equals("minecraft:chests/desert_pyramid") || lootTableLoadEvent.getName().toString().equals("minecraft:chests/spawn_bonus_chest") || lootTableLoadEvent.getName().toString().equals("minecraft:chests/simple_dungeon") || lootTableLoadEvent.getName().toString().equals("minecraft:chests/simple_dungeon") || lootTableLoadEvent.getName().toString().equals("minecraft:chests/stronghold_library")) {
            final LootEntry lootEntryTable = new LootEntryTable(new ResourceLocation(ModReference.id, "overworld_loot"), 1, 1, new LootCondition[0], "overworld_loot");

            final LootPool lootPool = new LootPool(new LootEntry[] {lootEntryTable}, new LootCondition[0], new RandomValueRange(1, 2), new RandomValueRange(1, 2), "overworld_loot");

            lootTableLoadEvent.getTable().addPool(lootPool);
        }
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
