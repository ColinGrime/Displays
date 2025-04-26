package me.colingrimes.displays.command.display.fun.trapdoor;

import me.colingrimes.displays.Displays;
import me.colingrimes.displays.util.DisplayUtil;
import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.handler.util.ArgumentList;
import me.colingrimes.midnight.command.handler.util.CommandProperties;
import me.colingrimes.midnight.command.handler.util.Sender;
import me.colingrimes.midnight.event.PlayerInteractBlockEvent;
import me.colingrimes.midnight.plugin.MidnightPlugin;
import me.colingrimes.midnight.scheduler.Scheduler;
import me.colingrimes.midnight.util.Common;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.TrapDoor;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

public class DisplayTrapdoor implements Command<Displays>, Listener {

	private static final Transformation TRANSFORMATION_CLOSED = new Transformation(
			new Vector3f(0, 0, 0),
			new Quaternionf(),
			new Vector3f(1, 1, 1),
			new Quaternionf()
	);
	private static final Transformation TRANSFORMATION_OPENED = new Transformation(
			new Vector3f(0.2f, 0, 0),
			new Quaternionf().rotateZ((float) Math.toRadians(90)),
			new Vector3f(1, 1, 1),
			new Quaternionf()
	);

	private final Set<Block> trapdoors = new HashSet<>();

	public DisplayTrapdoor() {
		Common.register(MidnightPlugin.get(), this);
	}

	@Override
	public void execute(@Nonnull Displays plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		Player player = sender.player();
		RayTraceResult result = player.rayTraceBlocks(50);
		if (result == null || result.getHitBlock() == null) {
			return;
		}

		Block block = result.getHitBlock();
		if (block.getType().name().endsWith("TRAPDOOR")) {
			trapdoors.add(block);
		}
	}

	@Override
	public void configureProperties(@Nonnull CommandProperties properties) {
		properties.setPlayerRequired(true);
	}

	@EventHandler
	public void onPlayerInteractAtEntity(@Nonnull PlayerInteractBlockEvent event) {
		Block block = event.getBlock();
		if (!(block.getBlockData() instanceof TrapDoor trapDoor) || !trapdoors.contains(block)) {
			return;
		}

		event.setCancelled(true);

		// Temporarily remove the real trapdoor.
		Material temp = block.getType();
		block.setType(Material.AIR);

		// Animate the fake trapdoor.
		BlockDisplay displayBlock = DisplayUtil.createBlock(block.getLocation(), temp.createBlockData(), trapDoor.isOpen() ? TRANSFORMATION_OPENED : TRANSFORMATION_CLOSED);
		displayBlock.setInterpolationDelay(-1);
		displayBlock.setInterpolationDuration(10);
		Scheduler.sync().runLater(() -> {
			displayBlock.setTransformation(trapDoor.isOpen() ? TRANSFORMATION_CLOSED : TRANSFORMATION_OPENED);
		}, 2L);

		Scheduler.sync().runLater(() -> {
			// Remove the fake trapdoor.
			displayBlock.remove();

			// Place the real trapdoor back.
			block.setType(temp);

			// Open it up.
			trapDoor.setFacing(BlockFace.EAST);
			trapDoor.setOpen(!trapDoor.isOpen());
			block.setBlockData(trapDoor);
		}, 13L);
	}
}
