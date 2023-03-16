package io.redstudioragnarok.recrystallizedwing.config;

import io.redstudioragnarok.recrystallizedwing.utils.ModReference;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = ModReference.id, name = ModReference.name)
public class RCWConfig {

    public static final Client client = new Client();
    public static final Common common = new Common();

    public static class Common {

        public int crystalwingdurability = 8;
        public int burntwingdurability = 4;
        public int enderscepterdurability = 0;
        public int enderscepterreachmult = 25;
        public int endersceptercreativereachmult = 4;
        public int crystalwingcooldown = 80;
        public int burntwingcooldown = 80;
        public int endersceptercooldown = 20;
        public int randomteleportationdistance = 1000;
    }

    public static class Client {

        public boolean showinactionbar = true;
    }

    @Mod.EventBusSubscriber(modid = ModReference.id)
    private static class EventHandler {
        @SubscribeEvent
        public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent onConfigChangedEvent) {
            if (onConfigChangedEvent.getModID().equals(ModReference.id)) {
                ConfigManager.sync(ModReference.id, Config.Type.INSTANCE);
            }
        }
    }
}
