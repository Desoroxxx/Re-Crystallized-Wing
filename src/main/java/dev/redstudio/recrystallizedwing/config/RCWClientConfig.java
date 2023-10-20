package dev.redstudio.recrystallizedwing.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import static dev.redstudio.recrystallizedwing.utils.ModReference.ID;

/**
 * @author Luna Lage (Desoroxxx)
 * @since 1.2
 */
@Mod.EventBusSubscriber(modid = ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RCWClientConfig {

    public static final ForgeConfigSpec SPEC;

    private static final ForgeConfigSpec.Builder BUILDER;

    private static final ForgeConfigSpec.BooleanValue SHOW_IN_ACTION_BAR;

    public static boolean showInActionBar;

    static {
        BUILDER = new ForgeConfigSpec.Builder();

        SHOW_IN_ACTION_BAR = BUILDER
                .comment("If true show the message when you use the Crystal Wing in the action bar instead of the chat")
                .define("showInActionBar", true);

        SPEC = BUILDER.build();
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        showInActionBar = SHOW_IN_ACTION_BAR.get();
    }
}
