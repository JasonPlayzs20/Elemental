package com.jason.mixin;

import com.jason.elements.AffectedElement;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class EntityTickingMixin {

    @Inject(at = @At("TAIL"), method = "tick")
    private void tick(CallbackInfo ci) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;
        if (livingEntity.isAlive() && !livingEntity.level().isClientSide()) {
            var elements = AffectedElement.getAffectedElements(livingEntity);
            elements.tick();

            ServerLevel serverLevel = (ServerLevel) livingEntity.level();

            if (elements.isAffectedWithPyro()) {
                spawnElementParticles(serverLevel, livingEntity, ParticleTypes.FLAME, 2, 0.01);
            }
            if (elements.isAffectedWithCryo()) {
                spawnElementParticles(serverLevel, livingEntity, ParticleTypes.SNOWFLAKE, 2, 0.02);
            }
            if (elements.isAffectedWithHydro()) {
                spawnElementParticles(serverLevel, livingEntity, ParticleTypes.RAIN, 3, 0.05);
            }
            if (elements.isAffectedWithElectro()) {
                spawnElementParticles(serverLevel, livingEntity, ParticleTypes.ELECTRIC_SPARK, 2, 0.03);
            }
        }
    }

    private static void spawnElementParticles(ServerLevel level, LivingEntity entity,
                                               ParticleOptions particle, int count, double speed) {
        AABB box = entity.getBoundingBox();
        double x = entity.getX();
        double y = box.minY + (box.maxY - box.minY) * 0.5;
        double z = entity.getZ();

        double xSpread = (box.maxX - box.minX) * 0.5;
        double ySpread = (box.maxY - box.minY) * 0.5;
        double zSpread = (box.maxZ - box.minZ) * 0.5;

        level.sendParticles(
                particle,
                x, y, z,
                count,
                xSpread,
                ySpread,
                zSpread,
                speed
        );
    }
}
