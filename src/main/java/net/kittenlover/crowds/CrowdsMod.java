package net.kittenlover.crowds;

import static net.minecraft.server.command.CommandManager.literal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.world.Difficulty;

public class CrowdsMod implements ModInitializer {
	public static final Logger LOGGER = LogManager.getLogger("crowds");

	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
			dispatcher.register(literal("crowds").executes(context -> {
				boolean active = HoardManager.fromWorld(context.getSource().getWorld()).active;
				context.getSource().getPlayer()
						.sendMessage(new LiteralText("crowds are " + (active ? "active" : "inactive")), false);
				return 1;
			}).then(literal("start").executes(context -> {
				if (context.getSource().getWorld().getDifficulty() == Difficulty.PEACEFUL)
					return 0;

				var hoard_manager = HoardManager.fromWorld(context.getSource().getWorld());
				hoard_manager.activate();
				return 1;
			})).then(literal("stop").executes(context -> {
				var hoard_manager = HoardManager.fromWorld(context.getSource().getWorld());
				hoard_manager.deactivate();
				return 1;
			})));
		});

		ServerTickEvents.START_SERVER_TICK.register(context -> {
			for (var world : context.getWorlds()) {
				var hoard_manager = HoardManager.fromWorld(world);
				if (hoard_manager.active)
					hoard_manager.tick();
			}
		});

		LOGGER.info("Hello Fabric world!");
	}
}
