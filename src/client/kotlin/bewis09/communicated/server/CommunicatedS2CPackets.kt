package bewis09.communicated.server

import bewis09.communicated.screen.EnvelopeClosingScreen
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.minecraft.network.PacketByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.packet.CustomPayload

object CommunicatedS2CPackets {
    fun register() {
        register(EnvelopeClosingScreenOpenerPayloads.S2C.ID, EnvelopeClosingScreenOpenerPayloads.S2C.CODEC) { payload, context ->
            context.client().setScreen(EnvelopeClosingScreen(context.player().gameProfile.name, payload.slot))
        }
    }

    private fun <T: CustomPayload> register(id: CustomPayload.Id<T>, codec: PacketCodec<PacketByteBuf, T>, function: (a: T, b: ClientPlayNetworking. Context)->Unit) {
        PayloadTypeRegistry.playS2C().register(id, codec)

        ClientPlayNetworking.registerGlobalReceiver(id) {payload, context ->
            function(payload, context)
        }
    }
}