package bewis09.communicated.block.entity

import bewis09.communicated.item.CommunicatedItems
import bewis09.communicated.screen.CommunicatedScreenHandlers
import bewis09.communicated.screen.MailboxScreenHandler
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventories
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper.WrapperLookup
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.screen.ScreenHandler
import net.minecraft.text.Text
import net.minecraft.util.Nameable
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.math.BlockPos

class MailboxBlockEntity(pos: BlockPos?, state: BlockState?): BlockEntity(CommunicatedBlockEntities.MAILBOX_BLOCK_ENTITY, pos, state), NamedScreenHandlerFactory, Inventory, Nameable {
    companion object {
        fun isValidMailboxContent(stack: ItemStack): Boolean {
            return stack.item == CommunicatedItems.LETTER || stack.item == CommunicatedItems.ENVELOPE || stack.item == CommunicatedItems.LETTER_PAPER
        }
    }

    private var items: DefaultedList<ItemStack> = DefaultedList.ofSize(this.size(), ItemStack.EMPTY)

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity?): ScreenHandler {
        return MailboxScreenHandler(CommunicatedScreenHandlers.MAILBOX_SCREEN_HANDLER, syncId, this, playerInventory)
    }

    override fun getName(): Text {
        return if (this.customName != null) this.customName!! else world!!.getBlockState(pos).block.name
    }

    override fun getDisplayName(): Text {
        return this.name
    }

    override fun writeNbt(nbt: NbtCompound?, registries: WrapperLookup?) {
        super.writeNbt(nbt, registries)
        Inventories.writeNbt(nbt, items, registries)
    }

    override fun readNbt(nbt: NbtCompound, registries: WrapperLookup?) {
        super.readNbt(nbt, registries)
        items = DefaultedList.ofSize(this.size(), ItemStack.EMPTY)
        Inventories.readNbt(nbt, items, registries)
    }

    override fun clear() {
        items.fill(ItemStack.EMPTY)
        markDirty()
    }

    override fun size(): Int {
        return 15
    }

    override fun isEmpty(): Boolean {
        return items.none { !it.isEmpty }
    }

    override fun getStack(slot: Int): ItemStack {
        return items[slot]
    }

    override fun removeStack(slot: Int, amount: Int): ItemStack {
        val itemStack = Inventories.splitStack(items, slot, amount)
        if (!itemStack.isEmpty) {
            this.markDirty()
        }

        return itemStack
    }

    override fun removeStack(slot: Int): ItemStack {
        return Inventories.removeStack(items, slot)
    }

    override fun setStack(slot: Int, stack: ItemStack) {
        items[slot] = stack
        this.markDirty()
    }

    override fun canPlayerUse(player: PlayerEntity): Boolean {
        return true
    }

    override fun isValid(slot: Int, stack: ItemStack): Boolean {
        return isValidMailboxContent(stack)
    }

    fun addStack(stack: ItemStack): ItemStack {
        if(!isValidMailboxContent(stack))
            return stack

        for (i in 0 until this.size()) {
            val stack3 = this.getStack(i)

            if (ItemStack.areItemsAndComponentsEqual(stack, stack3) && stack3.count > 0) {
                stack3.count += stack.count

                if (stack3.count > stack3.maxCount) {
                    stack.count = stack3.count - stack3.maxCount
                    stack3.count = stack3.maxCount
                } else {
                    stack.count = 0
                    return ItemStack.EMPTY
                }
            }
        }

        for (i in 0 until this.size()) {
            if (this.getStack(i).isEmpty) {
                this.setStack(i, stack)
                return ItemStack.EMPTY
            }
        }

        return stack
    }

    fun getComparatorOutput(): Int {
        return 15 - items.sumOf { it.isEmpty.compareTo(false) }
    }
}