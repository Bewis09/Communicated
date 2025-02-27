package bewis09.communicated.item

import bewis09.communicated.Communicated
import bewis09.communicated.item.interfaces.GeneratedTranslationItem
import bewis09.communicated.screen.EnvelopeScreenHandler
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.Item
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.screen.ScreenHandler
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.world.World

class EnvelopeItem(settings: Settings): Item(settings), GeneratedTranslationItem {
    private val TITLE = Communicated.translatedText("gui.envelope_screen","Envelope")

    override fun getTitle(): String = "Envelope"

    override fun use(world: World?, user: PlayerEntity?, hand: Hand?): ActionResult {
        user?.openHandledScreen(object: NamedScreenHandlerFactory {
            override fun getDisplayName(): Text {
                return TITLE
            }

            override fun createMenu(syncId: Int, playerInventory: PlayerInventory?, player: PlayerEntity?): ScreenHandler {
                return EnvelopeScreenHandler(syncId, playerInventory!!)
            }
        })

        return ActionResult.SUCCESS
    }
}