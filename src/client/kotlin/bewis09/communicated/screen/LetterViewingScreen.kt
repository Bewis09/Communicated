package bewis09.communicated.screen

import bewis09.communicated.Communicated
import bewis09.communicated.item.LetterPaperItem.Companion.ONE_PAGE_TEXT
import bewis09.communicated.item.LetterPaperItem.Companion.PAGES_TEXT
import bewis09.communicated.item.components.LetterComponent
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.render.RenderLayer
import net.minecraft.text.Text
import net.minecraft.util.Identifier

class LetterViewingScreen(private val component: LetterComponent): Screen(Text.of(component.title)) {
    private val element_height = 30

    companion object {
        val NO_PAPER = Communicated.translatedText("letter.no_paper", "This letter contains no papers")
    }

    override fun render(context: DrawContext?, mouseX: Int, mouseY: Int, delta: Float) {
        super.render(context, mouseX, mouseY, delta)

        val total_height = (if (component.author == null) 20 else 29) + if(component.papers.isEmpty()) 9 else component.papers.size * element_height

        val total_width = textRenderer.getWidth(PAGES_TEXT.translate(arrayOf(10))).coerceAtLeast(component.papers.map { Text.of(it.title) }.maxOfOrNull { textRenderer.getWidth(it) } ?: 0).plus(30)
        val top = (height / 2.5).toInt() - total_height / 2

        val x = width / 2 - total_width / 2
        var y = top

        context?.drawCenteredTextWithShadow(textRenderer, title, width / 2, y, -1)
        if(component.author != null)
            context?.drawCenteredTextWithShadow(textRenderer, Text.translatable("book.byAuthor", component.author), width / 2, y + 9, 0xFFAAAAAA.toInt())

        for ((index, c) in component.papers.withIndex()) {
            y = top + (if (component.author == null) 20 else 29) + index * element_height

            if (mouseX in x - 5..<x+total_width+5 && mouseY in y+3..<y+element_height+3) {
                context?.fill(x - 5,y+3,x + total_width + 5,y + 3 + element_height,0xFF000000.toInt())
                context?.drawBorder(x - 5,y+3,total_width + 10,element_height+1,-1)
            }

            context?.drawTexture({ texture: Identifier? -> RenderLayer.getGuiTextured(texture) }, Identifier.of("communicated","textures/item/letter_paper.png"), x, y + 8, 0f, 0f, 20, 20, 20, 20)

            context?.drawTextWithShadow(textRenderer, c.title, x + 30, y + 10, -1)
            context?.drawTextWithShadow(textRenderer, (if(c.pages.size > 1) PAGES_TEXT.translate(arrayOf(c.pages.size)) else ONE_PAGE_TEXT), x + 30, y + 19, 0xFFAAAAAA.toInt())
        }

        if(component.papers.isEmpty())
            context?.drawCenteredTextWithShadow(textRenderer, NO_PAPER, width / 2, y + if (component.author == null) 20 else 29, 0xFFAAAAAA.toInt())
    }

    override fun renderBackground(context: DrawContext?, mouseX: Int, mouseY: Int, delta: Float) {
        renderInGameBackground(context)
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        val total_height = (if (component.author == null) 20 else 29) + if(component.papers.isEmpty()) 9 else component.papers.size * element_height

        val total_width = component.papers.map { Text.of(it.title) }.also { mutableListOf(*it.toTypedArray()).add(PAGES_TEXT.translate(arrayOf(10))) }.maxOfOrNull { textRenderer.getWidth(it) }?.plus(30) ?: 0
        val top = (height / 2.5).toInt() - total_height / 2

        val x = width / 2 - total_width / 2

        for ((index, c) in component.papers.withIndex()) {
            val y = top + (if (component.author == null) 20 else 29) + index * element_height

            if (mouseX.toInt() in x - 5..<x+total_width+5 && mouseY.toInt() in y+3..<y+element_height+3) {
                MinecraftClient.getInstance().setScreen(LetterPaperViewingScreen(c, this))
            }
        }

        return false
    }
}