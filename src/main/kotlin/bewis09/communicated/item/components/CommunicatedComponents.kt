package bewis09.communicated.item.components

import com.mojang.serialization.Codec
import net.minecraft.component.ComponentType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier
import net.minecraft.util.Uuids

object CommunicatedComponents {
    val LETTER_CONTENT = register("letter_content", LetterComponent.CODEC)
    val LETTER_PAPER_CONTENT = register("letter_paper_content", LetterPaperComponent.CODEC)
    val KEY = register("key", Uuids.CODEC)

    private fun <R> register(id: String, codec: Codec<R>): ComponentType<R> {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of("communicated",id), ComponentType.builder<R>().codec(codec).build())
    }
}