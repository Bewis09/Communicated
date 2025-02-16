package bewis09.communicated.server

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.network.packet.CustomPayload

interface CommunicatedC2SPayload<T: CustomPayload>: CustomPayload {
    fun receive(context: ServerPlayNetworking.Context?)
}