package com.cy4.jed.gui;

import com.cy4.jed.ClientTimer;
import com.cy4.jed.JustEnoughDescriptions;
import com.cy4.jed.config.JEDConfig;
import com.cy4.jed.json.JEDDescriptionHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = JustEnoughDescriptions.MOD_ID, value = Dist.CLIENT)
public class JEDTooltip {
	private static final Minecraft mc = Minecraft.getInstance();
	private static float holdTimer = 0;

	@SubscribeEvent
	public static void onTooltip(ItemTooltipEvent event) {

		if (ClientUtil.isMouseInJeiWindow())
			return;

		if (!(JEDDescriptionHelper.hasDescriptionForItem(event.getItemStack())) && !(JEDConfig.allowIfEmpty.get()))
			return;

		if (mc.player != null) {
			event.getToolTip()
					.add(new TextComponent("Hold ").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY)
							.append(new TextComponent(ClientUtil.getJedKeyName()).withStyle(ChatFormatting.AQUA)));

			if (ClientUtil.isJedKeyDown()) {
				holdTimer += ClientTimer.deltaTick;

				if (holdTimer >= JEDConfig.tooltipActivationTime.get().floatValue()) {
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

		if (ClientUtil.isMouseInJeiWindow() || event.getItemStack().isEmpty())
			return;

		if (JEDConfig.tooltipActivationTime.get().floatValue() <= 0.1)
			return;

		if (!(JEDDescriptionHelper.hasDescriptionForItem(event.getItemStack())) && !(JEDConfig.allowIfEmpty.get()))
			return;

		if (ClientUtil.isJedKeyDown()) {

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

			if (j2 + i > mc.screen.width) {
				j2 -= 28 + i;
			}

			if (k2 + j + 6 > mc.screen.height) {
				k2 = mc.screen.height - j - 6;
			}

			float ratio = holdTimer / JEDConfig.tooltipActivationTime.get().floatValue();

			if (ratio != 0f) {
				RenderSystem.disableDepthTest();
				GuiComponent.fill(new PoseStack(), j2, k2 + j + 4, j2 + i + 4, k2 + j + 6, 0xFF444444);
				GuiComponent.fill(new PoseStack(), j2, k2 + j + 4, j2 + 4 + (int) (i * ratio), k2 + j + 6, 0xFFFFFFFF);
				RenderSystem.enableDepthTest();
			}
		}
	}
}
