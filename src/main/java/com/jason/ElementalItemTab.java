package com.jason;

import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ElementalItemTab {
    public static final ResourceKey<CreativeModeTab> ELEMENTAL_ITEM_TAB = ResourceKey.create(
            BuiltInRegistries.CREATIVE_MODE_TAB.key(), Identifier.fromNamespaceAndPath(Elemental.MOD_ID, "creative_tab")
    );
    public static final CreativeModeTab CUSTOM_CREATIVE_TAB = FabricCreativeModeTab.builder()
            .icon(() -> new ItemStack(Items.APPLE))
            .title(Component.translatable("creativeTab.elemental"))
            .displayItems((params, output) -> {
                output.accept(ItemTest.HYDRO_ATK);
                output.accept(ItemTest.PYRO_ATK);
                output.accept(ItemTest.ELECTRO_ATK);
                output.accept(ItemTest.CRYO_ATK);
                output.accept(ItemTest.ANEMO_ATK);
                output.accept(ItemTest.DENDRO_ATK);

            })
            .build();
}
