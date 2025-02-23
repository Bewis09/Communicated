package bewis09.communicated.server

import bewis09.communicated.screen.EnvelopeScreenHandler
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.network.PacketByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.network.packet.CustomPayload
import net.minecraft.network.packet.CustomPayload.Id
import net.minecraft.util.Identifier

class EnvelopeClosingScreenOpenerPayloads {
    class C2S(val slot: Int): CommunicatedC2SPayload<C2S> {
        companion object {
            val ID = Id<C2S>(Identifier.of("communicated","close_envelope_screen_c2s"))
            val CODEC: PacketCodec<PacketByteBuf, C2S> = PacketCodec.tuple(
                PacketCodecs.INTEGER,
                { a -> a.slot }
            ) { a -> C2S(a) }
        }

        override fun receive(context: ServerPlayNetworking.Context) {
            val i = context.player()?.currentScreenHandler

            if(i is EnvelopeScreenHandler) {
                ServerPlayNetworking.send(context.player(), S2C(slot))
            }
        }

        override fun getId(): Id<out CustomPayload> {
            return ID
        }
    }

    class S2C(val slot: Int): CustomPayload {
        companion object {
            val ID = Id<S2C>(Identifier.of("communicated","close_envelope_screen_s2c"))
            val CODEC: PacketCodec<PacketByteBuf, S2C> = PacketCodec.tuple(
                PacketCodecs.INTEGER,
                { a -> a.slot }
            ){ a -> S2C(a) }
        }

        override fun getId(): Id<out CustomPayload> {
            return ID
        }
    }
}