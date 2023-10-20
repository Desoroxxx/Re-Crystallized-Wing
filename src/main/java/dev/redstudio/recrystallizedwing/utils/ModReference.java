package dev.redstudio.recrystallizedwing.utils;

import Re_Crystallized_Wing.BuildConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class defines constants for Re-Crystallized Wing.
 */
public class ModReference {

    public static final String ID = BuildConfig.ID;
    public static final String NAME = "Re-Crystallized Wing";
    public static final String VERSION = BuildConfig.VERSION;
    public static final Logger LOGGER = LogManager.getLogger(NAME);
}
