package bewis09.communicated.screen

import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.resource.featuretoggle.FeatureFlags
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.util.Identifier

object CommunicatedScreenHandlers {
    val ENVELOPE_SCREEN_HANDLER = register("envelope_screen_handler") { a, b -> EnvelopeScreenHandler(a, b) }
    val MAILBOX_SCREEN_HANDLER = register("mailbox_screen_handler") { a, b -> MailboxScreenHandler(a, b) }

    private fun <K: ScreenHandler> register(path: String, factory: ScreenHandlerType.Factory<K>): ScreenHandlerType<K> {
        return Registry.register(Registries.SCREEN_HANDLER, Identifier.of("communicated", path), ScreenHandlerType(factory, FeatureFlags.VANILLA_FEATURES))
    }
}