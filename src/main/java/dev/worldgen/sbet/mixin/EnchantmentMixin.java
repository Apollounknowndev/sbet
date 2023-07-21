package dev.worldgen.sbet.mixin;

import dev.worldgen.sbet.config.ConfigHandler;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {
    @Inject(method= "getName(I)Lnet/minecraft/text/Text;", at= @At(value = "RETURN"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void sbet$coloredEnchantmentText(int level, CallbackInfoReturnable<Text> cir, MutableText mutableText) {
        int color;
        if(((Enchantment)(Object)this).isCursed()) {
            color = ConfigHandler.getConfig().tooltipColors().cursedColor();
        } else if(((Enchantment)(Object)this).isTreasure()) {
            color = ConfigHandler.getConfig().tooltipColors().treasureColor();
        } else {
            color = ConfigHandler.getConfig().tooltipColors().regularColor();
        }
        mutableText.setStyle(Style.EMPTY.withColor(color));
    }
}
