package com.cy4.jed.gui;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.text.WordUtils;

import com.cy4.jed.JustEnoughDescriptions;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;

import mezz.jei.gui.recipes.RecipesGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

@SuppressWarnings("deprecation")
public class ClientUtil {

	private static final Minecraft mc = Minecraft.getInstance();

	public static double getMouseX() {
		return mc.getWindow().getGuiScaledWidth() * mc.mouseHandler.xpos() / mc.getWindow().getScreenWidth();
	}

	public static double getMouseY() {
		return mc.getWindow().getGuiScaledHeight() * mc.mouseHandler.ypos() / mc.getWindow().getScreenHeight();
	}

	public static boolean isMouseInJeiWindow() {

		if (JustEnoughDescriptions.isJei()) {
			if (mc.screen instanceof RecipesGui)
				return ((RecipesGui) mc.screen).isMouseOver(getMouseX(), getMouseY());
		}

		return false;
	}

	public static void drawSplitString(PoseStack ms, Font font, Component str, int x, int y, int wrapWidth,
			int textColor, boolean revScale) {

		int i = 0;
		List<FormattedCharSequence> lines = font.split(str, wrapWidth).stream().collect(Collectors.toList());
		if (revScale)
			Collections.reverse(lines);

		for (FormattedCharSequence string : lines) {
			font.draw(ms, string, x, y + i * font.lineHeight * (revScale ? -1 : 1), textColor);
			i++;
		}
	}

	public static int getNumLines(Font font, Component str, int ww) {
		return font.split(str, ww).size();
	}

	public static boolean isJedKeyDown() {

		return JustEnoughDescriptions.openGui.getKeyModifier().isActive(null) && InputConstants
				.isKeyDown(mc.getWindow().getWindow(), JustEnoughDescriptions.openGui.getKey().getValue());
	}

	public static String getJedKeyName() {
		return WordUtils.capitalizeFully(JustEnoughDescriptions.openGui.getTranslatedKeyMessage().getString());
	}

}
