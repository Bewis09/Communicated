package bewis09.communicated.util;

import bewis09.communicated.item.components.LetterComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public interface PlayerEntityInvoker {
    void communicated$openPaper(ItemStack book, int slot);

    void communicated$openLetter(LetterComponent component, Text text);
}