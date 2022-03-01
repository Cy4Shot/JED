package com.cy4.jed.gui;

import com.cy4.jed.JustEnoughDescriptions;

import mezz.jei.gui.recipes.RecipesGui;
import net.minecraft.client.Minecraft;

public class ClientUtil {

	public static double getMouseX() {
		Minecraft mc = Minecraft.getInstance();
		return mc.getWindow().getGuiScaledWidth() * mc.mouseHandler.xpos() / mc.getWindow().getScreenWidth();
	}

	public static double getMouseY() {
		Minecraft mc = Minecraft.getInstance();
		return mc.getWindow().getGuiScaledHeight() * mc.mouseHandler.ypos() / mc.getWindow().getScreenHeight();
	}

	public static boolean isMouseInJeiWindow() {
		Minecraft mc = Minecraft.getInstance();

		if (JustEnoughDescriptions.isJei()) {
			if (mc.screen instanceof RecipesGui) {
				
				RecipesGui gui = ((RecipesGui) mc.screen);
				return gui.isMouseOver(getMouseX(), getMouseY());
			}
		}

		return false;
	}

}
