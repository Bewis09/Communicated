package bewis09.communicated

import bewis09.communicated.item.CommunicatedItems
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.minecraft.client.data.BlockStateModelGenerator
import net.minecraft.client.data.ItemModelGenerator
import net.minecraft.client.data.Models
import net.minecraft.data.recipe.RecipeExporter
import net.minecraft.data.recipe.RecipeGenerator
import net.minecraft.data.recipe.RecipeGenerator.hasItem
import net.minecraft.item.ItemConvertible
import net.minecraft.item.Items
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.book.RecipeCategory
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.RegistryWrapper.WrapperLookup
import net.minecraft.util.Identifier
import java.util.concurrent.CompletableFuture


object CommunicatedDataGenerator : DataGeneratorEntrypoint {
	private val recipes = arrayListOf<GeneratedCraftingRecipe>()

	private fun loadRecipes() {
		addRecipe(ShapelessGeneratedCraftingRecipe(
			RecipeCategory.TOOLS,
			CommunicatedItems.ENVELOPE,
			1,
			"letter",
			Ingredient.ofItem(Items.PAPER),
			Ingredient.ofItem(Items.PAPER)
		))
	}

	override fun onInitializeDataGenerator(fabricDataGenerator: FabricDataGenerator) {
		loadRecipes()

		val pack = fabricDataGenerator.createPack()
		pack.addProvider { a, b -> CommunicatedRecipeProvider(a, b) }
		pack.addProvider { a, b -> CommunicatedEnglishLangProvider(a, b) }
		pack.addProvider { a, _ -> CommunicatedModelProvider(a) }
	}

	class CommunicatedModelProvider(output: FabricDataOutput) : FabricModelProvider(output) {
		override fun generateBlockStateModels(blockStateModelGenerator: BlockStateModelGenerator) {
		}

		override fun generateItemModels(itemModelGenerator: ItemModelGenerator) {
			CommunicatedItems.flatModelItems.forEach {
				itemModelGenerator.register(it, Models.GENERATED)
			}
		}

		override fun getName(): String {
			return "CommunicatedModelProvider"
		}
	}

	class CommunicatedEnglishLangProvider(dataOutput: FabricDataOutput?, registryLookup: CompletableFuture<WrapperLookup?>?) : FabricLanguageProvider(dataOutput, "en_us", registryLookup) {
		override fun generateTranslations(wrapperLookup: WrapperLookup, translationBuilder: TranslationBuilder) {
			Communicated.translations.forEach {
				translationBuilder.add(it.key,it.value)
			}
		}
	}

	class CommunicatedRecipeProvider(output: FabricDataOutput?, registriesFuture: CompletableFuture<WrapperLookup?>?) : FabricRecipeProvider(output, registriesFuture) {
		override fun getRecipeGenerator(registryLookup: WrapperLookup, exporter: RecipeExporter): RecipeGenerator {
			return object : RecipeGenerator(registryLookup, exporter) {
				override fun generate() {
					generateRecipes(this, exporter)
				}
			}
		}

		override fun getName(): String {
			return "CommunicatedRecipeProvider"
		}

		override fun getRecipeIdentifier(identifier: Identifier?): Identifier {
			return super.getRecipeIdentifier(identifier)
		}
	}

	class ShapedGeneratedCraftingRecipe(
		private val category: RecipeCategory,
		private val outputItem: ItemConvertible,
		private val outputCount: Int,
		private val id: String,
		private val pattern: Pattern
	): GeneratedCraftingRecipe {
		override fun generate(recipeGenerator: RecipeGenerator, exporter: RecipeExporter) {
			val r = recipeGenerator.createShaped(category, outputItem, outputCount)

			val hashMap = hashMapOf<Ingredient, Char>()
			var char = 'A'

			for (p in pattern.patternLine) {
				var s = ""

				p.ingredients.forEach {
					if(it == null) {
						s += ""
						return@forEach
					}

					if (!hashMap.contains(it)) hashMap[it] = char++

					s += hashMap[it]
				}

				r.pattern(s)
			}

			for (c in hashMap.entries) {
				r.input(c.value, c.key)

				@Suppress("DEPRECATION")
				c.key.matchingItems.forEach {
					r.criterion(hasItem(it.value()), recipeGenerator.conditionsFromItem(it.value()))
				}
			}

			r.offerTo(exporter, RegistryKey.of(RegistryKeys.RECIPE, Identifier.of("communicated",id)))
		}

		class Pattern(vararg val patternLine: PatternLine) {
			class PatternLine {
				val ingredients: Array<Ingredient?> = arrayOf(null, null, null)

				constructor(ingredient: Ingredient) {
					this.ingredients[0] = ingredient
				}

				constructor(ingredient1: Ingredient, ingredient2: Ingredient) {
					this.ingredients[0] = ingredient1
					this.ingredients[1] = ingredient2
				}

				constructor(ingredient1: Ingredient, ingredient2: Ingredient, ingredient3: Ingredient) {
					this.ingredients[0] = ingredient1
					this.ingredients[1] = ingredient2
					this.ingredients[2] = ingredient3
				}
			}
		}
	}

	class ShapelessGeneratedCraftingRecipe(
		private val category: RecipeCategory,
		private val outputItem: ItemConvertible,
		private val outputCount: Int,
		private val id: String,
		private vararg val items: Ingredient
	): GeneratedCraftingRecipe {
		override fun generate(recipeGenerator: RecipeGenerator, exporter: RecipeExporter) {
			val r = recipeGenerator.createShapeless(category, outputItem, outputCount)

			for (p in items) {
				r.input(p)

				@Suppress("DEPRECATION")
				p.matchingItems.forEach {
					r.criterion(hasItem(it.value()), recipeGenerator.conditionsFromItem(it.value()))
				}
			}

			r.offerTo(exporter, RegistryKey.of(RegistryKeys.RECIPE, Identifier.of("communicated",id)))
		}
	}

	interface GeneratedCraftingRecipe {
		fun generate(recipeGenerator: RecipeGenerator, exporter: RecipeExporter)
	}

	private fun addRecipe(recipe: GeneratedCraftingRecipe) {
		if(recipes.contains(recipe))
			return

		recipes.add(recipe)
	}

	private fun generateRecipes(recipeGenerator: RecipeGenerator, exporter: RecipeExporter) {
		recipes.forEach { it.generate(recipeGenerator, exporter) }
	}
}