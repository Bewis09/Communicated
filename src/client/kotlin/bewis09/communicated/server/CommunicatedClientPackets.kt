package bewis09.communicated.server

import bewis09.communicated.screen.EnvelopeClosingScreen
import bewis09.communicated.screen.LetterViewingScreen
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.network.packet.CustomPayload

object CommunicatedClientPackets {
    fun register() {
        register(EnvelopeClosingScreenOpenerPayloads.S2C.ID) { payload, context ->
            context.client().setScreen(EnvelopeClosingScreen(context.player().gameProfile.name, payload.slot))
        }

        register(LetterOpeningPayload.ID) { payload, context ->
            context.client().setScreen(LetterViewingScreen(payload.component))
        }
    }

    private fun <T: CustomPayload> register(id: CustomPayload.Id<T>,  function: (a: T, b: ClientPlayNetworking. Context)->Unit) {
        ClientPlayNetworking.registerGlobalReceiver(id) {payload, context ->
            function(payload, context)
        }
    }
}