package bewis09.communicated.mixin.client;

import bewis09.communicated.util.MixinChildClassIdentifier;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Objects;

@Mixin(TextFieldWidget.class)
class TextFieldWidgetMixin implements MixinChildClassIdentifier {
    @Shadow @Final private TextRenderer textRenderer;

    @Shadow private String text;

    @Redirect(method = "renderWidget", at= @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/OrderedText;III)I"))
    private int redirect(DrawContext instance, TextRenderer textRenderer, OrderedText text, int x, int y, int color) {
        if(Objects.equals(communicated$getClassName(), "envelope_title_widget"))
            return instance.drawText(textRenderer,text,x,y,0xFF333333,false);
        return instance.drawTextWithShadow(textRenderer,text,x,y,color);
    }

    @Redirect(method = "renderWidget", at= @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)I"))
    private int redirect(DrawContext instance, TextRenderer textRenderer, Text text, int x, int y, int color) {
        if(Objects.equals(communicated$getClassName(), "envelope_title_widget"))
            return instance.drawText(textRenderer,text,x,y,0xFF333333,false);
        return instance.drawTextWithShadow(textRenderer,text,x,y,color);
    }

    @Redirect(method = "renderWidget", at= @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)I"))
    private int redirect(DrawContext instance, TextRenderer textRenderer, String text, int x, int y, int color) {
        if(Objects.equals(communicated$getClassName(), "envelope_title_widget"))
            return instance.drawText(textRenderer,text,x,y,0xFF333333,false);
        return instance.drawTextWithShadow(textRenderer,text,x,y,color);
    }

    @Override
    public String communicated$getClassName() {
        return getClass().getName();
    }
}