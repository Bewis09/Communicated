package bewis09.communicated.item

import bewis09.communicated.Communicated
import bewis09.communicated.block.CommunicatedBlocks
import bewis09.communicated.item.interfaces.FlatModelItem
import bewis09.communicated.item.interfaces.GeneratedTranslationItem
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.block.Block
import net.minecraft.item.*
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier

object CommunicatedItems {
    val flatModelItems = arrayListOf<Item>()

    val LETTER = registerPlainAndTranslated("letter", { s -> LetterItem(s) }, Item.Settings().maxCount(1))
    val LETTER_PAPER = registerPlainAndTranslated("letter_paper", { s -> LetterPaperItem(s) }, Item.Settings().maxCount(1), ItemGroups.TOOLS)
    val ENVELOPE = registerPlainAndTranslated("envelope", { s -> EnvelopeItem(s) }, Item.Settings().maxCount(1), ItemGroups.TOOLS)

    val OAK_MAILBOX = registerBlockItem("oak_mailbox", CommunicatedBlocks.OAK_MAILBOX_BLOCK )
    val SPRUCE_MAILBOX = registerBlockItem("spruce_mailbox", CommunicatedBlocks.SPRUCE_MAILBOX_BLOCK )
    val BIRCH_MAILBOX = registerBlockItem("birch_mailbox", CommunicatedBlocks.BIRCH_MAILBOX_BLOCK )
    val JUNGLE_MAILBOX = registerBlockItem("jungle_mailbox", CommunicatedBlocks.JUNGLE_MAILBOX_BLOCK )
    val ACACIA_MAILBOX = registerBlockItem("acacia_mailbox", CommunicatedBlocks.ACACIA_MAILBOX_BLOCK )
    val DARK_OAK_MAILBOX = registerBlockItem("dark_oak_mailbox", CommunicatedBlocks.DARK_OAK_MAILBOX_BLOCK )
    val CHERRY_MAILBOX = registerBlockItem("cherry_mailbox", CommunicatedBlocks.CHERRY_MAILBOX_BLOCK )
    val MANGROVE_MAILBOX = registerBlockItem("mangrove_mailbox", CommunicatedBlocks.MANGROVE_MAILBOX_BLOCK )
    val PALE_OAK_MAILBOX = registerBlockItem("pale_oak_mailbox", CommunicatedBlocks.PALE_OAK_MAILBOX_BLOCK )

    private fun <T> registerPlainAndTranslated(path: String, factory: ((Item.Settings) -> T), settings: Item.Settings?, vararg itemGroup: RegistryKey<ItemGroup>): T where T : Item, T: GeneratedTranslationItem {
        val i = registerWithTranslation(path, factory, settings, { it.getTitle() }, *itemGroup)

        if(i is FlatModelItem)
            flatModelItems.add(i)

        return i
    }

    private fun <T: Block> registerBlockItem(path: String, b: T): BlockItem {
        val i = register(path, { s -> BlockItem(b, s) }, Item.Settings().useBlockPrefixedTranslationKey(), ItemGroups.FUNCTIONAL)
        return i
    }

    private fun <T: Item> registerWithTranslation(path: String, factory: ((Item.Settings) -> T), settings: Item.Settings?, en_us: (i: T) -> String, vararg itemGroup: RegistryKey<ItemGroup>): T {
        val i = register(path, factory, settings, *itemGroup)
        Communicated.translations["item.communicated.$path"] = en_us(i)
        return i
    }

    private fun <T: Item> register(path: String, factory: ((Item.Settings) -> T), settings: Item.Settings?, vararg itemGroup: RegistryKey<ItemGroup>): T {
        val registryKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of("communicated", path))
        val i = Items.register(registryKey, factory, settings)

        itemGroup.forEach { s ->
            ItemGroupEvents.modifyEntriesEvent(s).register {
                it.add { i }
            }
        }

        @Suppress("UNCHECKED_CAST")
        return i as T
    }
}