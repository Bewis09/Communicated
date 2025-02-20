package bewis09.communicated.item

import bewis09.communicated.Communicated
import bewis09.communicated.item.components.CommunicatedComponents
import bewis09.communicated.item.components.LetterComponent
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

class LetterItem(settings: Settings): Item(settings), GeneratedTranslationItem, FlatModelItem {
    companion object {
        val LETTER_FOR = Communicated.translatedTextWithParams("letter_for", "for %s")
        val PAPER_COUNT = Communicated.translatedTextWithParams("letter_paper_count", "%s papers")
        val SINGLE_PAPER_COUNT = Communicated.translatedText("single_paper_count", "1 paper")
    }

    override fun use(world: World?, user: PlayerEntity?, hand: Hand?): ActionResult {
        if(user == null) return super.use(world, null, hand)

        val stack = if(hand==Hand.MAIN_HAND) user.inventory.mainHandStack else user.inventory.getStack(40)
        val content = stack.get(CommunicatedComponents.LETTER_CONTENT) ?: LetterComponent(stack.name.string, null, listOf(), null)

        (user as PlayerEntityInvoker).`communicated$openLetter`(content, stack.name)

        return ActionResult.SUCCESS
    }

    override fun getName(stack: ItemStack?): Text {
        val letterComponent = stack?.get(CommunicatedComponents.LETTER_CONTENT) ?: return super.getName(stack)

        return Text.of(letterComponent.title)
    }

    override fun appendTooltip(stack: ItemStack?, context: TooltipContext?, tooltip: MutableList<Text>?, type: TooltipType?) {
        val letterComponent = stack?.get(CommunicatedComponents.LETTER_CONTENT) ?: return

        if(letterComponent.author != null)
            tooltip?.add(Text.translatable("book.byAuthor", letterComponent.author).formatted(Formatting.GRAY))
        if(letterComponent.designated_to != null)
            tooltip?.add(LETTER_FOR(arrayOf(letterComponent.designated_to)).formatted(Formatting.GRAY))
        if(letterComponent.papers.size == 1)
            tooltip?.add(SINGLE_PAPER_COUNT.formatted(Formatting.GRAY))
        else
            tooltip?.add(PAPER_COUNT(arrayOf(letterComponent.papers.size)).formatted(Formatting.GRAY))
    }

    override fun getTitle(): String = "Letter"
}