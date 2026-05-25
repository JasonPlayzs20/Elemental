package com.jason.reactions;

import com.jason.Effects;
import com.jason.elements.*;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.jason.elements.Elements.*;

public class Reaction {

    private static final int FREEZE_COOLDOWN_TICKS = 20;
    private static final Map<ElementPair, ReactionFunction> reactionMap = new HashMap<>();
    private static final Map<UUID, Integer> freezeCooldowns = new HashMap<>();
    private static double burningDmg = 0;

    public static void initReaction() {
        reactionMap.put(new ElementPair(PYRO, HYDRO), (entity, damage, lastElement) -> vaporize(entity, damage, lastElement));
        reactionMap.put(new ElementPair(HYDRO, PYRO), (entity, damage, lastElement) -> vaporize(entity, damage, lastElement));

        reactionMap.put(new ElementPair(CRYO, PYRO), (entity, damage, lastElement) -> melt(entity, damage, lastElement));
        reactionMap.put(new ElementPair(PYRO, CRYO), (entity, damage, lastElement) -> melt(entity, damage, lastElement));

        reactionMap.put(new ElementPair(CRYO, HYDRO), (entity, damage, lastElement) -> frozen(entity, damage));
        reactionMap.put(new ElementPair(HYDRO, CRYO), (entity, damage, lastElement) -> frozen(entity, damage));

        reactionMap.put(new ElementPair(PYRO, ELECTRO), (entity, damage, lastElement) -> overload(entity, damage));
        reactionMap.put(new ElementPair(ELECTRO, PYRO), (entity, damage, lastElement) -> overload(entity, damage));

        reactionMap.put(new ElementPair(PYRO, DENDRO), (entity, damage, lastElement) -> burning(entity, damage));
        reactionMap.put(new ElementPair(DENDRO, PYRO), (entity, damage, lastElement) -> burning(entity, damage));
    }

    public static double calculateReactionAndAction(LivingEntity entity, double damage, Elements lastElement) {
        if (entity.isAlive() && !entity.level().isClientSide() && !(entity instanceof ArmorStand)) {
            ArrayList<Elements> elements = AffectedElement.getAffectedElements(entity).getAffectedElements();

            for (int i = 0; i < elements.size(); i++) {
                for (int j = 0; j < elements.size(); j++) {
                    if (i != j) {
                        ElementPair pair = new ElementPair(elements.get(i), elements.get(j));
                        ReactionFunction runnable = reactionMap.get(pair);
                        if (runnable != null) {
                            return runnable.apply(entity, damage, lastElement);

                        }
                    }
                }
            }
        }
        return damage;
    }

    public static double burning(LivingEntity entity, double damage) {
        if (!(entity.level() instanceof ServerLevel level) && !(entity instanceof ArmorStand)) {
            return damage;
        }
        AffectedElement.getAffectedElements(entity).removeDendro();
        AffectedElement.getAffectedElements(entity).removePyro();
        AffectedReaction.get(entity).affectBurning(11 * 20, damage);
        burningDmg = Dendro.getElementalMastery() * damage;
        return Dendro.getElementalMastery() * damage;
    }

    public static double frozen(LivingEntity entity, double damage) {
        if (!(entity.level() instanceof ServerLevel level) && !(entity instanceof ArmorStand)) {
            return damage;
        }
        AffectedElement.getAffectedElements(entity).removeHydro();
        AffectedReaction.get(entity).affectFrozen(11 * 20);

//        int currentTick = level.getServer().getTickCount();
//        Integer lastFreezeTick = freezeCooldowns.get(entity.getUUID());
//        if (lastFreezeTick != null && currentTick - lastFreezeTick < FREEZE_COOLDOWN_TICKS) {
//            return damage;
//        }
//        freezeCooldowns.put(entity.getUUID(), currentTick);
//
//        playSound(entity, SoundEvents.PLAYER_HURT_FREEZE, 0.8F, 0.8F);
//        playSound(entity, SoundEvents.GLASS_PLACE, 0.35F, 1.6F);
//        spawnParticles(entity, ParticleTypes.SNOWFLAKE, 30, 0.4, 0.55, 0.4, 0.03);
//        spawnParticles(entity, ParticleTypes.ITEM_SNOWBALL, 12, 0.32, 0.35, 0.32, 0.02);
//        spawnParticles(entity, ParticleTypes.POOF, 8, 0.28, 0.28, 0.28, 0.01);
//
////        AffectedElement.getAffectedElements(entity).removeCryo();
////        AffectedElement.getAffectedElements(entity).removeHydro();
//        if (entity.level() instanceof ServerLevel serverLevel) {
//            Effects.textEffect("Frozen",serverLevel, ChatFormatting.AQUA, entity.getX(), entity.getY(), entity.getZ());
////            DamageSource freezing = new DamageSource(
////                    serverLevel.registryAccess()
////                            .lookupOrThrow(Registries.DAMAGE_TYPE)
////                            .get(DamageTypes.FREEZE.identifier()).orElseThrow()
////            );
////            entity.hurtServer(serverLevel, freezing, 2);
//        }
        return 0;
    }

    public static double vaporize(LivingEntity entity, double damage, Elements lastElement) {
        playSound(entity, SoundEvents.LAVA_EXTINGUISH, 0.7F, 1.35F);
        spawnParticles(entity, ParticleTypes.SMOKE, 8, 0.28, 0.35, 0.28, 0.02);
        spawnParticles(entity, ParticleTypes.CLOUD, 5, 0.24, 0.28, 0.24, 0.01);

        AffectedElement.getAffectedElements(entity).removePyro();
        AffectedElement.getAffectedElements(entity).removeHydro();
        if (entity.level() instanceof ServerLevel level) {
            Effects.textEffect("Vaporize", level, ChatFormatting.RED, entity.getX(), entity.getY(), entity.getZ());
        }
        if (lastElement == PYRO) {
            return Pyro.pyroVaporize() * damage;
        }
        if (lastElement == HYDRO) {
            return Hydro.hydroVaporize() * damage;
        }
        return damage;
    }

    public static double melt(LivingEntity entity, double damage, Elements lastElement) {
        playSound(entity, SoundEvents.LAVA_POP, 0.75F, 0.85F);
        playSound(entity, SoundEvents.FIRE_EXTINGUISH, 0.35F, 1.45F);
        spawnParticles(entity, ParticleTypes.LAVA, 8, 0.22, 0.28, 0.22, 0.02);
        spawnParticles(entity, ParticleTypes.FLAME, 12, 0.32, 0.35, 0.32, 0.03);
        spawnParticles(entity, ParticleTypes.SMOKE, 6, 0.24, 0.25, 0.24, 0.01);

        AffectedElement.getAffectedElements(entity).removeCryo();
        AffectedElement.getAffectedElements(entity).removePyro();
        if (entity.level() instanceof ServerLevel level) {
            Effects.textEffect("Melt", level, ChatFormatting.GOLD, entity.getX(), entity.getY(), entity.getZ());
        }
        if (lastElement == PYRO) {
            return Pyro.pyroMelt() * damage;
        }
        if (lastElement == CRYO) {
            return Cryo.cryoMelt() * damage;
        }
        return damage;
    }


    public static double overload(LivingEntity entity, double damage) {
        if (entity.level() instanceof ServerLevel level) {
            double x = entity.getX();
            double y = getEffectY(entity);
            double z = entity.getZ();

            level.playSound(null, x, y, z, SoundEvents.GENERIC_EXPLODE, SoundSource.PLAYERS, 1.15F, 1.05F);
            level.sendParticles(ParticleTypes.EXPLOSION_EMITTER, x, y, z, 1, 0.0, 0.0, 0.0, 0.0);
        }
        spawnParticles(entity, ParticleTypes.EXPLOSION, 4, 0.35, 0.3, 0.35, 0.02);
        spawnParticles(entity, ParticleTypes.LARGE_SMOKE, 18, 0.65, 0.45, 0.65, 0.06);
        spawnParticles(entity, ParticleTypes.FLAME, 20, 0.7, 0.45, 0.7, 0.08);
        spawnParticles(entity, ParticleTypes.ELECTRIC_SPARK, 26, 0.75, 0.5, 0.75, 0.12);

        AffectedElement.getAffectedElements(entity).removeElectro();
        AffectedElement.getAffectedElements(entity).removePyro();

        if (entity.level() instanceof ServerLevel level) {
            Effects.textEffect("Overload", level, ChatFormatting.LIGHT_PURPLE, entity.getX(), entity.getY(), entity.getZ());
        }
        return damage + 2;
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

    public static double getBurningDmg() {
        return burningDmg;
    }

    public static void setBurningDmg(double burningDmg) {
        Reaction.burningDmg = burningDmg;
    }

    private record ElementPair(Elements a, Elements b) {
    }

    @FunctionalInterface
    interface ReactionFunction {
        double apply(LivingEntity entity, double damage, Elements lastElement);
    }
}
