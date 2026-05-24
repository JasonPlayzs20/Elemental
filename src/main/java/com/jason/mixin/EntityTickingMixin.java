package com.jason.mixin;

import com.jason.Damages;
import com.jason.elements.AffectedElement;
import com.jason.elements.Elements;
import com.jason.elements.Reaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.InterpolationHandler;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class EntityTickingMixin {

    @Shadow
    @Final
    protected InterpolationHandler interpolation;

    @Inject(at = @At("TAIL"), method = "tick")
    private void tick(CallbackInfo ci) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;
        if (livingEntity.isAlive()) {
            int x = livingEntity.getBlockX();
            int y = livingEntity.getBlockY();
            int z = livingEntity.getBlockZ();

            if (!livingEntity.level().isClientSide() && !(livingEntity instanceof ArmorStand)) {
                BlockState block = livingEntity.level().getBlockState(new BlockPos(x,y,z));

                if (block.is(Blocks.WATER) || block.is(Blocks.WATER_CAULDRON)) {
                    AffectedElement.getAffectedElements(livingEntity).affectHydro((LivingEntity) livingEntity);
                    Reaction.calculateReactionAndAction(livingEntity,0, Elements.HYDRO);
                }
                if (block.is(Blocks.LAVA) || block.is(Blocks.FIRE) || block.is(Blocks.SOUL_FIRE) || block.is(Blocks.CAMPFIRE) || block.is(Blocks.SOUL_CAMPFIRE) || block.is(Blocks.LAVA_CAULDRON)) {
                    AffectedElement.getAffectedElements(livingEntity).affectPyro((LivingEntity) livingEntity);
                    Reaction.calculateReactionAndAction(livingEntity,0, Elements.PYRO);

                }
                if (block.is(Blocks.POWDER_SNOW) || block.is(Blocks.POWDER_SNOW_CAULDRON)) {
                    AffectedElement.getAffectedElements(livingEntity).affectCryo((LivingEntity) livingEntity);
                    Reaction.calculateReactionAndAction(livingEntity,0, Elements.CRYO);

                }
            }
        }
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
            if (elements.isAffectedWithDendro()) {
                spawnFallingLeaves(serverLevel, livingEntity);
            }
            if (elements.isAffectedWithCryo() && elements.isAffectedWithHydro()) {
                Reaction.calculateReactionAndAction(livingEntity,0, Elements.CRYO);
                //TODO make a reaction or like over time tracker thing
                livingEntity.setTicksFrozen(60);
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

    private static void spawnFallingLeaves(ServerLevel level, LivingEntity entity) {
        AABB box = entity.getBoundingBox();
        double x = entity.getX();
        double y = box.maxY + 0.15;
        double z = entity.getZ();

        double xSpread = (box.maxX - box.minX) * 0.6;
        double zSpread = (box.maxZ - box.minZ) * 0.6;

        level.sendParticles(
                ColorParticleOption.create(ParticleTypes.TINTED_LEAVES, 0x4AC83D),
                x, y, z,
                2,
                xSpread,
                0.08,
                zSpread,
                0.01
        );
    }
}
