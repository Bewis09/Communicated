package bewis09.communicated.server

import bewis09.communicated.item.components.LetterComponent
import net.minecraft.network.PacketByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.network.packet.CustomPayload
import net.minecraft.network.packet.CustomPayload.Id
import net.minecraft.util.Identifier

class LetterOpeningPayload(val component: LetterComponent): CustomPayload {
    companion object {
        val ID = Id<LetterOpeningPayload>(Identifier.of("communicated", "open_letter"))

        private val PAPER_CODEC: PacketCodec<PacketByteBuf, LetterComponent.PaperComponentPart> = PacketCodec.tuple(
            PacketCodecs.STRING,
            { a -> a.title },
            PacketCodecs.string(2048).collect(PacketCodecs.toList(10)),
            { a -> a.pages }
        ) { a, b -> LetterComponent.PaperComponentPart(b, a) }

        val CODEC: PacketCodec<PacketByteBuf, LetterOpeningPayload> = PacketCodec.tuple(
            PacketCodecs.STRING,
            { a -> a.component.title },
            PacketCodecs.STRING,
            { a -> a.component.author },
            PAPER_CODEC.collect(PacketCodecs.toList(10)),
            { a -> a.component.papers }
        ) { a, b, c -> LetterOpeningPayload(LetterComponent(a, b, c, null)) }
    }

    override fun getId(): Id<out CustomPayload> {
        return ID
    }
}