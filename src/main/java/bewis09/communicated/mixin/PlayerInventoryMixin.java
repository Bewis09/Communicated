package bewis09.communicated.mixin;

import bewis09.communicated.util.PlayerInventoryInvokerInterface;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin implements PlayerInventoryInvokerInterface {
    @Shadow @Final private List<DefaultedList<ItemStack>> combinedInventory;

    @Override
    public int communicated$getStackSlot(ItemStack itemStack) {
        int i = 0;

        for (List<ItemStack> list : this.combinedInventory) {
            for (ItemStack stack : list) {
                if (!stack.isEmpty() && ItemStack.areItemsAndComponentsEqual(itemStack, stack)) {
                    return i;
                }
                i++;
            }
        }

        return -1;
    }
}
