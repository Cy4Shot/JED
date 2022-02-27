package com.cy4.jed.gui;

import com.cy4.jed.JustEnoughDescriptions;
import com.cy4.jed.json.JEDDescriptionHelper;
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
		renderItem();
		renderDc(ms);

		super.render(ms, mouseX, mouseY, partialTicks);
	}

	private void renderBg(PoseStack ms) {
		RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, GUI_TEXTURE);
		this.blit(ms, x, y, 0, 0, this.xSize, this.ySize);
	}

	private void renderItem() {
		setBlitOffset(100);
		RenderSystem.enableDepthTest();
		this.itemRenderer.renderAndDecorateItem(stack, x + 80, y + 15, 16, 16);
		setBlitOffset(0);
	}

	private void renderDc(PoseStack ms) {
		drawSplitString(ms, font, JEDDescriptionHelper.getDescriptionForItem(stack.getItem()), x + 10, y + 40,
				this.xSize - 20, 4210752);
	}

	public void drawSplitString(PoseStack ms, Font fontRenderer, Component str, int x, int y, int wrapWidth,
			int textColor) {

		int i = 0;
		for (FormattedCharSequence string : fontRenderer.split(str, wrapWidth)) {
			font.draw(ms, string, x, y + i * fontRenderer.lineHeight, textColor);
			i++;
		}
	}

	@Override
	public void setBlitOffset(int offset) {
		super.setBlitOffset(offset);
		this.itemRenderer.blitOffset = offset;
	}
}
