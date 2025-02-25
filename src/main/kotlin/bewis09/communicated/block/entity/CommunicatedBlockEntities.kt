package bewis09.communicated.block.entity

import bewis09.communicated.block.CommunicatedBlocks
import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

object CommunicatedBlockEntities {
    val MAILBOX_BLOCK_ENTITY: BlockEntityType<MailboxBlockEntity> = register(
        "mailbox", FabricBlockEntityTypeBuilder.create(
            { a, b -> MailboxBlockEntity(a, b) },
            CommunicatedBlocks.OAK_MAILBOX_BLOCK, CommunicatedBlocks.SPRUCE_MAILBOX_BLOCK, CommunicatedBlocks.BIRCH_MAILBOX_BLOCK, CommunicatedBlocks.JUNGLE_MAILBOX_BLOCK,
            CommunicatedBlocks.ACACIA_MAILBOX_BLOCK, CommunicatedBlocks.DARK_OAK_MAILBOX_BLOCK, CommunicatedBlocks.CHERRY_MAILBOX_BLOCK, CommunicatedBlocks.MANGROVE_MAILBOX_BLOCK,
            CommunicatedBlocks.PALE_OAK_MAILBOX_BLOCK
        ).build()
    )

    private fun <T : BlockEntityType<*>?> register(path: String?, blockEntityType: T): T {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of("communicated", path), blockEntityType)
    }
}