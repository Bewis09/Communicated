package bewis09.communicated.recipe

import bewis09.communicated.item.CommunicatedItems
import net.minecraft.item.ItemStack
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.SpecialCraftingRecipe
import net.minecraft.recipe.book.CraftingRecipeCategory
import net.minecraft.recipe.input.CraftingRecipeInput
import net.minecraft.registry.RegistryWrapper
import net.minecraft.util.collection.DefaultedList
import net.minecraft.world.World

class CopyKeyRecipe(category: CraftingRecipeCategory?): SpecialCraftingRecipe(category) {
    override fun matches(input: CraftingRecipeInput?, world: World?): Boolean {
        if(input == null)
            return false

        if(input.stacks?.filter { it.item == CommunicatedItems.KEY }?.size != 1)
            return false

        if(input.stacks?.filter { it.item == CommunicatedItems.UNUSED_KEY }?.size != 1)
            return false

        return input.stacks?.none { !it.isEmpty && (it.item != CommunicatedItems.UNUSED_KEY && it.item != CommunicatedItems.KEY) } == true
    }

    override fun craft(input: CraftingRecipeInput?, registries: RegistryWrapper.WrapperLookup?): ItemStack {
        if(input == null)
            return ItemStack.EMPTY

        val key = input.stacks?.find { it.item == CommunicatedItems.KEY } ?: return ItemStack.EMPTY

        val keyCopy = key.copy()
        keyCopy.count = 1

        return keyCopy
    }

    override fun getRecipeRemainders(input: CraftingRecipeInput?): DefaultedList<ItemStack> {
        if (input == null)
            return DefaultedList.of()

        val list = DefaultedList.ofSize(input.size(), ItemStack.EMPTY)

        for (i in 0 until input.size()) {
            val stack = input.getStackInSlot(i)
            if (stack.item == CommunicatedItems.KEY) {
                val copy = stack.copy()
                copy.count = 1
                list[i] = copy
            }
        }

        return list
    }

    override fun getSerializer(): RecipeSerializer<out SpecialCraftingRecipe> {
        return CommunicatedRecipes.COPY_KEY
    }
}