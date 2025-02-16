package bewis09.communicated.item.interfaces

import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider.TranslationBuilder

interface TranslatedElement {
    fun generateTranslationKeys(translationBuilder: TranslationBuilder)
}