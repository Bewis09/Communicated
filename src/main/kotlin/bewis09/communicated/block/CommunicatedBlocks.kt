package bewis09.communicated.block

import bewis09.communicated.Communicated
import bewis09.communicated.item.interfaces.GeneratedTranslationBlock
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier

object CommunicatedBlocks {
    private val WOODEN_MAILBOX_SETTINGS = AbstractBlock.Settings.create()

    val OAK_MAILBOX_BLOCK: MailboxBlock = registerTranslated("oak_mailbox", { MailboxBlock(it, "Oak Mailbox") }, WOODEN_MAILBOX_SETTINGS)
    val SPRUCE_MAILBOX_BLOCK: MailboxBlock = registerTranslated("spruce_mailbox", { MailboxBlock(it, "Spruce Mailbox") }, WOODEN_MAILBOX_SETTINGS)
    val BIRCH_MAILBOX_BLOCK: MailboxBlock = registerTranslated("birch_mailbox", { MailboxBlock(it, "Birch Mailbox") }, WOODEN_MAILBOX_SETTINGS)
    val JUNGLE_MAILBOX_BLOCK: MailboxBlock = registerTranslated("jungle_mailbox", { MailboxBlock(it, "Jungle Mailbox") }, WOODEN_MAILBOX_SETTINGS)
    val ACACIA_MAILBOX_BLOCK: MailboxBlock = registerTranslated("acacia_mailbox", { MailboxBlock(it, "Acacia Mailbox") }, WOODEN_MAILBOX_SETTINGS)
    val DARK_OAK_MAILBOX_BLOCK: MailboxBlock = registerTranslated("dark_oak_mailbox", { MailboxBlock(it, "Dark Oak Mailbox") }, WOODEN_MAILBOX_SETTINGS)
    val CHERRY_MAILBOX_BLOCK: MailboxBlock = registerTranslated("cherry_mailbox", { MailboxBlock(it, "Cherry Mailbox") }, WOODEN_MAILBOX_SETTINGS)
    val MANGROVE_MAILBOX_BLOCK: MailboxBlock = registerTranslated("mangrove_mailbox", { MailboxBlock(it, "Mangrove Mailbox") }, WOODEN_MAILBOX_SETTINGS)
    val PALE_OAK_MAILBOX_BLOCK: MailboxBlock = registerTranslated("pale_oak_mailbox", { MailboxBlock(it, "Pale Oak Mailbox") }, WOODEN_MAILBOX_SETTINGS)

    private fun <T> registerTranslated(id: String, factory: (a: AbstractBlock.Settings) -> T, settings: AbstractBlock.Settings): T where T : Block, T: GeneratedTranslationBlock {
        val b = register(id, factory, settings)
        Communicated.translations["block.communicated.$id"] = b.getTitle()
        return b
    }

    private fun <T: Block> register(id: String, factory: (a: AbstractBlock.Settings) -> T, settings: AbstractBlock.Settings): T {
        @Suppress("UNCHECKED_CAST")
        return Blocks.register(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("communicated", id)), factory, settings) as T
    }
}