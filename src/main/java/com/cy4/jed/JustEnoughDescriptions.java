package com.cy4.jed;

import java.io.IOException;

import org.lwjgl.glfw.GLFW;

import com.cy4.jed.config.JEDConfig;
import com.cy4.jed.json.JEDReloadListener;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.network.NetworkConstants;

@Mod("jed")
public class JustEnoughDescriptions {

	public static final String MOD_ID = "jed";
	public static KeyMapping openGui = new KeyMapping("key." + MOD_ID + ".open_gui", GLFW.GLFW_KEY_LEFT_SHIFT,
			"key." + MOD_ID + ".categories." + MOD_ID);

	public JustEnoughDescriptions() {
		ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class,
				() -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY,
						(remote, isServer) -> true));

		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, JEDConfig.CLIENT_SPEC, "jed-client.toml");

		MinecraftForge.EVENT_BUS.register(this);
	}

	@Mod.EventBusSubscriber(modid = MOD_ID, bus = Bus.MOD, value = Dist.CLIENT)
	public class ClientForgeEvents {

		@SubscribeEvent
		public static void addReloadListener(RegisterClientReloadListenersEvent event) throws IOException {
			event.registerReloadListener(JEDReloadListener.INSTANCE);
		}

		@SubscribeEvent
		public static void clientSetup(FMLClientSetupEvent event) {
			ClientTimer.setup();
			ClientRegistry.registerKeyBinding(openGui);
		}
	}

	public static boolean isJei() {
		return ModList.get().isLoaded("jei") && JEDConfig.jeiIntegration.get();
	}
}
