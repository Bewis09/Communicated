package bewis09.communicated

import bewis09.communicated.item.CommunicatedItems
import bewis09.communicated.item.components.CommunicatedComponents
import bewis09.communicated.screen.EnvelopeScreenHandler
import bewis09.communicated.server.CommunicatedC2SPackets
import net.fabricmc.api.ModInitializer
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.resource.featuretoggle.FeatureFlags
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object Communicated : ModInitializer {
    @Suppress("MemberVisibilityCanBePrivate")
	val LOGGER: Logger = LoggerFactory.getLogger("Communicated")
	val ENVELOPE_SCREEN_HANDLER = register("envelope_screen_handler") { a, b -> EnvelopeScreenHandler(a, b) }

	val translations = hashMapOf<String, String>()

	override fun onInitialize() {
		LOGGER.info("Loading Communicated!")

		CommunicatedC2SPackets.register()

		CommunicatedItems
		CommunicatedComponents
	}

	private fun <K: ScreenHandler> register(path: String, factory: ScreenHandlerType.Factory<K>): ScreenHandlerType<K> {
		return Registry.register(Registries.SCREEN_HANDLER, Identifier.of("communicated", path), ScreenHandlerType(factory, FeatureFlags.VANILLA_FEATURES));
	}

	fun translatedText(path: String, en_us: String): MutableText {
		translations["communicated.$path"] = en_us
		return Text.translatable("communicated.$path")
	}

	fun translatedTextWithParams(path: String, en_us: String): (str: Array<Any>) -> MutableText {
		translations["communicated.$path"] = en_us
		return { Text.translatable("communicated.$path",*it) }
	}
}