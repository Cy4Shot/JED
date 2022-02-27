package com.cy4.jed.gui;

import com.cy4.jed.ClientTimer;
import com.cy4.jed.JustEnoughDescriptions;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = JustEnoughDescriptions.MOD_ID, value = Dist.CLIENT)
public class JEDTooltip {

	private static final float maxTimer = 10f;
	private static float holdTimer = 0;

	@SubscribeEvent
	public static void onTooltip(ItemTooltipEvent event) {
		Minecraft mc = Minecraft.getInstance();

		if (mc.player != null) {
			event.getToolTip()
					.add(new TextComponent("Hold ").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY).append(new TextComponent("Shift").withStyle(ChatFormatting.AQUA)));

			if (Screen.hasShiftDown()) {
				holdTimer += ClientTimer.deltaTick;

				if (holdTimer >= maxTimer) {
					mc.setScreen(new JEDScreen(TextComponent.EMPTY, event.getItemStack()));
				}
			} else {
				holdTimer = 0;
			}
		} else {
			holdTimer = 0;
		}
	}

	@SubscribeEvent
	public static void onTooltipRender(RenderTooltipEvent.Pre event) {
		if (Screen.hasShiftDown()) {
			int i = 0;
			int j = event.getComponents().size() == 1 ? -2 : 0;

			for (ClientTooltipComponent clienttooltipcomponent : event.getComponents()) {
				int k = clienttooltipcomponent.getWidth(event.getFont());
				if (k > i) {
					i = k;
				}

				j += clienttooltipcomponent.getHeight();
			}

			int j2 = event.getX() + 10;
			int k2 = event.getY() - 12;

			float ratio = holdTimer / maxTimer;

			RenderSystem.disableDepthTest();
			GuiComponent.fill(new PoseStack(), j2, k2 + j + 4, j2 + i + 4, k2 + j + 6, 0xFF444444);
			GuiComponent.fill(new PoseStack(), j2, k2 + j + 4, j2 + 4 + (int)(i * ratio), k2 + j + 6, 0xFFFFFFFF);
			RenderSystem.enableDepthTest();
		}
	}
}
