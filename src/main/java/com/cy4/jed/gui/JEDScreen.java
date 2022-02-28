package com.cy4.jed.gui;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.cy4.jed.JustEnoughDescriptions;
import com.cy4.jed.json.JEDDescriptionHelper;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class JEDScreen extends Screen {

	private int xSize, ySize;
	private int x, y;

	private ItemStack stack;

	public JEDScreen(Component c, ItemStack s) {
		super(c);
		this.stack = s;
		this.xSize = 175;
		this.ySize = 201;
	}

	private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(JustEnoughDescriptions.MOD_ID,
			"textures/gui/jed.png");

	@Override
	public void render(PoseStack ms, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(ms);

		this.x = (this.width - this.xSize) / 2;
		this.y = (this.height - this.ySize) / 2;

		renderBg(ms);
		renderIt(ms);
		renderDc(ms);

		super.render(ms, mouseX, mouseY, partialTicks);
	}

	@Override
	public boolean keyPressed(int p_97765_, int p_97766_, int p_97767_) {
		if (super.keyPressed(p_97765_, p_97766_, p_97767_)) {
			return true;
		} else if (this.minecraft.options.keyInventory.isActiveAndMatches(InputConstants.getKey(p_97765_, p_97766_))) {
			this.onClose();
			return true;
		}

		return false;
	}

	private void renderBg(PoseStack ms) {
		RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, GUI_TEXTURE);
		this.blit(ms, x, y, 0, 0, this.xSize, this.ySize);
	}

	private void renderIt(PoseStack ms) {
		Component text = stack.getItem().getName(stack);
		int wrap = this.xSize - 50;

		if (getNumLines(font, text, wrap) == 1) {
			drawSplitString(ms, font, JEDDescriptionHelper.getItemTitle(stack), x + 10, y + 18, this.xSize - 50,
					0x000000, false);
		} else {
			drawSplitString(ms, font, JEDDescriptionHelper.getItemTitle(stack), x + 10, y + 22, this.xSize - 50,
					0x000000, true);
		}

		setBlitOffset(100);
		RenderSystem.enableDepthTest();
		this.itemRenderer.renderAndDecorateItem(stack, x + 146, y + 14, 16, 16);
		setBlitOffset(0);
	}

	private void renderDc(PoseStack ms) {
		drawSplitString(ms, font, JEDDescriptionHelper.getDescriptionForItem(stack), x + 10, y + 40, this.xSize - 20,
				4210752, false);
	}

	public void drawSplitString(PoseStack ms, Font fontRenderer, Component str, int x, int y, int wrapWidth,
			int textColor, boolean revScale) {

		int i = 0;
		List<FormattedCharSequence> lines = fontRenderer.split(str, wrapWidth).stream().collect(Collectors.toList());
		if (revScale)
			Collections.reverse(lines);

		for (FormattedCharSequence string : lines) {
			font.draw(ms, string, x, y + i * fontRenderer.lineHeight * (revScale ? -1 : 1), textColor);
			i++;
		}
	}

	public int getNumLines(Font font, Component str, int ww) {
		return font.split(str, ww).size();
	}

	@Override
	public void setBlitOffset(int offset) {
		super.setBlitOffset(offset);
		this.itemRenderer.blitOffset = offset;
	}
}
