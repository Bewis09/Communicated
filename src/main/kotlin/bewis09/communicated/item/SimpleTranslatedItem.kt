package bewis09.communicated.item

import bewis09.communicated.item.interfaces.GeneratedTranslationItem
import net.minecraft.item.Item

class SimpleTranslatedItem(settings: Settings, private val en_us: String) : Item(settings), GeneratedTranslationItem {
    override fun getTitle(): String {
        return en_us
    }
}