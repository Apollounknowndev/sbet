package dev.worldgen.sbet.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.Consumer;

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
	private static void sbet$addSpace(List<Text> tooltip, NbtList enchantments, CallbackInfo ci) {
		// TODO: Make this code like not shit
		for(int i = tooltip.size()-enchantments.size(); i < tooltip.size(); ++i) {
			tooltip.set(i, Text.literal(" ").append(tooltip.get(i)));
		}
	}
}