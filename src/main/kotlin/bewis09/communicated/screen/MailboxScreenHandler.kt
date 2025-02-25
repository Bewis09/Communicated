package bewis09.communicated.screen

import bewis09.communicated.block.entity.MailboxBlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.screen.slot.Slot

class MailboxScreenHandler(type: ScreenHandlerType<*>, syncId: Int, private val inventory: Inventory, playerInventory: PlayerInventory): ScreenHandler(type, syncId) {
    constructor(syncId: Int, playerInventory: PlayerInventory): this(CommunicatedScreenHandlers.MAILBOX_SCREEN_HANDLER, syncId, SimpleInventory(15), playerInventory)

    init {
        checkSize(inventory, 15)
        inventory.onOpen(playerInventory.player)

        for (i in 0 until 3) {
            for (j in 0 until 5) {
                addSlot(object: Slot(inventory, j + i * 5, 44 + j * 18, 17 + i * 18) {
                    override fun canInsert(stack: ItemStack?): Boolean {
                        return MailboxBlockEntity.isValidMailboxContent(stack!!)
                    }
                })
            }
        }

        for (i in 0 until 3) {
            for (j in 0 until 9) {
                addSlot(Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18))
            }
        }

        for (i in 0 until 9) {
            addSlot(Slot(playerInventory, i, 8 + i * 18, 142))
        }
    }

    override fun quickMove(player: PlayerEntity?, slot: Int): ItemStack {
        var itemStack = ItemStack.EMPTY
        val slot2 = slots[slot]
        if (slot2.hasStack()) {
            val itemStack2 = slot2.stack
            itemStack = itemStack2.copy()
            if (slot < 15) {
                if (!this.insertItem(itemStack2, 15, slots.size, true)) {
                    return ItemStack.EMPTY
                }
            } else if (!this.insertItem(itemStack2, 0, 15, false)) {
                return ItemStack.EMPTY
            }

            if (itemStack2.isEmpty) {
                slot2.stack = ItemStack.EMPTY
            } else {
                slot2.markDirty()
            }
        }

        return itemStack
    }

    override fun canUse(player: PlayerEntity?): Boolean {
        return this.inventory.canPlayerUse(player)
    }
}