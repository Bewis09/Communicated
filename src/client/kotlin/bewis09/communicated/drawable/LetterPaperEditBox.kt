package bewis09.communicated.drawable

import bewis09.communicated.util.MixinChildClassIdentifier
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.widget.EditBoxWidget
import net.minecraft.text.Text

class LetterPaperEditBox(textRenderer: TextRenderer?, x: Int, y: Int, width: Int, height: Int, placeholder: Text?, message: Text?): EditBoxWidget(textRenderer,x,y,width,height, placeholder, message), MixinChildClassIdentifier {
    override fun renderWidget(context: DrawContext?, mouseX: Int, mouseY: Int, delta: Float) {
        while(maxScrollY > 0) {
            text = text.substring(0,text.length-1)
        }

        if (this.visible) {
            context!!.enableScissor(this.x + 1, this.y + 1, this.x + this.width - 1, this.y + this.height - 1)
            context.matrices.push()
            context.matrices.translate(0.0, -this.scrollY, 0.0)
            this.renderContents(context, mouseX, mouseY, delta)
            context.matrices.pop()
            context.disableScissor()
            this.renderOverlay(context)
        }
    }

    override fun isFocused(): Boolean {
        return true
    }

    override fun `communicated$getClassName`(): String {
        return this.javaClass.name
    }
}