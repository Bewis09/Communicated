package bewis09.communicated.util;

import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Hand;

public interface PlayerEntityInvoker {
    void communicated$openPaper(ItemStack book, int slot);
}