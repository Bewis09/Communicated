package bewis09.communicated

import bewis09.communicated.item.CommunicatedItems
import bewis09.communicated.item.components.CommunicatedComponents
import bewis09.communicated.screen.EnvelopeScreenHandler
import bewis09.communicated.server.CommunicatedServersidePackets
import net.fabricmc.api.ModInitializer
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.resource.featuretoggle.FeatureFlags
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.server.MinecraftServer
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.WorldSavePath
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.nio.file.Path
import java.security.SecureRandom

object Communicated : ModInitializer {
	private var encryption_key: ByteArray? = null
	private var last_world_path: Path? = null

    @Suppress("MemberVisibilityCanBePrivate")
	val LOGGER: Logger = LoggerFactory.getLogger("Communicated")
	val ENVELOPE_SCREEN_HANDLER = register("envelope_screen_handler") { a, b -> EnvelopeScreenHandler(a, b) }

	val translations = hashMapOf<String, String>()

	override fun onInitialize() {
		LOGGER.info("Loading Communicated!")

		CommunicatedServersidePackets.register()

		CommunicatedItems
		CommunicatedComponents
	}

	fun getEncryptionKey(server: MinecraftServer): ByteArray {
		val path = Path.of(server.getSavePath(WorldSavePath.ROOT).toString(),"communicated/encryption.txt")

		if (path != last_world_path || encryption_key == null) {
			last_world_path = path

			val file = path.toFile()
			if(!file.exists()) return createEncryptionFile(file)

			val bytes = file.readBytes()
			if(bytes.size < 16) return createEncryptionFile(file)

			encryption_key = bytes.sliceArray(IntRange(0,15))
		}

		return encryption_key!!
	}

	private fun createEncryptionFile(file: File): ByteArray {
		val random = SecureRandom()
		val key = ByteArray(16)
		random.nextBytes(key)

		file.parentFile.mkdirs()
		file.createNewFile()

		file.writeBytes(key)

		encryption_key = key

		return key
	}

	private fun <K: ScreenHandler> register(path: String, factory: ScreenHandlerType.Factory<K>): ScreenHandlerType<K> {
		return Registry.register(Registries.SCREEN_HANDLER, Identifier.of("communicated", path), ScreenHandlerType(factory, FeatureFlags.VANILLA_FEATURES))
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