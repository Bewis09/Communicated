package bewis09.communicated.item

import bewis09.communicated.Communicated
import net.minecraft.block.Block
import net.minecraft.component.DataComponentTypes
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registries
import net.minecraft.text.Text

class MailboxBlockItem(block: Block, settings: Settings, locked_title: String): BlockItem(block, settings) {
    private val LOCKED_TITLE: Text = Communicated.translatedText(Registries.BLOCK.getId(block).path + ".locked", locked_title)

    override fun getName(stack: ItemStack?): Text {
        val blockEntityData = stack?.get(DataComponentTypes.BLOCK_ENTITY_DATA) ?: return super.getName(stack)
        val containsTitle = blockEntityData.contains("Key")

        return if(containsTitle) LOCKED_TITLE else super.getName(stack)
    }
}