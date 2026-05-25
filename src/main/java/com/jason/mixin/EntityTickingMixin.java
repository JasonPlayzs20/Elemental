package com.jason.mixin;

import com.jason.Damages;
import com.jason.Effects;
import com.jason.elements.AffectedElement;
import com.jason.elements.Elements;
import com.jason.reactions.AffectedReaction;
import com.jason.reactions.Reaction;
import com.jason.reactions.SecondaryReactions;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
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

//import static com.jason.reactions.Reaction.playSound;
//import static com.jason.reactions.Reaction.spawnParticles;

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
                BlockState block = livingEntity.level().getBlockState(new BlockPos(x, y, z));

                if (block.is(Blocks.WATER) || block.is(Blocks.WATER_CAULDRON)) {
                    AffectedElement.getAffectedElements(livingEntity).affectHydro((LivingEntity) livingEntity);
                    Reaction.calculateReactionAndAction(livingEntity, 0, Elements.HYDRO);
                }
                if (block.is(Blocks.LAVA) || block.is(Blocks.FIRE) || block.is(Blocks.SOUL_FIRE) || block.is(Blocks.CAMPFIRE) || block.is(Blocks.SOUL_CAMPFIRE) || block.is(Blocks.LAVA_CAULDRON)) {
                    AffectedElement.getAffectedElements(livingEntity).affectPyro((LivingEntity) livingEntity);
                    Reaction.calculateReactionAndAction(livingEntity, 0, Elements.PYRO);
                    SecondaryReactions.calculateSecondaryReactions(livingEntity, 0, Elements.PYRO);

                }
                if (block.is(Blocks.POWDER_SNOW) || block.is(Blocks.POWDER_SNOW_CAULDRON)) {
                    AffectedElement.getAffectedElements(livingEntity).affectCryo((LivingEntity) livingEntity);
                    Reaction.calculateReactionAndAction(livingEntity, 0, Elements.CRYO);

                }
            }
        }
        if (livingEntity.isAlive() && !livingEntity.level().isClientSide() && !(livingEntity instanceof ArmorStand)) {
            var elements = AffectedElement.getAffectedElements(livingEntity);
            elements.tick();
            var reactions = AffectedReaction.get(livingEntity);
            reactions.tick();

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

            if (reactions.isFrozen()) {
                livingEntity.setTicksFrozen(60);
                if ((double) reactions.getFrozenTick() / 20 == Math.floor((double) reactions.getFrozenTick() / 20)) {
                    playSound(livingEntity, SoundEvents.PLAYER_HURT_FREEZE, 0.8F, 0.8F);
                    playSound(livingEntity, SoundEvents.GLASS_PLACE, 0.35F, 1.6F);
                    spawnParticles(livingEntity, ParticleTypes.SNOWFLAKE, 30, 0.4, 0.55, 0.4, 0.03);
                    spawnParticles(livingEntity, ParticleTypes.ITEM_SNOWBALL, 12, 0.32, 0.35, 0.32, 0.02);
                    spawnParticles(livingEntity, ParticleTypes.POOF, 8, 0.28, 0.28, 0.28, 0.01);

//        AffectedElement.getAffectedElements(entity).removeCryo();
//        AffectedElement.getAffectedElements(entity).removeHydro();
                    if (livingEntity.level() instanceof ServerLevel serverLevele) {
                        Effects.textEffect("Frozen", serverLevel, ChatFormatting.AQUA, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
                    }
                }
            }
            if (reactions.isBurning()) {
                if ((double) reactions.getBurningTick() / 10 == Math.floor((double) reactions.getBurningTick() / 10)) {
//                    playSound(livingEntity, SoundEvents.PLAYER_HURT_FREEZE, 0.8F, 0.8F);
                    playSound(livingEntity, SoundEvents.GLASS_PLACE, 0.35F, 1.6F);
                    spawnParticles(livingEntity, ParticleTypes.SOUL_FIRE_FLAME, 30, 0.4, 0.55, 0.4, 0.03);
                    spawnParticles(livingEntity, ParticleTypes.COPPER_FIRE_FLAME, 12, 0.32, 0.35, 0.32, 0.02);
                    spawnParticles(livingEntity, ParticleTypes.POOF, 8, 0.28, 0.28, 0.28, 0.01);

//        AffectedElement.getAffectedElements(entity).removeCryo();
//        AffectedElement.getAffectedElements(entity).removeHydro();
                    if (livingEntity.level() instanceof ServerLevel serverLevele) {
                        Effects.textEffect("Burning", serverLevel, ChatFormatting.GREEN, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
                        DamageSource dendroSource = new DamageSource(
                                livingEntity.level().registryAccess()
                                        .lookupOrThrow(Registries.DAMAGE_TYPE)
                                        .get(Damages.DENDRO_DAMAGE.identifier()).orElseThrow()
                        );

                        livingEntity.hurtServer((ServerLevel) serverLevele,dendroSource, (float) AffectedReaction.get(livingEntity).getBurningDamage());
                    }
                }
            }
//            if (elements.isAffectedWithCryo() && elements.isAffectedWithHydro()) {
//                Reaction.calculateReactionAndAction(livingEntity,0, Elements.CRYO);
//                //TODO make a reaction or like over time tracker thing
//                livingEntity.setTicksFrozen(60);
//            }
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
    private static void playSound(LivingEntity entity, SoundEvent sound, float volume, float pitch) {
        if (entity.level() instanceof ServerLevel level) {
            level.playSound(null, entity.getX(), getEffectY(entity), entity.getZ(), sound, SoundSource.PLAYERS, volume, pitch);
        }
    }

    private static void spawnParticles(LivingEntity entity, ParticleOptions particle, int count,
                                       double xSpread, double ySpread, double zSpread, double speed) {
        if (entity.level() instanceof ServerLevel level) {
            level.sendParticles(
                    particle,
                    entity.getX(),
                    getEffectY(entity),
                    entity.getZ(),
                    count,
                    xSpread,
                    ySpread,
                    zSpread,
                    speed
            );
        }
    }
    private static double getEffectY(LivingEntity entity) {
        AABB box = entity.getBoundingBox();
        return box.minY + (box.maxY - box.minY) * 0.55;
    }

    private record ElementPair(Elements a, Elements b) {
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
