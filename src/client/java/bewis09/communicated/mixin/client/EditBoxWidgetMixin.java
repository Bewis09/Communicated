package bewis09.communicated.mixin.client;

import bewis09.communicated.drawable.LetterPaperEditBox;
import bewis09.communicated.util.MixinChildClassIdentifier;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.EditBoxWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Objects;

@Mixin(EditBoxWidget.class)
public class EditBoxWidgetMixin implements MixinChildClassIdentifier {
    @Redirect(method = "renderContents", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)I"))
    private int injected(DrawContext instance, TextRenderer textRenderer, String text, int x, int y, int color) {
        if(Objects.equals(communicated$getClassName(), LetterPaperEditBox.class.getName()))
            return instance.drawText(textRenderer,text,x,y,0xFF000000,false);
        return instance.drawTextWithShadow(textRenderer,text,x,y,color);
    }

    @ModifyArg(method = "renderContents", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fill(IIIII)V", ordinal = 0), index = 4)
    private int injectedFillColor(int x) {
        return 0xFF000000;
    }

    @ModifyArg(method = "renderContents", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)I", ordinal = 1), index = 2)
    private int injectedTextPlacement(int x) {
        return x+1;
    }

    @Override
    public String communicated$getClassName() {
        return this.getClass().getName();
    }
}
