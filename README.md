# JustEnoughDescriptions
JED is mod that adds descriptions to every item in Minecraft!

## Contributing to JED
To contribute to JED, join our [discord](https://discord.com/invite/x9Mj63m4QG). We are looking for **both** authors and translators, so join even if you only speak English!

## Integrating JED support into your mods / resourcepacks
To integrate JED support into your mods or resourcepacks, simply create a `jed` folder in your assets folder. For example:
- `assets/minecraft/jed` for Minecraft descriptions.
- `assets/yourmodid/jed` for custom mods.
Inside this folder, create `.json` files similar to the language format. These **must** be titled with the correct language ID, for example `en_us` or `ru_ru`. An example json is shown below:
```json
{
	"null": "This description hasn't been created yet! Contact the author of the mod or whoever is responsible for implementing JED!",
	"minecraft:diamond": "This is a diamond!",
	"minecraft:stick": "STICK is used for stick things",
	"minecraft:stone": "Stone Block is a block."
}
```
