package bewis09.communicated.item

import bewis09.communicated.Communicated
import bewis09.communicated.item.components.CommunicatedComponents
import bewis09.communicated.item.components.LetterComponent
import bewis09.communicated.item.interfaces.GeneratedTranslationItem
import bewis09.communicated.server.LetterOpeningPayload
import bewis09.communicated.util.EncryptionUtil
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.network.packet.s2c.play.OverlayMessageS2CPacket
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.Formatting
import net.minecraft.util.Hand
import net.minecraft.world.World

class LetterItem(settings: Settings): Item(settings), GeneratedTranslationItem {
    companion object {
        val NOT_FOR_YOU = Communicated.translatedText("letter.not_for_you", "You are not allowed to open this letter")
        val LETTER_FOR = Communicated.translatedTextWithParams("letter_for", "for %s")
        val PAPER_COUNT = Communicated.translatedTextWithParams("letter_paper_count", "%s papers")
        val SINGLE_PAPER_COUNT = Communicated.translatedText("single_paper_count", "1 paper")
    }

    override fun use(world: World?, user: PlayerEntity?, hand: Hand?): ActionResult {
        if(user == null) return super.use(world, null, hand)

        if(world == null || world.isClient() && user !is ServerPlayerEntity) return ActionResult.SUCCESS

        val stack = if(hand==Hand.MAIN_HAND) user.mainHandStack else user.offHandStack
        val data = stack.get(CommunicatedComponents.LETTER_CONTENT) ?: LetterComponent(stack.name.string, null, listOf(), null)

        if(data.designated_to == null || user.gameProfile?.name == data.designated_to) {
            ServerPlayNetworking.send(user as ServerPlayerEntity, LetterOpeningPayload(LetterComponent(data.title,data.author,data.papers.map {
                return@map LetterComponent.PaperComponentPart(it.pages.map { a ->
                    EncryptionUtil.decrypt(a, Communicated.getEncryptionKey(world.server!!))
                },it.title)
            },null)))
        } else {
            (user as ServerPlayerEntity).networkHandler.sendPacket(OverlayMessageS2CPacket(NOT_FOR_YOU))
        }

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