package com.cy4.jed.json;

import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.Item;

public class JEDDescriptionHelper {

	public static Component getDescriptionForItem(Item item) {
		Minecraft mc = Minecraft.getInstance();
		String reg = item.getRegistryName().toString();
		
		Map<String, String> map = JEDReloadListener.INSTANCE.getMap(mc.getLanguageManager().getSelected());
		
		if (map.containsKey(reg)) {
			return new TextComponent(map.get(item.getRegistryName().toString()));
		} else {
			if (map.containsKey("null")) {
				return new TextComponent(map.get("null"));
			}
			
			return new TextComponent("ERROR: NULL KEY NOT PRESENT");
		}
	}

}
