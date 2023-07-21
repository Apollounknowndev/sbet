package dev.worldgen.sbet.mixin;

import dev.worldgen.sbet.SBET;
import dev.worldgen.sbet.config.ConfigCodec;
import dev.worldgen.sbet.config.ConfigHandler;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TextContent;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(ItemStack.class)
public class ItemStackMixin {
	@Inject(at = @At("HEAD"), method = "appendEnchantments(Ljava/util/List;Lnet/minecraft/nbt/NbtList;)V")
	private static void sbet$addEnchantmentText(List<Text> tooltip, NbtList enchantments, CallbackInfo info) {
		MutableText mutableText;
		if (enchantments.size() == 0) {
			return;
		} else if (enchantments.size() == 1) {
			mutableText = Text.translatable("item.sbet.enchantment");
		} else {
			mutableText = Text.translatable("item.sbet.enchantments");
		}
		mutableText.formatted(Formatting.GRAY);
		tooltip.add(mutableText);
	}

	@Inject(
		method= "appendEnchantments(Ljava/util/List;Lnet/minecraft/nbt/NbtList;)V",
		at= @At(value = "TAIL")
	)
	private static void sbet$addSpaceAndTooltips(List<Text> tooltip, NbtList enchantments, CallbackInfo ci) {
		if (enchantments.size() > 0) {
			List<Text> tooltipCopy = new ArrayList<Text>();
			for(int i = 0; i < tooltip.size()-enchantments.size(); i++) {
				tooltipCopy.add(tooltip.get(i));
			}
			for(int i = 0; i < enchantments.size(); i++) {
				int tooltipLine = tooltip.size()-enchantments.size()+i;
				tooltipCopy.add(Text.literal(" ").append(tooltip.get(tooltipLine)));
				if (ConfigHandler.getConfig().descriptionsEnabled()) {
					Enchantment enchantmentId =
							Registries.ENCHANTMENT.get(EnchantmentHelper.getIdFromNbt(enchantments.getCompound(i)));
					if (enchantmentId != null) {
						if (ConfigHandler.getConfig().descriptions().containsKey(enchantmentId)) {
							for (String descriptionLine :
									ConfigHandler.getConfig().descriptions().get(enchantmentId)) {
								MutableText mutableText = Text.literal("  ").append(descriptionLine);
								tooltipCopy.add(mutableText.formatted(Formatting.DARK_GRAY));
							}
						} else if (I18n.hasTranslation(enchantmentId.getTranslationKey() + ".desc")) {
							tooltipCopy.add(Text.literal("  ").append(Text.translatable(enchantmentId.getTranslationKey() + ".desc")).formatted(Formatting.DARK_GRAY));
						}
					}
				}
			}
            tooltip.clear();
			tooltip.addAll(tooltipCopy);
		}
	}
}