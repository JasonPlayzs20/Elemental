package com.jason.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class ElementalClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// Spawn element particles on affected entities
//		ClientTickEvents.END_CLIENT_TICK.register(ElementParticleHandler::onClientTick);
	}
}