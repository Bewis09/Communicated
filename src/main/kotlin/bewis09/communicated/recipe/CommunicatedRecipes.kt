package bewis09.communicated.recipe

import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.SpecialCraftingRecipe.SpecialRecipeSerializer

object CommunicatedRecipes {
    val LOCK_MAILBOX: RecipeSerializer<LockMailboxRecipe> = RecipeSerializer.register("crafting_special_lockmailbox", SpecialRecipeSerializer { LockMailboxRecipe(it) })
    val COPY_KEY: RecipeSerializer<CopyKeyRecipe> = RecipeSerializer.register("crafting_special_copy_key", SpecialRecipeSerializer { CopyKeyRecipe(it) })
}