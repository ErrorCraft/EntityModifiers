# Entity Modifiers
This is a fabric mod that adds entity modifiers to Minecraft.
There's a [wiki](https://github.com/ErrorCraft/EntityModifiers/wiki) for more information about the entity modifier functions and the `entity` command.

Entity modifiers are similar to item modifiers, except that they work with entities rather than items!
They are supposed to replace the `data modify entity ...` commands.
This makes changing entities much more powerful, as we can provide context to what we want to modify.
- We know what we want to change. This means we can have validation of our input!
- We can use number providers rather than having to use `execute store ...`. This also means it doesn't limit us to just the integer range, as we are able to use floating point numbers as well!
- When we are working with text components we can use advanced components such as `score` that resolve properly.
- Because we are editing direct values and not save data, we can apply this to players as well! This enables player-specific features like hunger.

## Downloads
|**Version**|**Game Version**|**Link**|
|-|-|-|
|1.0.0|1.17.x|[Download](https://github.com/ErrorCraft/EntityModifiers/releases/tag/v1.0.0)|
|1.1.0|1.18.x|[Download](https://github.com/ErrorCraft/EntityModifiers/releases/tag/v1.1.0)|

## Using entity modifiers
Entity modifiers are JSON files in data packs.
You can create entity modifiers under the `modifiers/entities` folder in your namespace.
This structure is similar to what tags and loot tables use.
Inside the file you can either have one or an array of modifiers, just like in item modifiers!
The modifiers can be applied using the `entity` command, which follows a similar syntax to the `item` and `data` commands.
