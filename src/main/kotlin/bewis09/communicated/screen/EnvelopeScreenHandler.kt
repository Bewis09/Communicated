package bewis09.communicated.screen

import bewis09.communicated.item.CommunicatedItems
import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.ContainerComponent
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerListener
import net.minecraft.screen.slot.Slot
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.collection.DefaultedList

class EnvelopeScreenHandler(syncId: Int, val playerInventory: PlayerInventory, val slot: Int): ScreenHandler(CommunicatedScreenHandlers.ENVELOPE_SCREEN_HANDLER, syncId) {
    private val defaultedList = DefaultedList.ofSize(3, ItemStack.EMPTY)
    private val inventory: SimpleInventory
    val itemStack: ItemStack = playerInventory.getStack(slot)

    constructor(syncId: Int, playerInventory: PlayerInventory): this(syncId, playerInventory, if(playerInventory.mainHandStack.item == CommunicatedItems.ENVELOPE) playerInventory.selectedSlot else 40)

    init {
        itemStack.get(DataComponentTypes.CONTAINER)?.copyTo(defaultedList)

        inventory = SimpleInventory(*defaultedList.toTypedArray())
        inventory.addListener {
            itemStack.set(DataComponentTypes.CONTAINER,ContainerComponent.fromStacks(inventory.heldStacks))
        }

        addListener(object: ScreenHandlerListener {
            override fun onSlotUpdate(handler: ScreenHandler?, slotId: Int, stack: ItemStack?) {
                val player = playerInventory.player
                if(player is ServerPlayerEntity && playerInventory.getStack(slot) != itemStack) (player).closeHandledScreen()
            }

            override fun onPropertyUpdate(handler: ScreenHandler?, property: Int, value: Int) {
                val player = playerInventory.player
                if(player is ServerPlayerEntity && playerInventory.getStack(slot) != itemStack) (player).closeHandledScreen()
            }
        })

        for (j in 0..<3) {
            this.addSlot(object: Slot(inventory,j,j*22+8,18) {
                override fun canInsert(stack: ItemStack?): Boolean {
                    return stack?.item == CommunicatedItems.LETTER_PAPER
                }
            })
        }

        for (i in 0..<3) {
            for (j in 0..<9) {
                this.addSlot(Slot(playerInventory,i*9+j+9,j*18+8,i*18+50))
            }
        }

        for (j in 0..<9) {
            this.addSlot(Slot(playerInventory,j,j*18+8,108))
        }
    }

    override fun quickMove(player: PlayerEntity?, slot: Int): ItemStack {
        var itemStack = ItemStack.EMPTY
        val slot2 = slots[slot]
        if (slot2.hasStack()) {
            val itemStack2 = slot2.stack
            itemStack = itemStack2.copy()
            if (slot < 3) {
                if (!this.insertItem(itemStack2, 3, slots.size, true)) {
                    return ItemStack.EMPTY
                }
            } else if (!this.insertItem(itemStack2, 0, 3, false)) {
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
        return true
    }
}