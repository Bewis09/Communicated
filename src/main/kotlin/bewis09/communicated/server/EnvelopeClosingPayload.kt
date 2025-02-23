package bewis09.communicated.server

import bewis09.communicated.Communicated
import bewis09.communicated.item.CommunicatedItems
import bewis09.communicated.item.EnvelopeItem
import bewis09.communicated.item.components.CommunicatedComponents
import bewis09.communicated.item.components.LetterComponent
import bewis09.communicated.util.EncryptionUtil
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.component.DataComponentTypes
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.network.packet.CustomPayload
import net.minecraft.network.packet.CustomPayload.Id
import net.minecraft.util.Identifier

class EnvelopeClosingPayload(val title: String, val designated_to: String, val signed: Boolean, val slot: Int): CommunicatedC2SPayload<EnvelopeClosingPayload> {
    companion object {
        val ID = Id<EnvelopeClosingPayload>(Identifier.of("communicated","close_envelope"))
        val CODEC: PacketCodec<PacketByteBuf, EnvelopeClosingPayload> = PacketCodec.tuple(
            PacketCodecs.INTEGER,
            { a: EnvelopeClosingPayload -> a.slot},
            PacketCodecs.STRING,
            { a: EnvelopeClosingPayload -> a.designated_to},
            PacketCodecs.STRING,
            { a: EnvelopeClosingPayload -> a.title},
            PacketCodecs.BOOLEAN,
            { a: EnvelopeClosingPayload -> a.signed})
        { slot: Int, designated_to: String, title: String, signed: Boolean -> EnvelopeClosingPayload(title, designated_to, signed, slot) }
    }

    override fun getId(): Id<out CustomPayload> {
        return ID
    }

    override fun receive(context: ServerPlayNetworking.Context) {
        if(context.player().inventory.getStack(slot).item !is EnvelopeItem)
            return

        val stack = ItemStack(CommunicatedItems.LETTER)
        val envelope = context.player().inventory.getStack(slot)

        stack.set(CommunicatedComponents.LETTER_CONTENT, LetterComponent(
            title,
            if(signed) context.player().gameProfile.name else null,
            envelope.get(DataComponentTypes.CONTAINER)?.stream()?.map {
                LetterComponent.PaperComponentPart(it.get(CommunicatedComponents.LETTER_PAPER_CONTENT)?.pages
                    ?.map { a ->
                        EncryptionUtil.encrypt(a, Communicated.getEncryptionKey(context.server())) } ?: listOf(""), it.name.string)
            }?.toList() ?: listOf(),
            designated_to.ifEmpty { null }
        ))

        context.player().inventory.setStack(slot,stack)
    }
}