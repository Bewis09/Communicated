package bewis09.communicated.screen

import bewis09.communicated.drawable.LetterPaperEditBox
import bewis09.communicated.item.components.LetterComponent
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.PageTurnWidget
import net.minecraft.client.render.RenderLayer
import net.minecraft.text.Text
import net.minecraft.util.Identifier

class LetterPaperViewingScreen(component: LetterComponent.PaperComponentPart, val parent: Screen): Screen(Text.empty()) {
    private val editBoxWidget = LetterPaperEditBox(MinecraftClient.getInstance().textRenderer,0, 0, 140, 162, Text.of(""),Text.of(""))
    private val text: List<String> = component.pages.ifEmpty { listOf("") }

    private var page = 0

    override fun init() {
        this.addDrawableChild(PageTurnWidget(this.width/2-70,this.height/2+75, false, {
            page--
            page = 0.coerceAtLeast(page)
            editBoxWidget.text = text[page]
        }, true))
        this.addDrawableChild(PageTurnWidget(this.width/2+47,this.height/2+75, true, {
            page++
            page = (text.size - 1).coerceAtMost(page)
            editBoxWidget.text = text[page]
        }, true))
    }

    override fun renderBackground(context: DrawContext?, mouseX: Int, mouseY: Int, delta: Float) {
        this.renderInGameBackground(context)

        context?.drawTexture({ texture: Identifier? -> RenderLayer.getGuiTextured(texture) }, Identifier.of("communicated","textures/screen/letter_paper.png"),this.width/2-80,this.height/2-96,0f,0f,256,256,256,256)

        context?.drawText(MinecraftClient.getInstance().textRenderer,Text.translatable("book.pageIndicator",page+1,text.size),width/2-MinecraftClient.getInstance().textRenderer.getWidth(Text.translatable("book.pageIndicator",page+1,text.size))/2,height/2+79,0xFF000000.toInt(),false)
    }

    override fun render(context: DrawContext?, mouseX: Int, mouseY: Int, delta: Float) {
        super.render(context, mouseX, mouseY, delta)

        val texts = textRenderer.wrapLines(Text.of(text[page]), 132)

        texts.forEachIndexed { i, t ->
            context?.drawText(textRenderer, t, this.width/2 - 66, this.height/2 - 82 + i * 9, -0x1000000, false)
        }
    }

    override fun close() {
        MinecraftClient.getInstance().setScreen(parent)
    }
}