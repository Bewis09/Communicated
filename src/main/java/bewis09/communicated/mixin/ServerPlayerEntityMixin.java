package bewis09.communicated.mixin;

import bewis09.communicated.item.components.LetterComponent;
import bewis09.communicated.util.PlayerEntityInvoker;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin implements PlayerEntityInvoker {
    @Override
    public void communicated$openPaper(@NotNull ItemStack book, int slot) {

    }

    @Unique
    @Override
    public void communicated$openLetter(LetterComponent component, Text text) {

    }
}
