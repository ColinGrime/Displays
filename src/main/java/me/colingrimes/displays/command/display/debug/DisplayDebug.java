package me.colingrimes.displays.command.display.debug;

import me.colingrimes.displays.Displays;
import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.handler.util.ArgumentList;
import me.colingrimes.midnight.command.handler.util.Sender;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import javax.annotation.Nonnull;

public class DisplayDebug implements Command<Displays> {

	@Override
	public void execute(@Nonnull Displays plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		Command.super.execute(plugin, sender, args);
	}

	private void renderAxes(Location origin) {
		World world = origin.getWorld();

		// X axis (red wool)
		BlockDisplay xAxis = world.spawn(origin.clone().add(0.5, 0, 0), BlockDisplay.class, e -> {
			e.setBlock(Bukkit.createBlockData(Material.BLUE_WOOL));
			e.setTransformation(new Transformation(
					new Vector3f(0, 0, 0),
					new Quaternionf(),
					new Vector3f(0.25f, 0.25f, 0.25f),
					new Quaternionf()
			));
		});

		// Y axis (green wool)
		BlockDisplay yAxis = world.spawn(origin.clone().add(0, 0.5, 0), BlockDisplay.class, e -> {
			e.setBlock(Bukkit.createBlockData(Material.LIME_WOOL));
			e.setTransformation(new Transformation(
					new Vector3f(0, 0, 0),
					new Quaternionf(),
					new Vector3f(0.25f, 0.25f, 0.25f),
					new Quaternionf()
			));
		});

		// Z axis (blue wool)
		BlockDisplay zAxis = world.spawn(origin.clone().add(0, 0, 0.5), BlockDisplay.class, e -> {
			e.setBlock(Bukkit.createBlockData(Material.RED_WOOL));
			e.setTransformation(new Transformation(
					new Vector3f(0, 0, 0),
					new Quaternionf(),
					new Vector3f(0.25f, 0.25f, 0.25f),
					new Quaternionf()
			));
		});
	}
}
