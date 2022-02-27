package com.cy4.jed;

import net.minecraft.client.Minecraft;

public final class ClientTimer {
	public static long gameTick = 0;
	public static float partialTick = 0;
	public static float deltaTick = 0;
	public static float totalTick = 0;

	private static void calcDelta() {
		float oldTotal = totalTick;
		totalTick = gameTick + partialTick;
		deltaTick = totalTick - oldTotal;
	}

	public static void renderTickBegin(float pt) {
		partialTick = pt;
	}

	public static void renderTickFinish() {
		calcDelta();
	}

	public static void endTick(Minecraft mc) {
		gameTick++;
		partialTick = 0;

		calcDelta();
	}
}	
