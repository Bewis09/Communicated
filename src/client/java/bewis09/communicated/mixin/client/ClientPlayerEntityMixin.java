package bewis09.communicated.mixin.client;

import bewis09.communicated.Communicated;
import bewis09.communicated.item.components.LetterComponent;
import bewis09.communicated.screen.LetterPaperScreen;
import bewis09.communicated.screen.LetterViewingScreen;
import bewis09.communicated.util.PlayerEntityInvoker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin implements PlayerEntityInvoker {
    @Unique
    private final Text TITLE = Communicated.INSTANCE.translatedText("gui.letter_paper","Letter Paper");

    @Unique
    @Override
    public void communicated$openPaper(@NotNull ItemStack book, int slot) {
        MinecraftClient.getInstance().setScreen(new LetterPaperScreen(TITLE, book, slot));
    }

    @Unique
    @Override
    public void communicated$openLetter(LetterComponent component, Text text) {
        MinecraftClient.getInstance().setScreen(new LetterViewingScreen(component, text));
    }
}
