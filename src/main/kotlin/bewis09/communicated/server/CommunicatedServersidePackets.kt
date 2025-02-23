package bewis09.communicated.server

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.network.PacketByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.packet.CustomPayload

object CommunicatedServersidePackets {
    fun register() {
        registerC2S(EnvelopeClosingPayload.ID, EnvelopeClosingPayload.CODEC)
        registerC2S(LetterPaperWritingPayload.ID, LetterPaperWritingPayload.CODEC)

        registerC2S(EnvelopeClosingScreenOpenerPayloads.C2S.ID, EnvelopeClosingScreenOpenerPayloads.C2S.CODEC)
        registerS2C(EnvelopeClosingScreenOpenerPayloads.S2C.ID, EnvelopeClosingScreenOpenerPayloads.S2C.CODEC)

        registerS2C(LetterOpeningPayload.ID, LetterOpeningPayload.CODEC)
    }

    private fun <T: CustomPayload> registerS2C(id: CustomPayload.Id<T>, codec: PacketCodec<PacketByteBuf, T>) {
        PayloadTypeRegistry.playS2C().register(id, codec)
    }

    private fun <T: CommunicatedC2SPayload<T>> registerC2S(id: CustomPayload.Id<T>, codec: PacketCodec<PacketByteBuf, T>) {
        PayloadTypeRegistry.playC2S().register(id, codec)

        ServerPlayNetworking.registerGlobalReceiver(id) {payload, context ->
            context.server().execute {
                payload.receive(context)
            }
        }
    }
}