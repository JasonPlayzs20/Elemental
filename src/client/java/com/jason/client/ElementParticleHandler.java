package com.jason.client;

import com.jason.elements.AffectedElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import org.joml.Vector3f;

public class ElementParticleHandler {

    // Element colors (ARGB packed int)
    private static final int PYRO_COLOR = 0xFFFF4D00;      // orange-red
    private static final int CRYO_COLOR = 0xFF80CCFF;      // icy blue
    private static final int HYDRO_COLOR = 0xFF0066FF;     // deep blue
    private static final int ELECTRO_COLOR = 0xFFB34DFF;   // purple

    public static void onClientTick(Minecraft client) {
        ClientLevel level = client.level;
        if (level == null || client.isPaused()) return;

        RandomSource random = level.getRandom();
//        System.out.println("Jason's Element Particle Handler:");
        for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, getLoadedBounds(client))) {
            var elements = AffectedElement.getAffectedElements(entity);

            if (elements.isAffectedWithPyro()) {
                spawnElementParticles(level, entity, random, PYRO_COLOR, 1.2f);
            }
            if (elements.isAffectedWithCryo()) {
                spawnElementParticles(level, entity, random, CRYO_COLOR, 1.0f);
            }
            if (elements.isAffectedWithHydro()) {
                spawnElementParticles(level, entity, random, HYDRO_COLOR, 1.0f);
            }
            if (elements.isAffectedWithElectro()) {
                spawnElementParticles(level, entity, random, ELECTRO_COLOR, 1.0f);
            }
        }
    }

    private static void spawnElementParticles(ClientLevel level, LivingEntity entity,
                                               RandomSource random, int color, float scale) {
        // Spawn 1-2 particles per tick around the entity's bounding box
        AABB box = entity.getBoundingBox();
        double x = box.minX + random.nextDouble() * (box.maxX - box.minX);
        double y = box.minY + random.nextDouble() * (box.maxY - box.minY);
        double z = box.minZ + random.nextDouble() * (box.maxZ - box.minZ);

        // Small upward drift
        double vy = 0.02 + random.nextDouble() * 0.03;
        double vx = (random.nextDouble() - 0.5) * 0.02;
        double vz = (random.nextDouble() - 0.5) * 0.02;

        DustParticleOptions particle = new DustParticleOptions(color, scale);
        level.addParticle(particle, x, y, z, vx, vy, vz);
    }

    private static AABB getLoadedBounds(Minecraft client) {
        // Only process entities near the player (64 block radius)
        var player = client.player;
        if (player == null) return new AABB(0, 0, 0, 0, 0, 0);
        return player.getBoundingBox().inflate(64.0);
    }
}
