package com.cy4.jed.compat;

import com.cy4.jed.JustEnoughDescriptions;
import com.cy4.jed.json.JEDDescriptionHelper;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class JEDJeiPlugin implements IModPlugin {

	@Override
	public ResourceLocation getPluginUid() {
		return new ResourceLocation(JustEnoughDescriptions.MOD_ID, JustEnoughDescriptions.MOD_ID);
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		if (JustEnoughDescriptions.isJei()) {
			registration.getIngredientManager().getAllIngredients(VanillaTypes.ITEM).forEach(ingredient -> {
				registration.addIngredientInfo(ingredient, VanillaTypes.ITEM,
						JEDDescriptionHelper.getDescriptionForItem(ingredient));
			});
		}
	}

}
