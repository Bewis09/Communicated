package bewis09.communicated.screen

import bewis09.communicated.Communicated
import bewis09.communicated.server.EnvelopeClosingScreenOpenerPayloads
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.render.RenderLayer
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text
import net.minecraft.util.Identifier

class EnvelopeHandledScreen(handler: EnvelopeScreenHandler, playerInventory: PlayerInventory, title: Text): HandledScreen<EnvelopeScreenHandler>(handler,playerInventory,title) {
    companion object {
        val CLOSE = Communicated.translatedText("gui.close_envelope","Close Envelope")
    }

    init {
        backgroundHeight = 132
        playerInventoryTitleY = backgroundHeight - 94
    }

    override fun init() {
        super.init()

        addDrawableChild(ButtonWidget.builder(CLOSE){
            ClientPlayNetworking.send(EnvelopeClosingScreenOpenerPayloads.C2S(screenHandler.slot))
        }.dimensions(x+75,y+16,94,20).build())
    }

    override fun render(context: DrawContext?, mouseX: Int, mouseY: Int, delta: Float) {
        super.render(context, mouseX, mouseY, delta)
        this.drawMouseoverTooltip(context, mouseX, mouseY)
    }

    override fun drawBackground(context: DrawContext?, delta: Float, mouseX: Int, mouseY: Int) {
        context!!.drawTexture({ texture: Identifier? -> RenderLayer.getGuiTextured(texture) }, Identifier.of("communicated","textures/screen/envelope_inventory.png"), this.x, this.y, 0.0f, 0.0f, this.backgroundWidth, this.backgroundHeight, 256, 256)
    }
}