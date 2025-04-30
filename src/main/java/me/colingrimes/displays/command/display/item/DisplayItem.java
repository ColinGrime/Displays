package me.colingrimes.displays.command.display.item;

import me.colingrimes.displays.Displays;
import me.colingrimes.displays.util.DisplayUtil;
import me.colingrimes.displays.util.Utils;
import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.handler.util.ArgumentList;
import me.colingrimes.midnight.command.handler.util.CommandProperties;
import me.colingrimes.midnight.command.handler.util.Sender;
import me.colingrimes.midnight.util.text.Parser;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ItemDisplay;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Stream;

public class DisplayItem implements Command<Displays> {

	@Override
	public void execute(@Nonnull Displays plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		Block block = Utils.rayTraceBlock(sender.player());
		if (block == null) {
			return;
		}

		Material material = Material.getMaterial(args.getOrDefault(0, "DIAMOND"));
		ItemDisplay.ItemDisplayTransform transform = Parser.parseNullable(ItemDisplay.ItemDisplayTransform.class, args.getOrDefault(1, "FIXED"));
		if (material == null || transform == null) {
			return;
		}

		Location location = block.getLocation();
		DisplayUtil.createItem(location.clone().add(0.5, 1.5, 0.5), material, transform);
	}

	@Nullable
	@Override
	public List<String> tabComplete(@Nonnull Displays plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		if (args.size() == 1) {
			return Stream.of(Material.values()).filter(m -> m.isItem() && m.name().contains(args.getFirst())).map(Material::name).toList();
		} else if (args.size() == 2) {
			return Stream.of(ItemDisplay.ItemDisplayTransform.values()).map(ItemDisplay.ItemDisplayTransform::name).toList();
		}
		return null;
	}

	@Override
	public void configureProperties(@Nonnull CommandProperties properties) {
		properties.setPlayerRequired(true);
	}
}
