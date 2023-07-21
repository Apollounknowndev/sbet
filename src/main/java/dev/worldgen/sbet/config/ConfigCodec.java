package dev.worldgen.sbet.config;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.registry.Registries;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigCodec {
    public static final Map<Enchantment, List<String>> DEFAULT_DESCRIPTIONS = new HashMap<>() {{
    put(Enchantments.AQUA_AFFINITY, List.of(
            "Increases underwater mining speed"
    ));
    put(Enchantments.BANE_OF_ARTHROPODS, List.of(
            "Increases damage to arthropods",
            "such as spiders and bees"
    ));
    put(Enchantments.BLAST_PROTECTION, List.of(
            "Increases protection against",
            "explosion damage and knockback"
    ));
    put(Enchantments.CHANNELING, List.of(
            "Summons a lightning bolt",
            "on the target hit"
    ));
    put(Enchantments.BINDING_CURSE, List.of(
            "The armor piece cannot",
            "be removed once equipped"
    ));
    put(Enchantments.VANISHING_CURSE, List.of(
            "The item vanishes upon death"
    ));
    put(Enchantments.DEPTH_STRIDER, List.of(
            "Increases underwater movement speed"
    ));
    put(Enchantments.EFFICIENCY, List.of(
            "Increases tool speed"
    ));
    put(Enchantments.FEATHER_FALLING, List.of(
            "Decreases impact of fall damage"
    ));
    put(Enchantments.FIRE_ASPECT, List.of(
            "Sets the target ablaze"
    ));
    put(Enchantments.FIRE_PROTECTION, List.of(
            "Increases protection against",
            "fire damage and burn time"
    ));
    put(Enchantments.FLAME, List.of(
            "Sets the arrow shot ablaze"
    ));
    put(Enchantments.FORTUNE, List.of(
            "Increases block drops"
    ));
    put(Enchantments.FROST_WALKER, List.of(
            "Freezes the water below into ice"
    ));
    put(Enchantments.IMPALING, List.of(
            "Increases damage against",
            "aquatic mobs"
    ));
    put(Enchantments.INFINITY, List.of(
            "Prevents regular arrows",
            "from being consumed upon",
            "being fired"
    ));
    put(Enchantments.KNOCKBACK, List.of(
            "Increases how far mobs are",
            "knocked back from attacks"
    ));
    put(Enchantments.LOOTING, List.of(
            "Increases mob drops"
    ));
    put(Enchantments.LOYALTY, List.of(
            "Returns to the player after",
            "hitting the ground or target"
    ));
    put(Enchantments.LUCK_OF_THE_SEA, List.of(
            "Increases fishing luck"
    ));
    put(Enchantments.LURE, List.of(
            "Decreases time to catch a fish"
    ));
    put(Enchantments.MENDING, List.of(
            "Collected experience is",
            "used to repair the item"
    ));
    put(Enchantments.MULTISHOT, List.of(
            "Fires three arrows whilst",
            "only consuming one"
    ));
    put(Enchantments.PIERCING, List.of(
            "Fired arrows will pierce",
            "through targets"
    ));
    put(Enchantments.POWER, List.of(
            "Increases arrow damage"
    ));
    put(Enchantments.PROJECTILE_PROTECTION, List.of(
            "Increased protection against",
            "projectiles"
    ));
    put(Enchantments.PROTECTION, List.of(
            "Increased general protection"
    ));
    put(Enchantments.PUNCH, List.of(
            "Increases how far mobs are",
            "knocked back from arrows"
    ));
    put(Enchantments.QUICK_CHARGE, List.of(
            "Decreases the time for the",
            "crossbow to load an arrow"
    ));
    put(Enchantments.RESPIRATION, List.of(
            "Increases the time that can be spent",
            "underwater before beginning to drown"
    ));
    put(Enchantments.RIPTIDE, List.of(
            "Launches player using the item",
            "when in water or rain"
    ));
    put(Enchantments.SHARPNESS, List.of(
            "Increases general damage"
    ));
    put(Enchantments.SILK_TOUCH, List.of(
            "Allows certain blocks to drop",
            "themselves, such as ores"
    ));
    put(Enchantments.SMITE, List.of(
            "Increases damage against undead",
            "mobs, such as zombies and skeletons"
    ));
    put(Enchantments.SOUL_SPEED, List.of(
            "Increases movement speed on",
            "soul sand and soul soil"
    ));
    put(Enchantments.SWEEPING, List.of(
            "Increases damage from",
            "sweeping attacks"
    ));
    put(Enchantments.SWIFT_SNEAK, List.of(
            "Increases movement speed",
            "when sneaking"
    ));
    put(Enchantments.THORNS, List.of(
            "Causes attackers to take damage"
    ));
    put(Enchantments.UNBREAKING, List.of(
            "Reduces durability loss"
    ));
}};
    public static final Codec<ConfigCodec> CODEC = RecordCodecBuilder.create((instance) -> {
        return instance.group(
            TooltipColors.CODEC.fieldOf("tooltip_colors").forGetter(ConfigCodec::tooltipColors),
            Codec.BOOL.fieldOf("descriptions_enabled").orElse(true).forGetter(ConfigCodec::descriptionsEnabled),
            Codec.simpleMap(Registries.ENCHANTMENT.getCodec(), Codec.STRING.listOf(), Registries.ENCHANTMENT).fieldOf("descriptions").orElse(DEFAULT_DESCRIPTIONS).forGetter(ConfigCodec::descriptions)
        ).apply(instance, ConfigCodec::new);
    });


    private final TooltipColors tooltipColors;
    private final boolean descriptionsEnabled;
    private final Map<Enchantment, List<String>> descriptions;
    public ConfigCodec(TooltipColors tooltipColors, boolean descriptionsEnabled, Map<Enchantment, List<String>> descriptions) {
        this.tooltipColors = tooltipColors;
        this.descriptionsEnabled = descriptionsEnabled;
        this.descriptions = ImmutableMap.copyOf(descriptions);
    }

    public TooltipColors tooltipColors() {
        return this.tooltipColors;
    }
    public boolean descriptionsEnabled() {
        return this.descriptionsEnabled;
    }

    public Map<Enchantment, List<String>> descriptions() {
        return this.descriptions;
    }

    public record TooltipColors(Integer regularColor, Integer treasureColor, Integer cursedColor) {
        public static final Codec<TooltipColors> CODEC = RecordCodecBuilder.create((instance) -> {
            return instance.group(
                Codec.INT.fieldOf("regular").orElse(5635925).forGetter(TooltipColors::regularColor),
                Codec.INT.fieldOf("treasure").orElse(16733695).forGetter(TooltipColors::treasureColor),
                Codec.INT.fieldOf("cursed").orElse(16733525).forGetter(TooltipColors::cursedColor)
            ).apply(instance, TooltipColors::new);
        });

        public Integer regularColor() {
            return this.regularColor;
        }

        public Integer treasureColor() {
            return this.treasureColor;
        }

        public Integer cursedColor() {
            return this.cursedColor;
        }
    }
}
