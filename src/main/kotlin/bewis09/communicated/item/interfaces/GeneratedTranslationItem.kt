package bewis09.communicated.item.interfaces

import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider.TranslationBuilder
import net.minecraft.item.Item

interface GeneratedTranslationItem: TranslatedElement {
    override fun generateTranslationKeys(translationBuilder: TranslationBuilder) {
        translationBuilder.add(this as Item, getTitle())
    }

    fun getTitle(): String
}