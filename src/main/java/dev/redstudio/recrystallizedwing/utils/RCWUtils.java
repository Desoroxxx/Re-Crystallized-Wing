package dev.redstudio.recrystallizedwing.utils;

import dev.redstudio.recrystallizedwing.config.RCWServerConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public final class RCWUtils {

    public static final Random random = new Random();

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

        ((ServerLevel) entity.getLevel()).sendParticles(ParticleTypes.EXPLOSION, entity.getX(), entity.getY(), entity.getZ(), amount, xOffset, yOffset, zOffset, velocity);
    }

    /**
     * Verifies if the given block position is a safe teleport location for players.
     *
     * @param level The level in which to verify the respawn location
     * @param blockPos The block position to verify
     *
     * @return True if the respawn location is safe, false otherwise
     */
    public static boolean verifyTeleportCoordinates(final Level level, final BlockPos blockPos) {
        final Material floorBlockMaterial = level.getBlockState(blockPos.below()).getMaterial();
        final Material bottomBlockMaterial = level.getBlockState(blockPos).getMaterial();
        final Material topBlockMaterial = level.getBlockState(blockPos.above()).getMaterial();

        final boolean floorSafe = floorBlockMaterial.isSolid() || floorBlockMaterial.isLiquid();
        final boolean bottomSafe = !bottomBlockMaterial.isSolid() && !bottomBlockMaterial.isLiquid();
        final boolean topSafe = !topBlockMaterial.isSolid() && !topBlockMaterial.isLiquid();

        return floorSafe && bottomSafe && topSafe;
    }

    /**
     * Finds the highest solid block at the given position in the level.
     *
     * @param level The level in which to search for the highest solid block
     * @param mutablePos A mutable block position of the position for the search
     * @param skipNonNormalCube A boolean flag indicating whether to skip non-normal cubes during the search
     *
     * @return The Y coordinate of the highest solid block, or 0 if no solid block is found
     */
    public static int getHighestSolidBlock(final Level level, final BlockPos.MutableBlockPos mutablePos, final boolean skipNonNormalCube) {
        mutablePos.setY(level.getHeight());

        if (!skipNonNormalCube)
            while ((mutablePos.getY() > 0) && level.getBlockState(mutablePos).isAir())
                mutablePos.move(Direction.DOWN);
        else
            while ((mutablePos.getY() > 0) && (level.getBlockState(mutablePos).isAir() && !level.getBlockState(mutablePos).isCollisionShapeFullBlock(level, mutablePos)))
                mutablePos.move(Direction.DOWN);

        return mutablePos.getY();
    }

    /**
     * Teleports the given player to a new, randomly selected location within a specified distance from their current location the distance is specified via the config.
     *
     * @param level The level in which to teleport the player
     * @param player The player to teleport
     */
    public static void randomTeleport(final Level level, final Player player) {
        int randomX = 0;
        int randomZ = 0;

        final BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos((int) player.getX(), (int) player.getY(), (int) player.getZ());

        while (!verifyTeleportCoordinates(level, mutablePos.offset(0, 1, 0))) {
            randomX = (int) ((player.getX() + random.nextInt(RCWServerConfig.randomTeleportationDistance * 2)) - RCWServerConfig.randomTeleportationDistance);
            randomZ = (int) ((player.getZ() + random.nextInt(RCWServerConfig.randomTeleportationDistance * 2)) - RCWServerConfig.randomTeleportationDistance);

            mutablePos.set(randomX, 0, randomZ);
            mutablePos.setY(getHighestSolidBlock(level, mutablePos, false));
        }

        teleportPlayer(level, player, randomX + 0.5, mutablePos.getY(), randomZ + 0.5, 80);
    }

    /**
     * Teleports the given player to the specified coordinates in the given level and spawns explosion particle effects at both the old and new locations.
     *
     * @param level The level in which to teleport the player
     * @param player The player to teleport
     * @param x The X coordinate of the new location
     * @param y The Y coordinate of the new location
     * @param z The Z coordinate of the new location
     * @param particleAmount The number of explosion particles to spawn
     */
    public static void teleportPlayer(final Level level, final Player player, final double x, final double y, final double z, final int particleAmount) {
        spawnExplosionParticleAtEntity(player, particleAmount);

        player.teleportTo(x + 0.5, y, z + 0.5);

        while (level.getCollisions(player, player.getBoundingBox()).iterator().hasNext())
            player.teleportTo(player.getX(), player.getY() + 1, player.getZ());

        if (!RCWServerConfig.nostalgicSounds)
            level.playSound(null, player.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.MASTER, 1, 1);

        spawnExplosionParticleAtEntity(player, particleAmount);
    }

    /**
     * Teleports the given player to the specified coordinates in the given level and spawns explosion particle effects at both the old and new locations.
     *
     * @param level The level in which to teleport the player
     * @param player The player to teleport
     * @param blockPos The coordinates of the new location
     * @param particleAmount The number of explosion particles to spawn
     */
    public static void teleportPlayer(final Level level, final Player player, final BlockPos blockPos, final int particleAmount) {
        teleportPlayer(level, player, blockPos.getX(), blockPos.getY(), blockPos.getZ(), particleAmount);
    }

    /**
     * Performs a ray trace with extended reach in the level using the player's position, rotation and by calculating the reach with the config options.
     *
     * @param level The level in which to perform the ray trace
     * @param player The player whose position and rotation to use for the ray trace
     *
     * @return A RayTraceResult object containing information about the block that was hit (if any)
     */
    public static HitResult rayTraceWithExtendedReach(final Level level, final Player player) {
        final Vec3 startPosition = new Vec3(player.getX(), player.getY() + player.getEyeHeight(), player.getZ());

        final float yaw = player.getYRot();
        final float cosYaw = Mth.cos(-yaw * 0.017453292F - (float) Math.PI);
        final float sinYaw = Mth.sin(-yaw * 0.017453292F - (float) Math.PI);

        final float pitch = player.getXRot();
        final float cosPitch = -Mth.cos(-pitch * 0.017453292F);
        final float sinPitch = Mth.sin(-pitch * 0.017453292F);

        final float reachMultiplier = RCWServerConfig.enderScepterReach * (player.isCreative() ? RCWServerConfig.enderScepterCreativeReachMult : 1);

        final Vec3 endPosition = startPosition.add((sinYaw * cosPitch) * reachMultiplier, sinPitch * reachMultiplier, (cosYaw * cosPitch) * reachMultiplier);

        return level.clip(new ClipContext(startPosition, endPosition, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
    }

    /**
     * Plays a short "pling" sound effect in the level with a specific pitch at the player location.
     *
     * @param pitch An integer value representing the pitch of the "pling" sound to be played, measured in semitones relative to the standard A440 tuning
     * @param level The level in which to play the "pling" sound effect
     * @param player The player whose position to use for the sound
     */
    public static void playPlingAtPitch(final Level level, final Player player, final float pitch) {
        level.playSound(null, player.blockPosition(), SoundEvents.NOTE_BLOCK_PLING, SoundSource.MASTER, 0.5F, pitch);
    }
}
