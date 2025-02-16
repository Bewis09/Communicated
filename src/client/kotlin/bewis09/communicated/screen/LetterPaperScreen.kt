package bewis09.communicated.screen

import bewis09.communicated.drawable.LetterPaperEditBox
import bewis09.communicated.item.components.CommunicatedComponents
import bewis09.communicated.server.FinishLetterWritingPayload
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.Element
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.PageTurnWidget
import net.minecraft.client.render.RenderLayer
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.Identifier

class LetterPaperScreen(title: Text, itemStack: ItemStack, private val slot: Int): Screen(title) {
    private val editBoxWidget = LetterPaperEditBox(MinecraftClient.getInstance().textRenderer,0, 0, 140, 162, Text.of(""),Text.of(""))
    private val text: ArrayList<String> = arrayListOf(*itemStack.get(CommunicatedComponents.LETTER_PAPER_CONTENT)?.pages?.map { it }?.toTypedArray() ?: arrayOf())

    private var page = 0

    init {
        if(text.size - 1 < page)
            text.add("")
        editBoxWidget.text = text[page]
        editBoxWidget.setChangeListener {
            text[page] = it
        }
    }

    override fun getFocused(): Element {
        return editBoxWidget
    }

    override fun init() {
        editBoxWidget.x = this.width/2-70
        editBoxWidget.y = this.height/2-86

        this.addDrawableChild(editBoxWidget)

        this.addDrawableChild(PageTurnWidget(this.width/2-70,this.height/2+75, false, {
            page--
            page = 0.coerceAtLeast(page)
            editBoxWidget.text = text[page]
        }, true))
        this.addDrawableChild(PageTurnWidget(this.width/2+47,this.height/2+75, true, {
            page++
            page = 9.coerceAtMost(page)
            if(text.size - 1 < page)
                text.add("")
            editBoxWidget.text = text[page]
        }, true))
    }

    override fun renderBackground(context: DrawContext?, mouseX: Int, mouseY: Int, delta: Float) {
        this.renderDarkening(context)

        context?.drawTexture({ texture: Identifier? -> RenderLayer.getGuiTextured(texture) }, Identifier.of("communicated","textures/screen/letter_paper.png"),this.width/2-80,this.height/2-96,0f,0f,256,256,256,256)

        context?.drawText(MinecraftClient.getInstance().textRenderer,Text.translatable("book.pageIndicator",page+1,text.size),width/2-MinecraftClient.getInstance().textRenderer.getWidth(Text.translatable("book.pageIndicator",page+1,text.size))/2,height/2+79,0xFF000000.toInt(),false)
    }

    override fun close() {
        ClientPlayNetworking.send(FinishLetterWritingPayload(text.filterIndexed { i, v -> i == 0 || v != "" || text.subList(i, text.size).any { it != "" } },slot))

        super.close()
    }
}