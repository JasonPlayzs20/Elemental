package com.jason;

import net.fabricmc.api.ModInitializer;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.jason.ElementalItemTab.CUSTOM_CREATIVE_TAB;
import static com.jason.ElementalItemTab.ELEMENTAL_ITEM_TAB;

public class Elemental implements ModInitializer {
	public static final String MOD_ID = "elemental";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!");
		ItemTest.initialize();
		// Register the group.
		Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, ELEMENTAL_ITEM_TAB, CUSTOM_CREATIVE_TAB);
	}
}