package com.jason.mixin;

import com.jason.Damages;
import com.jason.ItemTest;
import net.minecraft.advancements.criterion.PlayerHurtEntityTrigger;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.behavior.StartAttacking;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class AttackMixin {



    @Inject(at = @At("HEAD"), method = "attack")
    public void attack(Entity entity, CallbackInfo ci) {
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

        Player player = (Player) (Object) this;
        ItemStack itemStack = player.getMainHandItem();
        if (itemStack.is(ItemTest.HYDRO_ATK)) {
            entity.hurtServer((ServerLevel) player.level(),hydroSource, 1);
        }
        else if (itemStack.is(ItemTest.PYRO_ATK)) {
            entity.hurtServer((ServerLevel) player.level(),pyroSource, 1);
        }
        else if (itemStack.is(ItemTest.ELECTRO_ATK)) {
            entity.hurtServer((ServerLevel) player.level(),electroSource, 1);
        }
        else if (itemStack.is(ItemTest.CRYO_ATK)) {
            entity.hurtServer((ServerLevel) player.level(),cryoSource, 1);
        }

    }
}
