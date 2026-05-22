package com.jason;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;

public class Damages {
    public static final ResourceKey<DamageType> HYDRO_DAMAGE = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.fromNamespaceAndPath("elemental", "hydro"));
    public static final ResourceKey<DamageType> PYRO_DAMAGE = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.fromNamespaceAndPath("elemental", "pyro"));
    public static final ResourceKey<DamageType> ELECTRO_DAMAGE = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.fromNamespaceAndPath("elemental", "electro"));
    public static final ResourceKey<DamageType> CRYO_DAMAGE = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.fromNamespaceAndPath("elemental", "cryo"));


}
