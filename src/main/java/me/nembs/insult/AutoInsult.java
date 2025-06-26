package me.nembs.insult;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AutoInsult implements ModInitializer {
	public static final String MOD_ID = "autoinsult";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("AutoInsult Mod correctly initialized");
	}
}