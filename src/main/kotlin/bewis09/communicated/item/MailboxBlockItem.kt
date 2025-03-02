package bewis09.communicated.item

import bewis09.communicated.Communicated
import net.minecraft.block.Block
import net.minecraft.component.DataComponentTypes
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemStack
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import kotlin.math.absoluteValue

class MailboxBlockItem(block: Block, settings: Settings): BlockItem(block, settings) {
    companion object {
        val LOCKED = Communicated.translatedText("locked", "(Locked)")
    }

    override fun getName(stack: ItemStack?): Text {
        val blockEntityData = stack?.get(DataComponentTypes.BLOCK_ENTITY_DATA) ?: return super.getName(stack)
        val containsTitle = blockEntityData.contains("Key")

        return if(containsTitle) super.getName(stack).copy().append(" ").append(LOCKED) else super.getName(stack)
    }

    override fun appendTooltip(stack: ItemStack?, context: TooltipContext?, tooltip: MutableList<Text>?, type: TooltipType?) {
        val blockEntityData = stack?.get(DataComponentTypes.BLOCK_ENTITY_DATA) ?: return
        val key = blockEntityData.copyNbt().getUuid("Key")

        if(key != null) {
            tooltip?.add(Text.literal(key.leastSignificantBits.absoluteValue.toString(16)).formatted(Formatting.GRAY))
        }
    }
}