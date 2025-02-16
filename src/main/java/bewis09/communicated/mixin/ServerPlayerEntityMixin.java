package bewis09.communicated.mixin;

import bewis09.communicated.util.PlayerEntityInvoker;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin implements PlayerEntityInvoker {
    @Override
    public void communicated$openPaper(@NotNull ItemStack book, @NotNull int slot) {

    }
}
