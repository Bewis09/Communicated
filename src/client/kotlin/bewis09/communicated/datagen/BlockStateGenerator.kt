package bewis09.communicated.datagen

import bewis09.communicated.block.CommunicatedBlocks
import net.minecraft.block.Block
import net.minecraft.client.data.*
import net.minecraft.client.data.VariantSettings.Rotation
import net.minecraft.registry.Registries
import net.minecraft.state.property.Properties
import net.minecraft.util.Identifier
import net.minecraft.util.math.Direction
import java.util.*

object BlockStateGenerator {
    fun generateBlockStates(blockStateModelGenerator: BlockStateModelGenerator) {
        blockStateModelGenerator.registerMailbox(CommunicatedBlocks.OAK_MAILBOX_BLOCK, "oak")
        blockStateModelGenerator.registerMailbox(CommunicatedBlocks.SPRUCE_MAILBOX_BLOCK, "spruce")
        blockStateModelGenerator.registerMailbox(CommunicatedBlocks.BIRCH_MAILBOX_BLOCK, "birch")
        blockStateModelGenerator.registerMailbox(CommunicatedBlocks.JUNGLE_MAILBOX_BLOCK, "jungle")
        blockStateModelGenerator.registerMailbox(CommunicatedBlocks.ACACIA_MAILBOX_BLOCK, "acacia")
        blockStateModelGenerator.registerMailbox(CommunicatedBlocks.DARK_OAK_MAILBOX_BLOCK, "dark_oak")
        blockStateModelGenerator.registerMailbox(CommunicatedBlocks.CHERRY_MAILBOX_BLOCK, "cherry")
        blockStateModelGenerator.registerMailbox(CommunicatedBlocks.MANGROVE_MAILBOX_BLOCK, "mangrove")
        blockStateModelGenerator.registerMailbox(CommunicatedBlocks.PALE_OAK_MAILBOX_BLOCK, "pale_oak")
    }

    private fun BlockStateModelGenerator.registerWithParent(block: Block, parent: String, texture: Map<String, Identifier>) {
        Model(Optional.of(Identifier.of("communicated","block/$parent")), Optional.empty()).upload(
            block,
            TextureMap().also { texture.forEach { (key, value) -> it.register(TextureKey.of(key), value) }},
            this.modelCollector
        )
    }

    private fun BlockStateModelGenerator.registerMailbox(block: Block, type: String) {
        this.registerHorizontalFacing(block)
        this.registerWithParent(block, "mailbox", hashMapOf(
            Pair("1", Identifier.of("block/"+type+"_log")),
            Pair("2", Identifier.of("block/"+type+"_planks"))
        ))
    }

    private fun BlockStateModelGenerator.registerHorizontalFacing(block: Block) {
        this.blockStateCollector.accept(VariantsBlockStateSupplier.create(block).coordinate(createHorizontalRotationStates(block)))
    }

    private fun createHorizontalRotationStates(block: Block): BlockStateVariantMap {
        return BlockStateVariantMap.create(Properties.HORIZONTAL_FACING)
            .register(Direction.SOUTH, BlockStateVariant.create().put(VariantSettings.MODEL, Registries.BLOCK.getId(block).withPrefixedPath("block/")).put(VariantSettings.Y, Rotation.R180))
            .register(Direction.WEST, BlockStateVariant.create().put(VariantSettings.MODEL, Registries.BLOCK.getId(block).withPrefixedPath("block/")).put(VariantSettings.Y, Rotation.R270))
            .register(Direction.NORTH, BlockStateVariant.create().put(VariantSettings.MODEL, Registries.BLOCK.getId(block).withPrefixedPath("block/")))
            .register(Direction.EAST, BlockStateVariant.create().put(VariantSettings.MODEL, Registries.BLOCK.getId(block).withPrefixedPath("block/")).put(VariantSettings.Y, Rotation.R90))
    }
}