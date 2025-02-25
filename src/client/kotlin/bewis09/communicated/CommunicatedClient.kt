package bewis09.communicated

import bewis09.communicated.screen.*
import bewis09.communicated.server.CommunicatedClientPackets
import net.fabricmc.api.ClientModInitializer
import net.minecraft.client.gui.screen.ingame.HandledScreens

object CommunicatedClient : ClientModInitializer {
	override fun onInitializeClient() {
		HandledScreens.register(CommunicatedScreenHandlers.ENVELOPE_SCREEN_HANDLER) { a, b, c -> EnvelopeHandledScreen(a, b, c) }
		HandledScreens.register(CommunicatedScreenHandlers.MAILBOX_SCREEN_HANDLER) { a, b, c -> MailboxHandledScreen(a, b, c) }

		initializeTranslations()

		CommunicatedClientPackets.register()
	}

	private fun initializeTranslations() {
		EnvelopeHandledScreen
		EnvelopeClosingScreen
		LetterViewingScreen
	}
}