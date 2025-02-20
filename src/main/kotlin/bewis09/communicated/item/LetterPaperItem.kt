package bewis09.communicated.item

import bewis09.communicated.Communicated
import bewis09.communicated.item.components.CommunicatedComponents
import bewis09.communicated.item.interfaces.FlatModelItem
import bewis09.communicated.item.interfaces.GeneratedTranslationItem
import bewis09.communicated.util.PlayerEntityInvoker
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.Formatting
import net.minecraft.util.Hand
import net.minecraft.world.World

class LetterPaperItem(settings: Settings): Item(settings), GeneratedTranslationItem, FlatModelItem {
    companion object {
        val PAGES_TEXT = Communicated.translatedTextWithParams("letter_paper_pages_tooltip","%s Pages")
        val ONE_PAGE_TEXT = Communicated.translatedText("letter_paper_one_page_tooltip","1 Page")
    }

    override fun use(world: World?, user: PlayerEntity?, hand: Hand?): ActionResult {
        (user as PlayerEntityInvoker).`communicated$openPaper`(user.getStackInHand(hand), if(hand == Hand.OFF_HAND) 40 else user.inventory.selectedSlot)

        return ActionResult.SUCCESS
    }

    override fun appendTooltip(stack: ItemStack?, context: TooltipContext?, tooltip: MutableList<Text>?, type: TooltipType?) {
        val a = stack?.get(CommunicatedComponents.LETTER_PAPER_CONTENT)
        if(a != null)
            tooltip?.add((if(a.pages.size > 1) PAGES_TEXT(arrayOf(a.pages.size)) else ONE_PAGE_TEXT).formatted(Formatting.GRAY))
        super.appendTooltip(stack, context, tooltip, type)
    }

    override fun getTitle(): String = "Letter Paper"
}