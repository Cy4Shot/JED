package com.cy4.jed.json;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cy4.jed.config.JEDConfig;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.LanguageInfo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class JEDReloadListener implements ResourceManagerReloadListener {

	public static final JEDReloadListener INSTANCE = new JEDReloadListener();

	private static final Minecraft mc = Minecraft.getInstance();
	private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
	private static final Pattern UNSUPPORTED_FORMAT_PATTERN = Pattern.compile("%(\\d+\\$)?[\\d.]*[df]");
	private static final Logger LOGGER = LogManager.getLogger();
	private static final LanguageInfo EN_US = new LanguageInfo("en_us", "US", "English", false);

	private Map<LanguageInfo, Map<String, String>> map = new HashMap<>();

	protected void apply(List<ResourceLocation> objects, ResourceManager manager) {
		Set<String> languages = mc.getLanguageManager().languages.keySet();

		for (ResourceLocation entry : objects) {

			String name = StringUtils.substringBetween(entry.getPath(), "/", ".");
			if (languages.contains(name) && !JEDConfig.excludedModIds.get().contains(entry.getNamespace())) {

				Builder<String, String> builder = ImmutableMap.builder();
				BiConsumer<String, String> biconsumer = builder::put;

				try {
					InputStream in = mc.getResourceManager().getResource(entry).getInputStream();
					loadFromJson(in, biconsumer);
				} catch (JsonParseException | IOException exception) {
					LOGGER.error("Couldn't read strings from {}", entry.toString());
				}

				map.put(mc.getLanguageManager().getLanguage(name), new HashMap<>(builder.build()));
			}
		}
	}

	public static void loadFromJson(InputStream is, BiConsumer<String, String> con) {
		JsonObject jsonobject = GSON.fromJson(new InputStreamReader(is, StandardCharsets.UTF_8), JsonObject.class);

		for (Entry<String, JsonElement> entry : jsonobject.entrySet()) {
			String s = UNSUPPORTED_FORMAT_PATTERN.matcher(GsonHelper.convertToString(entry.getValue(), entry.getKey()))
					.replaceAll("%$1s");
			con.accept(entry.getKey(), s);
		}
	}

	public Map<String, String> getMap(LanguageInfo info) {
		if (JEDConfig.alwaysEnUs.get())
			return map.get(EN_US);
		return map.getOrDefault(info, map.get(EN_US));
	}

	@Override
	public void onResourceManagerReload(ResourceManager manager) {
		List<ResourceLocation> rls = manager.listResources("jed", s -> s.endsWith(".json")).stream()
				.collect(Collectors.toList());
		apply(rls, manager);
	}

}
