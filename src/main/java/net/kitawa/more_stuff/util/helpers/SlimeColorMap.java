package net.kitawa.more_stuff.util.helpers;

import org.spongepowered.asm.mixin.Unique;

import java.util.Map;
import java.util.Set;

public class SlimeColorMap {
    public static Map<String, Set<String>> SLIME_STICK_RULES = Map.ofEntries(

            // -----------------------
            // Core colors
            // -----------------------
            Map.entry("red", Set.of("red", "orange", "pink", "brown", "purple")),
            Map.entry("blue", Set.of("blue", "cyan", "light_blue", "purple", "green")),
            Map.entry("yellow", Set.of("yellow", "orange", "lime", "green")),

            // -----------------------
            // Mixed / secondary
            // -----------------------
            Map.entry("green", Set.of("green", "lime", "cyan", "blue", "yellow")),
            Map.entry("orange", Set.of("orange", "red", "yellow")),
            Map.entry("purple", Set.of("purple", "magenta", "blue", "red")),

            // -----------------------
            // Light colors
            // -----------------------
            Map.entry("lime", Set.of("lime", "green", "white")),
            Map.entry("light_blue", Set.of("light_blue", "blue", "white")),
            Map.entry("pink", Set.of("pink", "red", "white")),
            Map.entry("magenta", Set.of("magenta", "purple", "pink")),

            // -----------------------
            // Neutrals
            // -----------------------
            Map.entry("white", Set.of(
                    "white",
                    "lime",
                    "light_blue",
                    "pink",
                    "light_gray"
            )),

            Map.entry("black", Set.of("black", "gray", "brown")),
            Map.entry("gray", Set.of("gray", "light_gray", "black")),
            Map.entry("light_gray", Set.of("light_gray", "white", "gray")),
            Map.entry("brown", Set.of("brown", "black", "red")),

            // -----------------------
            // Special colors
            // -----------------------
            Map.entry("cyan", Set.of("cyan", "blue", "green", "light_blue")),

            // -----------------------
            // Non-sticky
            // -----------------------
            Map.entry("clear", Set.of("black", "tinted")),
            Map.entry("tinted", Set.of("clear", "black"))
    );
}
