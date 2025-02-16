package bewis09.communicated.item.components

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder

class LetterPaperComponent(val pages: List<String>) {
    companion object {
        private val PAGE_CODEC: Codec<String> = Codec.string(0, 2048)
        private val PAGES_CODEC: Codec<List<String>> = PAGE_CODEC.sizeLimitedListOf(10)
        val CODEC: Codec<LetterPaperComponent> = RecordCodecBuilder.create { instance: RecordCodecBuilder.Instance<LetterPaperComponent> ->
            instance.group(PAGES_CODEC.optionalFieldOf("pages", listOf()).forGetter { a -> a.pages })
                .apply(instance) { pages: List<String> -> LetterPaperComponent(pages) }
        }
    }
}