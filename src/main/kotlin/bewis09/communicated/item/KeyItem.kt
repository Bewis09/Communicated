package bewis09.communicated.item

import bewis09.communicated.item.components.CommunicatedComponents
import bewis09.communicated.item.interfaces.GeneratedTranslationItem
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import kotlin.math.abs

class KeyItem(settings: Settings): Item(settings), GeneratedTranslationItem {
    override fun appendTooltip(stack: ItemStack?, context: TooltipContext?, tooltip: MutableList<Text>?, type: TooltipType?) {
        val uuid = stack?.get(CommunicatedComponents.KEY) ?: return

        tooltip?.add(Text.literal(abs(uuid.leastSignificantBits).toString(16)).formatted(Formatting.GRAY))
    }

    override fun getTitle(): String {
        return "Key"
    }
}