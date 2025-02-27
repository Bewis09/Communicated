package bewis09.communicated.recipe

import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.SpecialCraftingRecipe.SpecialRecipeSerializer
import net.minecraft.recipe.book.CraftingRecipeCategory

object CommunicatedRecipes {
    val LOCK_MAILBOX: RecipeSerializer<LockMailboxRecipe> = RecipeSerializer.register("crafting_special_lockmailbox", SpecialRecipeSerializer { craftingRecipeCategory: CraftingRecipeCategory? -> LockMailboxRecipe(craftingRecipeCategory) })
}