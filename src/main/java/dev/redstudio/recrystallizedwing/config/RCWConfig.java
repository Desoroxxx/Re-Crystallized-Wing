package dev.redstudio.recrystallizedwing.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static dev.redstudio.recrystallizedwing.utils.ModReference.ID;
import static dev.redstudio.recrystallizedwing.utils.ModReference.NAME;

@Config(modid = ID, name = NAME)
@Mod.EventBusSubscriber(modid = ID)
public class RCWConfig {

    public static final Common common = new Common();

    public static class Common {

        public final Durability durability = new Durability();
        public final Cooldown cooldown = new Cooldown();

        public int enderScepterReach = 60;
        public int enderScepterCreativeReachMult = 4;
        public int randomTeleportationDistance = 1000;

        public boolean showInActionBar = true;
        public boolean nostalgicSounds = false;

        public static class Durability {

            @Config.RequiresMcRestart
            public int crystalWingDurability = 32;

            @Config.RequiresMcRestart
            public int BurntWingDurability = 16;

            @Config.RequiresMcRestart
            public int enderScepterDurability = 128;
        }

        public static class Cooldown {

            public int crystalWingCooldown = 256;
            public int burntWingCooldown = 512;
            public int enderScepterCooldown = 20;
        }
    }

    @SubscribeEvent
    public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent onConfigChangedEvent) {
        if (onConfigChangedEvent.getModID().equals(ID))
            ConfigManager.sync(ID, Config.Type.INSTANCE);
    }
}
