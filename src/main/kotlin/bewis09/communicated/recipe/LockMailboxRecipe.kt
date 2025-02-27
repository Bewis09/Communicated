package bewis09.communicated.recipe

import bewis09.communicated.block.MailboxBlock
import bewis09.communicated.block.entity.CommunicatedBlockEntities
import bewis09.communicated.item.CommunicatedItems
import bewis09.communicated.item.components.CommunicatedComponents
import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.NbtComponent
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.SpecialCraftingRecipe
import net.minecraft.recipe.book.CraftingRecipeCategory
import net.minecraft.recipe.input.CraftingRecipeInput
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryWrapper
import net.minecraft.util.collection.DefaultedList
import net.minecraft.world.World
import java.util.*

class LockMailboxRecipe(category: CraftingRecipeCategory?) : SpecialCraftingRecipe(category) {
    var uuid: UUID = UUID.randomUUID()

    override fun matches(input: CraftingRecipeInput?, world: World?): Boolean {
        if (input?.stackCount != 2)
            return false

        var boxStack: Int = -1
        var keyStack: Int = -1

        for (i in 0 until input.size()) {
            val stack = input.stacks[i]
            val item = stack.item

            if (item is BlockItem) {
                if(item.block is MailboxBlock) {
                    if (boxStack >= 0)
                        return false
                    boxStack = i
                }
            }

            if(item == CommunicatedItems.KEY) {
                if (keyStack >= 0)
                    return false
                keyStack = i
            }
        }

        return boxStack != -1 || keyStack != -1
    }

    override fun craft(input: CraftingRecipeInput?, registries: RegistryWrapper.WrapperLookup?): ItemStack {
        if (input?.stackCount != 2)
            return ItemStack.EMPTY

        uuid = UUID.randomUUID()

        var boxStack: Int = -1
        var keyStack: Int = -1

        for (i in 0 until input.size()) {
            val stack = input.stacks[i]
            val item = stack.item

            if (item is BlockItem) {
                if(item.block is MailboxBlock) {
                    if (boxStack >= 0)
                        return ItemStack.EMPTY
                    boxStack = i
                }
            }

            if(item == CommunicatedItems.KEY) {
                if (keyStack >= 0)
                    return ItemStack.EMPTY
                keyStack = i
            }
        }

        if (boxStack == -1 || keyStack == -1)
            return ItemStack.EMPTY

        val stack = input.stacks[boxStack].copy()
        val nbt = stack.get(DataComponentTypes.BLOCK_ENTITY_DATA)?.copyNbt() ?: NbtCompound()
        nbt.putUuid("Key", uuid)

        if(!nbt.contains("id"))
            nbt.putString("id", Registries.BLOCK_ENTITY_TYPE.getId(CommunicatedBlockEntities.MAILBOX_BLOCK_ENTITY).toString())

        stack.set(DataComponentTypes.BLOCK_ENTITY_DATA, NbtComponent.of(nbt))

        return stack
    }

    override fun getRecipeRemainders(input: CraftingRecipeInput): DefaultedList<ItemStack> {
        val list = DefaultedList.ofSize(input.size(), ItemStack.EMPTY)

        for (i in 0 until input.size()) {
            val stack = input.stacks[i]
            if (stack.item == CommunicatedItems.KEY) {
                list[i] = stack.copy()
                list[i].set(CommunicatedComponents.KEY, uuid)
            }
        }

        return list
    }

    override fun getSerializer(): RecipeSerializer<out SpecialCraftingRecipe> {
        return CommunicatedRecipes.LOCK_MAILBOX
    }
}