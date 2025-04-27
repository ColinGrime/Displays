package me.colingrimes.displays.command.display.test.rotate;

import me.colingrimes.displays.Displays;
import me.colingrimes.displays.util.DisplayUtil;
import me.colingrimes.displays.util.Transformations;
import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.handler.util.ArgumentList;
import me.colingrimes.midnight.command.handler.util.CommandProperties;
import me.colingrimes.midnight.command.handler.util.Sender;
import me.colingrimes.midnight.scheduler.Scheduler;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Transformation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Stream;

public class DisplayRotate implements Command<Displays> {

	@Override
	public void execute(@Nonnull Displays plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		Player player = sender.player();
		RayTraceResult result = player.rayTraceBlocks(50);
		if (result == null || result.getHitBlock() == null) {
			return;
		}

		String rotate = args.getLowercase(0);
		if (!rotate.equals("x") && !rotate.equals("y") && !rotate.equals("z")) {
			return;
		}

		int degrees = args.getIntOrDefault(1, 90);
		Transformation transformation = switch (rotate) {
			case "x" -> Transformations.create().leftRotateX(degrees).build();
			case "y" -> Transformations.create().leftRotateY(degrees).build();
			case "z" -> Transformations.create().leftRotateZ(degrees).build();
			default -> null;
		};

		Block block = result.getHitBlock();
		Material type = block.getType();
		BlockData blockData = block.getBlockData();
		block.setType(Material.AIR);

		BlockDisplay blockDisplay = DisplayUtil.createBlock(block.getLocation(), blockData);
		blockDisplay.setInterpolationDelay(-1);
		blockDisplay.setInterpolationDuration(20);
		Scheduler.sync().runLater(() -> blockDisplay.setTransformation(transformation), 2L);

		// Remove block display & put back the block.
		Scheduler.sync().runLater(() -> {
			blockDisplay.remove();
			block.setType(type);
			block.setBlockData(blockData);
		}, 22L);
	}

	@Nullable
	@Override
	public List<String> tabComplete(@Nonnull Displays plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		if (args.size() == 1) {
			return Stream.of("x", "y", "z").toList();
		}
		return null;
	}

	@Override
	public void configureProperties(@Nonnull CommandProperties properties) {
		properties.setArgumentsRequired(2);
		properties.setPlayerRequired(true);
	}
}
