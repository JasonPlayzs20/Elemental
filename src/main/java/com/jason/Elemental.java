package com.jason;

import com.jason.elements.AffectedElement;
import com.jason.elements.Reaction;
import com.jason.test.TestSkills;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
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
		TestSkills.initialize();
		Reaction.initReaction();
		// Register the group.
		Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, ELEMENTAL_ITEM_TAB, CUSTOM_CREATIVE_TAB);
		CreativeModeTabEvents.modifyOutputEvent(ELEMENTAL_ITEM_TAB).register(output -> {
			output.accept(TestSkills.PYRO_CIRCLE);
			output.accept(TestSkills.HYDRO_CIRCLE);
		});
		ServerTickEvents.END_SERVER_TICK.register(server -> {
			Effects.tick(server);
			TestSkills.tick(server);
			for (ServerLevel level : server.getAllLevels()) {

			}
		});
//		var _ = AffectedElement.getAffectedElements(null);
//
//		ServerTickEvents.END_SERVER_TICK.register(server -> {
//			Effects.tick(server);
//			for (ServerLevel level : server.getAllLevels()) {
//				for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, new AABB(-30000000, -64, -30000000, 30000000, 320, 30000000))) {
//					AffectedElement.getAffectedElements(entity).tick();
//				}
//			}
//		});

	}
}
