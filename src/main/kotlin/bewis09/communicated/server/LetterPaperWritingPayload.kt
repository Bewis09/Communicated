package bewis09.communicated.server

import bewis09.communicated.item.CommunicatedItems
import bewis09.communicated.item.components.CommunicatedComponents
import bewis09.communicated.item.components.LetterPaperComponent
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.network.PacketByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.network.packet.CustomPayload
import net.minecraft.network.packet.CustomPayload.Id
import net.minecraft.util.Identifier

class LetterPaperWritingPayload(val pages: List<String>, val slot: Int): CommunicatedC2SPayload<LetterPaperWritingPayload> {
    companion object {
        val ID = Id<LetterPaperWritingPayload>(Identifier.of("communicated","finish_letter_writing"))
        val CODEC: PacketCodec<PacketByteBuf, LetterPaperWritingPayload> = PacketCodec.tuple(
            PacketCodecs.string(1024).collect(PacketCodecs.toList(10)),
            { a: LetterPaperWritingPayload -> a.pages},
            PacketCodecs.INTEGER,
            { a: LetterPaperWritingPayload -> a.slot})
            { str: List<String>, slot: Int -> LetterPaperWritingPayload(str,slot) }
    }

    override fun getId(): Id<out CustomPayload> {
        return ID
    }

    override fun receive(context: ServerPlayNetworking.Context) {
        context.server()?.execute {
            if (PlayerInventory.isValidHotbarIndex(slot) || slot == 40 && context.player().inventory.getStack(slot).item == CommunicatedItems.LETTER_PAPER) {
                context.player().inventory.getStack(slot).set(CommunicatedComponents.LETTER_PAPER_CONTENT, LetterPaperComponent(pages.map { it }))
            }
        }
    }
}