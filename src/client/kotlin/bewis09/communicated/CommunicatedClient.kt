package bewis09.communicated

import bewis09.communicated.screen.EnvelopeClosingScreen
import bewis09.communicated.screen.EnvelopeHandledScreen
import bewis09.communicated.screen.LetterViewingScreen
import bewis09.communicated.server.CommunicatedS2CPackets
import net.fabricmc.api.ClientModInitializer
import net.minecraft.client.gui.screen.ingame.HandledScreens

object CommunicatedClient : ClientModInitializer {
	override fun onInitializeClient() {
		HandledScreens.register(Communicated.ENVELOPE_SCREEN_HANDLER) { a, b, c -> EnvelopeHandledScreen(a, b, c) }

		initializeTranslations()

		CommunicatedS2CPackets.register()
	}

	private fun initializeTranslations() {
		EnvelopeHandledScreen
		EnvelopeClosingScreen
		LetterViewingScreen
	}
}