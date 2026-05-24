package com.jason;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

import java.util.function.Function;

public class ItemTest {
    public static <T extends Item> T register(String name, Function<Item.Properties, T> itemFactory, Item.Properties settings) {
        // Create the item key.
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Elemental.MOD_ID, name));

        // Create the item instance.
        T item = itemFactory.apply(settings.setId(itemKey));

        // Register the item.
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);

        return item;
    }
    public static final Item HYDRO_ATK = register("hydro_atk", Item::new, new Item.Properties());
    public static final Item PYRO_ATK = register("pyro_atk", Item::new, new Item.Properties());
    public static final Item ELECTRO_ATK = register("electro_atk", Item::new, new Item.Properties());
    public static final Item CRYO_ATK = register("cryo_atk", Item::new, new Item.Properties());
    public static final Item ANEMO_ATK = register("anemo_atk", Item::new, new Item.Properties());
    public static final Item DENDRO_ATK = register("dendro_atk", Item::new, new Item.Properties());

    public static void initialize() {
    }

}
