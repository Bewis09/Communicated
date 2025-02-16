package bewis09.communicated.server

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.network.PacketByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.packet.CustomPayload

object CommunicatedC2SPackets {
    fun register() {
        register(CloseEnvelopePayload.ID, CloseEnvelopePayload.CODEC)
        register(FinishLetterWritingPayload.ID, FinishLetterWritingPayload.CODEC)

        register(EnvelopeClosingScreenOpenerPayloads.C2S.ID, EnvelopeClosingScreenOpenerPayloads.C2S.CODEC)
    }

    private fun <T: CommunicatedC2SPayload<T>> register(id: CustomPayload.Id<T>, codec: PacketCodec<PacketByteBuf, T>) {
        PayloadTypeRegistry.playC2S().register(id, codec)

        ServerPlayNetworking.registerGlobalReceiver(id) {payload, context ->
            payload.receive(context)
        }
    }
}