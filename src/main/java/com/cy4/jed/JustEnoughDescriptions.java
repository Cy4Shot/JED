package com.cy4.jed;

import java.io.IOException;

import com.cy4.jed.json.JEDReloadListener;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.network.NetworkConstants;

@Mod("jed")
public class JustEnoughDescriptions {
	
	public static final String MOD_ID = "jed";
	
	public JustEnoughDescriptions() {
		ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class,
				() -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY,
						(remote, isServer) -> true));

		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@Mod.EventBusSubscriber(modid = MOD_ID, bus = Bus.MOD, value = Dist.CLIENT)
	public class ClientForgeEvents {

		@SubscribeEvent
		public static void addReloadListener(RegisterClientReloadListenersEvent  event) throws IOException {
			event.registerReloadListener(JEDReloadListener.INSTANCE);
		}
		
		@SubscribeEvent
		public static void onInitializeClient(FMLClientSetupEvent evt) {
			MinecraftForge.EVENT_BUS.addListener((TickEvent.ClientTickEvent e) -> {
				if (e.phase == TickEvent.Phase.END) {
					ClientTimer.endTick(Minecraft.getInstance());
				}
			});
			
			MinecraftForge.EVENT_BUS.addListener((TickEvent.RenderTickEvent e) -> {
				if (e.phase == TickEvent.Phase.START) {
					ClientTimer.renderTickBegin(e.renderTickTime);
				} else {
					ClientTimer.renderTickFinish();
				}
			});
		}
	}
}
