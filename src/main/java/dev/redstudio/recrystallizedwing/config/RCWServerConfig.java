package dev.redstudio.recrystallizedwing.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.config.ModConfigEvent;

/**
 * @author Luna Lage (Desoroxxx)
 * @since
 */
public class RCWServerConfig {

    public static final ForgeConfigSpec SPEC;

    private static final ForgeConfigSpec.Builder BUILDER;

    // General
    private static final ForgeConfigSpec.BooleanValue NOSTALGIC_SOUNDS;
    private static final ForgeConfigSpec.IntValue ENDER_SCEPTER_REACH;
    private static final ForgeConfigSpec.IntValue ENDER_SCEPTER_CREATIVE_REACH_MULT;
    private static final ForgeConfigSpec.IntValue RANDOM_TELEPORTATION_DISTANCE;

    // Durability
    private static final ForgeConfigSpec.IntValue CRYSTAL_WING_DURABILITY;
    private static final ForgeConfigSpec.IntValue BURNT_WING_DURABILITY;
    private static final ForgeConfigSpec.IntValue ENDER_SCEPTER_DURABILITY;

    // Cooldown
    private static final ForgeConfigSpec.IntValue CRYSTAL_WING_COOLDOWN;
    private static final ForgeConfigSpec.IntValue BURNT_WING_COOLDOWN;
    private static final ForgeConfigSpec.IntValue ENDER_SCEPTER_COOLDOWN;

    // General
    public static boolean nostalgicSounds;
    public static int enderScepterReach = 60;
    public static int enderScepterCreativeReachMult = 4;
    public static int randomTeleportationDistance = 1000;

    // Durability
    public static int crystalWingDurability = 32;
    public static int burntWingDurability = 16;
    public static int enderScepterDurability = 128;

    // Cooldown
    public static int crystalWingCooldown = 256;
    public static int burntWingCooldown = 512;
    public static int enderScepterCooldown = 20;

    static {
        BUILDER = new ForgeConfigSpec.Builder();

        NOSTALGIC_SOUNDS = BUILDER
                .comment("If true uses the sounds from the original Crystal Wing 1.2.5")
                .define("nostalgicSounds", false);

        ENDER_SCEPTER_REACH = BUILDER
                .comment("Ender Scepter reach, in blocks")
                .defineInRange("enderScepterReach", 60, 0, Integer.MAX_VALUE);

        ENDER_SCEPTER_CREATIVE_REACH_MULT = BUILDER
                .comment("Ender Scepter creative reach multiplier")
                .defineInRange("enderScepterCreativeReachMult", 4, 0, Integer.MAX_VALUE);

        RANDOM_TELEPORTATION_DISTANCE = BUILDER
                .comment("Random teleportation distance. determines how far should the player be teleported when random teleportation is used (Using a Crystal Wing in the end or using a Burnt Wing), in blocks")
                .defineInRange("randomTeleportationDistance", 1000, 0, Integer.MAX_VALUE);

        BUILDER.push("Durability").comment("Configuration for the durability of items");

        CRYSTAL_WING_DURABILITY = BUILDER
                .comment("Max durability of the Crystal Wing, 0 means infinite durability.")
                .defineInRange("crystalWingDurability", 32, 0, Integer.MAX_VALUE);

        BURNT_WING_DURABILITY = BUILDER
                .comment("Max durability of the Burnt Wing, 0 means infinite durability.")
                .defineInRange("BurntWingDurability", 16, 0, Integer.MAX_VALUE);

        ENDER_SCEPTER_DURABILITY = BUILDER
                .comment("Max durability of the Ender Scepter, 0 means infinite durability.")
                .defineInRange("enderScepterDurability", 128, 0, Integer.MAX_VALUE);

        BUILDER.pop();

        BUILDER.push("Cooldown").comment("Configuration for the cooldown of items in ticks");

        CRYSTAL_WING_COOLDOWN = BUILDER
                .comment("Cooldown between uses of the Crystal Wing in ticks")
               .defineInRange("crystalWingCooldown", 256, 0, Integer.MAX_VALUE);

        BURNT_WING_COOLDOWN = BUILDER
                .comment("Cooldown between uses of the Burnt Wing in ticks")
               .defineInRange("BurntWingCooldown", 512, 0, Integer.MAX_VALUE);

        ENDER_SCEPTER_COOLDOWN = BUILDER
                .comment("Cooldown between uses of the Ender Scepter in ticks")
              .defineInRange("enderScepterCooldown", 20, 0, Integer.MAX_VALUE);

        BUILDER.pop();

        SPEC = BUILDER.build();
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        // General
        nostalgicSounds = NOSTALGIC_SOUNDS.get();
        enderScepterReach = ENDER_SCEPTER_REACH.get();
        enderScepterCreativeReachMult = ENDER_SCEPTER_CREATIVE_REACH_MULT.get();
        randomTeleportationDistance = RANDOM_TELEPORTATION_DISTANCE.get();

        // Durability
        crystalWingDurability = CRYSTAL_WING_DURABILITY.get();
        burntWingDurability = BURNT_WING_DURABILITY.get();
        enderScepterDurability = ENDER_SCEPTER_DURABILITY.get();

        // Cooldown
        crystalWingCooldown = CRYSTAL_WING_COOLDOWN.get();
        burntWingCooldown = BURNT_WING_COOLDOWN.get();
        enderScepterCooldown = ENDER_SCEPTER_COOLDOWN.get();
    }
}
