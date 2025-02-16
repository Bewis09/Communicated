package bewis09.communicated.screen

import bewis09.communicated.Communicated
import bewis09.communicated.server.CloseEnvelopePayload
import bewis09.communicated.util.MixinChildClassIdentifier
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.gui.widget.TextFieldWidget
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.util.SelectionManager
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import kotlin.math.ceil

class EnvelopeClosingScreen(private val player_name: String, private val slot: Int) : Screen(EnvelopeHandledScreen.CLOSE) {
    private val background_width = 176
    private val background_height = 96

    private var proposed_title = ""

    private var delete_author_hovered = false
    private var author_shown = true

    private lateinit var designated_text_field: TextFieldWidget

    companion object {
        val DESIGNATED_TO_FIELD = Communicated.translatedText("designated_to_field", "Designated to: ")
        val UNSIGNED = Communicated.translatedText("unsigned_paper", "(unsigned)")
    }

    private val paperTitleSelectionManager = SelectionManager(
        { this.proposed_title },
        { title: String -> this.proposed_title = title },
        { this.getClipboard() },
        { clipboard: String -> this.setClipboard(clipboard) },
        { string: String -> string.length < 25 }
    )

    override fun init() {
        val x = width / 2 - background_width / 2
        val y = height / 2 - background_height / 2

        if(!this::designated_text_field.isInitialized)
            designated_text_field = object: TextFieldWidget(textRenderer, background_width - 12, 0, background_width - 12, 12, Text.empty()), MixinChildClassIdentifier {
                override fun `communicated$getClassName`(): String {
                    return "envelope_title_widget"
                }
            }

        designated_text_field.y = y + 54
        designated_text_field.x = x + 6
        designated_text_field.setDrawsBackground(false)
        designated_text_field.setMaxLength(16)

        addDrawableChild(designated_text_field)

        addDrawableChild(ButtonWidget.builder(title) {
            if(proposed_title.isEmpty()) return@builder

            ClientPlayNetworking.send(CloseEnvelopePayload(
                proposed_title,
                designated_text_field.text,
                author_shown,
                slot
            ))

            this.close()
        }.dimensions(x+6, y + background_height - 26, background_width - 12, 20).build())
    }

    override fun charTyped(chr: Char, modifiers: Int): Boolean {
        if (!designated_text_field.isFocused)
            return paperTitleSelectionManager.insert(chr)
        return super.charTyped(chr, modifiers)
    }

    private fun setClipboard(clipboard: String) {
        if (this.client != null) {
            SelectionManager.setClipboard(this.client, clipboard)
        }
    }

    private fun getClipboard(): String {
        return if (this.client != null) SelectionManager.getClipboard(this.client) else ""
    }

    override fun render(context: DrawContext?, mouseX: Int, mouseY: Int, delta: Float) {
        context?.matrices?.translate(0f, 0f, 10f)
        super.render(context, mouseX, mouseY, delta)
        context?.matrices?.translate(0f, 0f, -10f)

        val x = width / 2 - background_width / 2
        val y = height / 2 - background_height / 2

        context?.drawTexture({ texture: Identifier? -> RenderLayer.getGuiTextured(texture) }, Identifier.of("communicated","textures/screen/envelope_closing.png"), x, y, 0f, 0f, 256, 256, 256, 256)

        context?.drawText(textRenderer, proposed_title, this.width / 2 - ceil(textRenderer.getWidth(proposed_title) / 2.0).toInt(), y + 7, 0xFF444444.toInt(), false)
        if(!designated_text_field.isFocused)
            context?.drawHorizontalLine(this.width / 2 + textRenderer.getWidth(proposed_title) / 2, this.width / 2 + ceil(textRenderer.getWidth(proposed_title) / 2.0).toInt() + 4, y + 14, if(System.currentTimeMillis() % 1200 < 600) 0x77000000 else 0xDD000000.toInt())

        val text = if (author_shown) Text.translatable("book.byAuthor", player_name) else UNSIGNED
        context?.drawText(textRenderer, text, this.width / 2 - ceil(textRenderer.getWidth(text) / 2.0).toInt(), y + 18, 0xFF888888.toInt(), false)

        delete_author_hovered = mouseX in x + 6..x + background_width - 6 && mouseY in y + 18..y + 25 && author_shown

        if (delete_author_hovered) {
            context?.drawHorizontalLine(
                this.width / 2 - ceil(textRenderer.getWidth(Text.translatable("book.byAuthor", player_name)) / 2.0).toInt() - 3,
                this.width / 2 + ceil(textRenderer.getWidth(Text.translatable("book.byAuthor", player_name)) / 2.0).toInt(),
                y + 21,
                0xFF444444.toInt()
            )
        }

        context?.drawText(textRenderer, DESIGNATED_TO_FIELD, x + 6, y + 44, 0xFF666666.toInt(), false)
    }

    override fun renderBackground(context: DrawContext?, mouseX: Int, mouseY: Int, delta: Float) {
        context?.matrices?.translate(0f, 0f, -10f)
        this.renderInGameBackground(context)
        context?.matrices?.translate(0f, 0f, 10f)
    }

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        if (!designated_text_field.isFocused) {
            when (keyCode) {
                259 -> {
                    paperTitleSelectionManager.delete(-1)
                    return true
                }
            }
        }

        return super.keyPressed(keyCode, scanCode, modifiers)
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        this.focused = null

        if(delete_author_hovered)
            author_shown = false

        return super.mouseClicked(mouseX, mouseY, button)
    }
}