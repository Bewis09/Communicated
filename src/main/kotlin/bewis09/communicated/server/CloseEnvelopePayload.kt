package bewis09.communicated.server

import bewis09.communicated.item.CommunicatedItems
import bewis09.communicated.item.EnvelopeItem
import bewis09.communicated.item.components.CommunicatedComponents
import bewis09.communicated.item.components.LetterComponent
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.component.DataComponentTypes
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.network.packet.CustomPayload
import net.minecraft.network.packet.CustomPayload.Id
import net.minecraft.util.Identifier

class CloseEnvelopePayload(val title: String, val designated_to: String, val signed: Boolean, val slot: Int): CommunicatedC2SPayload<CloseEnvelopePayload> {
    companion object {
        val ID = Id<CloseEnvelopePayload>(Identifier.of("communicated","close_envelope"))
        val CODEC: PacketCodec<PacketByteBuf, CloseEnvelopePayload> = PacketCodec.tuple(
            PacketCodecs.INTEGER,
            { a: CloseEnvelopePayload -> a.slot},
            PacketCodecs.STRING,
            { a: CloseEnvelopePayload -> a.designated_to},
            PacketCodecs.STRING,
            { a: CloseEnvelopePayload -> a.title},
            PacketCodecs.BOOLEAN,
            { a: CloseEnvelopePayload -> a.signed})
        { slot: Int, designated_to: String, title: String, signed: Boolean -> CloseEnvelopePayload(title, designated_to, signed, slot) }
    }

    override fun getId(): Id<out CustomPayload> {
        return ID
    }

    override fun receive(context: ServerPlayNetworking.Context?) {
        context?.server()?.execute {
            if(context.player().inventory.getStack(slot).item !is EnvelopeItem)
                return@execute

            val stack = ItemStack(CommunicatedItems.LETTER)
            val envelope = context.player().inventory.getStack(slot)

            stack.set(CommunicatedComponents.LETTER_CONTENT, LetterComponent(
                title,
                if(signed) context.player().gameProfile.name else null,
                envelope.get(DataComponentTypes.CONTAINER)?.stream()?.map {
                    LetterComponent.PaperComponentPart(it.get(CommunicatedComponents.LETTER_PAPER_CONTENT)?.pages ?: listOf(""), it.name)
                }?.toList() ?: listOf(),
                designated_to.ifEmpty { null }
            ))

            context.player().inventory.setStack(slot,stack)
        }
    }
}