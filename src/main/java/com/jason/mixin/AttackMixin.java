package com.jason.mixin;

import com.jason.Damages;
import com.jason.ItemTest;
import com.jason.elements.AffectedElement;
import com.jason.elements.Elements;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.jason.reactions.Reaction.calculateReactionAndAction;
import static com.jason.reactions.SecondaryReactions.calculateSecondaryReactions;

@Mixin(Player.class)
public class AttackMixin {



    @Inject(at = @At("HEAD"), method = "attack")
    public void attack(Entity entity, CallbackInfo ci) {
        Player player = (Player) (Object) this;
        if (player.level().isClientSide()) return;

        DamageSource pyroSource = new DamageSource(
                entity.level().registryAccess()
                        .lookupOrThrow(Registries.DAMAGE_TYPE)
                        .get(Damages.PYRO_DAMAGE.identifier()).orElseThrow()
        );
        DamageSource cryoSource = new DamageSource(
                entity.level().registryAccess()
                        .lookupOrThrow(Registries.DAMAGE_TYPE)
                        .get(Damages.CRYO_DAMAGE.identifier()).orElseThrow()
        );
        DamageSource hydroSource = new DamageSource(
                entity.level().registryAccess()
                        .lookupOrThrow(Registries.DAMAGE_TYPE)
                        .get(Damages.HYDRO_DAMAGE.identifier()).orElseThrow()
        );
        DamageSource electroSource = new DamageSource(
                entity.level().registryAccess()
                        .lookupOrThrow(Registries.DAMAGE_TYPE)
                        .get(Damages.ELECTRO_DAMAGE.identifier()).orElseThrow()
        );
        DamageSource anemoSource = new DamageSource(
                entity.level().registryAccess()
                        .lookupOrThrow(Registries.DAMAGE_TYPE)
                        .get(Damages.ANEMO_DAMAGE.identifier()).orElseThrow()
        );
        DamageSource dendroSource = new DamageSource(
                entity.level().registryAccess()
                        .lookupOrThrow(Registries.DAMAGE_TYPE)
                        .get(Damages.DENDRO_DAMAGE.identifier()).orElseThrow()
        );

        ItemStack itemStack = player.getMainHandItem();
        LivingEntity livingEntity = (LivingEntity) entity;
        if (itemStack.is(ItemTest.HYDRO_ATK)) {
            AffectedElement.getAffectedElements(entity).affectHydro((LivingEntity)livingEntity);
            System.out.println("HYDRO");
            System.out.println(AffectedElement.getAffectedElements(entity).isAffectedWithHydro());
            double dmg = calculateReactionAndAction(livingEntity,2, Elements.HYDRO) + calculateSecondaryReactions(livingEntity,2,Elements.HYDRO);
           livingEntity.hurtServer((ServerLevel) player.level(), hydroSource, (float) dmg);
        }
        else if (itemStack.is(ItemTest.PYRO_ATK)) {
            AffectedElement.getAffectedElements(entity).affectPyro((LivingEntity)livingEntity);
            double dmg = calculateReactionAndAction(livingEntity,2, Elements.PYRO) + calculateSecondaryReactions(livingEntity,2,Elements.PYRO);
           livingEntity.hurtServer((ServerLevel) player.level(), pyroSource, (float) dmg);
        }
        else if (itemStack.is(ItemTest.ELECTRO_ATK)) {
            AffectedElement.getAffectedElements(entity).affectElectro((LivingEntity)livingEntity);
            double dmg = calculateReactionAndAction(livingEntity,2, Elements.ELECTRO) + calculateSecondaryReactions(livingEntity,2,Elements.ELECTRO);
           livingEntity.hurtServer((ServerLevel) player.level(), electroSource, (float) dmg);
        }
        else if (itemStack.is(ItemTest.CRYO_ATK)) {
            AffectedElement.getAffectedElements(entity).affectCryo((LivingEntity)livingEntity);
            double dmg = calculateReactionAndAction(livingEntity,2, Elements.CRYO) + calculateSecondaryReactions(livingEntity,2,Elements.CRYO);
           livingEntity.hurtServer((ServerLevel) player.level(), cryoSource, (float) dmg);
        }
        else if (itemStack.is(ItemTest.ANEMO_ATK)) {
            AffectedElement.getAffectedElements(entity).affectAnemo(livingEntity);
            livingEntity.hurtServer((ServerLevel) player.level(), anemoSource, (float) calculateReactionAndAction(livingEntity, 2, Elements.ANEMO));
        }
        else if (itemStack.is(ItemTest.DENDRO_ATK)) {
            AffectedElement.getAffectedElements(entity).affectDendro(livingEntity);
            livingEntity.hurtServer((ServerLevel) player.level(), dendroSource, (float) calculateReactionAndAction(livingEntity, 2, Elements.DENDRO));
        }

    }
}
