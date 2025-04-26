package me.colingrimes.displays.command.display;

import me.colingrimes.displays.Displays;
import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.handler.util.ArgumentList;
import me.colingrimes.midnight.command.handler.util.CommandProperties;
import me.colingrimes.midnight.command.handler.util.Sender;
import me.colingrimes.midnight.scheduler.Scheduler;
import org.bukkit.*;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import javax.annotation.Nonnull;

public class DisplayCommand implements Command<Displays> {

	@Override
	public void execute(@Nonnull Displays plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		World world = sender.world();
		Location location = sender.location().add(0, 0.5, 2);
		location.setX(location.getBlockX() - 0.2);
		location.setY(location.getBlockY() + 0.4);
		location.setZ(location.getBlockZ() + 0.5);
		location.setPitch(0);
		location.setYaw(0);

		ItemDisplay display = location.getWorld().spawn(location, ItemDisplay.class, entity -> {
			entity.setItemStack(new ItemStack(Material.DIAMOND_PICKAXE));
			entity.setPersistent(false);
			entity.setGlowing(true);
			entity.setGlowColorOverride(Color.BLACK);
			entity.setTransformation(new Transformation(
					new Vector3f(0, 0, 0),
					new Quaternionf().rotateZ((float) Math.toRadians(-45)),
					new Vector3f(0.75f, 0.75f, 0.75f),
					new Quaternionf()
			));
			entity.setInterpolationDelay(0);
			entity.setInterpolationDuration(10);
		});
//
//		display.setTransformation(new Transformation(
//				new Vector3f(0, 0, 0),
//				new Quaternionf().rotateZ((float) Math.toRadians(70)),
//				new Vector3f(1, 1, 1),
//				new Quaternionf()
//		));

		display.setInterpolationDelay(0);
		display.setInterpolationDuration(10);

		Scheduler.sync().runRepeating(new Runnable() {
			float angle = -45f; // start at -45°
			boolean swingingDown = true;
			int pauseTicks = 0;
			Vector3f position = new Vector3f(0, 0, 0); // starting offset

			@Override
			public void run() {
				if (pauseTicks > 0) {
					pauseTicks--;
					return;
				}

				if (swingingDown) {
					angle += 20f;

					if (angle >= 70f) {
						angle = 70f;
						pauseTicks = 3;
						swingingDown = false;

						// Break block below
						world.getBlockAt(display.getLocation().clone().add(position.x, position.y - 1, position.z)).breakNaturally(display.getItemStack());

						// Move forward one block
						position.add(new Vector3f(-1, 0, 0)); // move +X (east), adjust if needed

						// Update transformation including new position
						display.setTransformation(new Transformation(
								new Vector3f(position),
								new Quaternionf().rotateZ((float) Math.toRadians(angle)),
								new Vector3f(0.75f, 0.75f, 0.75f),
								new Quaternionf()
						));
						return;
					}
				} else {
					angle -= 20f;

					if (angle <= -45f) {
						angle = -45f;
						pauseTicks = 2;
						swingingDown = true;
					}
				}

				// Normal transform update
				display.setTransformation(new Transformation(
						new Vector3f(position),
						new Quaternionf().rotateZ((float) Math.toRadians(angle)),
						new Vector3f(0.75f, 0.75f, 0.75f),
						new Quaternionf()
				));
			}
		}, 0L, 1L);




//		Scheduler.sync().runRepeating(() -> {
//			display.setTransformation(display.getTransformation().);
//			display.setTransformation(new Transformation(
//					new Vector3f(0, 0, 0),
//					new Quaternionf().rotateZ((float) Math.toRadians(45)),
//					new Vector3f(1, 1, 1),
//					new Quaternionf()
//			));
//			display.setInterpolationDuration(10);
//			Scheduler.sync().runLater(() -> {
//				display.setTransformation(display.getTransformation());
//				display.setTransformation(new Transformation(
//						new Vector3f(0, 0, 0),
//						new Quaternionf().rotateZ((float) -Math.toRadians(45)),
//						new Vector3f(1, 1, 1),
//						new Quaternionf()
//				));
//			}, 10L);
//		}, 0L, 20L);

//		create(location, Material.STONE, new Transformation(
//				new Vector3f(0, 0, 0),
//				new Quaternionf().rotateZ((float)Math.toRadians(45)), // rotate 45° before scale
//				new Vector3f(2, 1, 1), // scale X by 2
//				new Quaternionf() // no right rotation
//		));
//
//		create(location.clone().add(0, 2, 0), Material.DIRT, new Transformation(
//				new Vector3f(0, 0, 0),
//				new Quaternionf(),
//				new Vector3f(2, 1, 1), // scale X by 2
//				new Quaternionf() // no right rotation
//		));
	}

	@Override
	public void configureProperties(@Nonnull CommandProperties properties) {
		properties.setPlayerRequired(true);
	}
}
