package bewis09.communicated.item.components

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import java.util.*
import kotlin.jvm.optionals.getOrNull

class LetterComponent(val title: String, val author: String?, val papers: List<PaperComponentPart>, val designated_to: String?) {
    class PaperComponentPart(val pages: List<String>, val title: String)

    companion object {
        private val PAPER_CODEC: Codec<PaperComponentPart> = RecordCodecBuilder.create {
            it.group(
                Codec.string(0, 2048).sizeLimitedListOf(10).fieldOf("pages").forGetter { a -> a.pages },
                Codec.STRING.fieldOf("title").forGetter { a -> a.title }
            ).apply(it) { pages: List<String>, title: String -> PaperComponentPart(pages, title) }
        }

        val CODEC: Codec<LetterComponent?> = RecordCodecBuilder.create {
            it.group(
                PAPER_CODEC.sizeLimitedListOf(3).fieldOf("papers").forGetter { a -> a?.papers },
                Codec.string(0, 24).fieldOf("title").forGetter { a -> a?.title },
                Codec.string(0, 16).optionalFieldOf("author").forGetter { a -> Optional.ofNullable(a?.author) },
                Codec.string(0, 16).optionalFieldOf("designated_to").forGetter { a -> Optional.ofNullable(a?.designated_to) }
            ).apply(it) { papers: List<PaperComponentPart>, title: String, author: Optional<String>, designated_to: Optional<String> -> LetterComponent(title, author.getOrNull(), papers, designated_to.getOrNull()) }
        }
    }
}