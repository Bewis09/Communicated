package bewis09.communicated.block

import bewis09.communicated.Communicated
import bewis09.communicated.block.entity.MailboxBlockEntity
import bewis09.communicated.item.CommunicatedItems
import bewis09.communicated.item.components.CommunicatedComponents
import bewis09.communicated.item.interfaces.GeneratedTranslationBlock
import com.mojang.serialization.MapCodec
import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.NbtComponent
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.network.packet.s2c.play.OverlayMessageS2CPacket
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.state.StateManager
import net.minecraft.state.property.EnumProperty
import net.minecraft.util.ActionResult
import net.minecraft.util.BlockMirror
import net.minecraft.util.BlockRotation
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World

class MailboxBlock(settings: Settings, private val en_us: String): BlockWithEntity(settings), GeneratedTranslationBlock {
    companion object {
        val CODEC: MapCodec<MailboxBlock> = createCodec { MailboxBlock(it,"") }
        val FACING: EnumProperty<Direction> = HorizontalFacingBlock.FACING

        val LOCKED_MAILBOX = Communicated.translatedText("locked_mailbox", "This mailbox is locked")
        val WRONG_KEY = Communicated.translatedText("wrong_key", "This is the wrong key")
    }

    override fun getCodec(): MapCodec<out BlockWithEntity> {
        return CODEC
    }

    override fun onUse(state: BlockState?, world: World, pos: BlockPos?, player: PlayerEntity, hit: BlockHitResult?): ActionResult {
        val mailboxBlockEntity = world.getBlockEntity(pos) as? MailboxBlockEntity

        if (world is ServerWorld && mailboxBlockEntity != null) {
            val i = mailboxBlockEntity.addStack(player.inventory.mainHandStack)

            if (i == player.inventory.mainHandStack) {
                when {
                    mailboxBlockEntity.key == null || mailboxBlockEntity.key == player.inventory.mainHandStack.get(CommunicatedComponents.KEY) -> player.openHandledScreen(mailboxBlockEntity)
                    player.inventory.mainHandStack.item == CommunicatedItems.KEY -> (player as ServerPlayerEntity).networkHandler.sendPacket(OverlayMessageS2CPacket(WRONG_KEY))
                    else -> (player as ServerPlayerEntity).networkHandler.sendPacket(OverlayMessageS2CPacket(LOCKED_MAILBOX))
                }
            } else player.inventory.setStack(player.inventory.selectedSlot, i)
        }

        return ActionResult.SUCCESS
    }

    override fun hasComparatorOutput(state: BlockState?): Boolean {
        return true
    }

    override fun getComparatorOutput(state: BlockState?, world: World?, pos: BlockPos?): Int {
        val mailboxBlockEntity = world?.getBlockEntity(pos) as? MailboxBlockEntity

        if(mailboxBlockEntity != null) {
            return mailboxBlockEntity.getComparatorOutput()
        }

        return 0
    }

    override fun createBlockEntity(pos: BlockPos?, state: BlockState?): BlockEntity {
        return MailboxBlockEntity(pos, state)
    }

    override fun getRenderType(state: BlockState?): BlockRenderType {
        return BlockRenderType.MODEL
    }

    override fun getTitle(): String {
        return en_us
    }

    override fun appendProperties(builder: StateManager.Builder<Block?, BlockState?>) {
        builder.add(FACING)
    }

    private fun getShape(state: BlockState): VoxelShape {
        val direction: Direction = state.get(FACING)
        return when (direction) {
            Direction.NORTH -> VoxelShapes.union(Block.createCuboidShape(3.0, 3.0, 8.0, 13.0, 14.0, 16.0), Block.createCuboidShape(2.0, 14.0, 7.0, 14.0, 15.0, 16.0))
            Direction.SOUTH -> VoxelShapes.union(Block.createCuboidShape(3.0, 3.0, 0.0, 13.0, 14.0, 8.0), Block.createCuboidShape(2.0, 14.0, 0.0, 14.0, 15.0, 9.0))
            Direction.WEST -> VoxelShapes.union(Block.createCuboidShape(8.0, 3.0, 3.0, 16.0, 14.0, 13.0), Block.createCuboidShape(7.0, 14.0, 2.0, 16.0, 15.0, 14.0))
            Direction.EAST -> VoxelShapes.union(Block.createCuboidShape(0.0, 3.0, 3.0, 8.0, 14.0, 13.0), Block.createCuboidShape(0.0, 14.0, 2.0, 9.0, 15.0, 14.0))
            else -> Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 16.0)
        }
    }

    override fun getCollisionShape(state: BlockState, world: BlockView, pos: BlockPos, context: ShapeContext): VoxelShape {
        return getShape(state)
    }

    override fun getOutlineShape(state: BlockState, world: BlockView, pos: BlockPos, context: ShapeContext): VoxelShape {
        return getShape(state)
    }

    override fun hasSidedTransparency(state: BlockState?): Boolean {
        return true
    }

    override fun rotate(state: BlockState, rotation: BlockRotation): BlockState {
        return state.with(FACING, rotation.rotate(state.get(FACING)))
    }

    override fun mirror(state: BlockState, mirror: BlockMirror): BlockState {
        return state.rotate(mirror.getRotation(state.get(FACING)))
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState {
        return defaultState.with(FACING, if(ctx.side == Direction.UP || ctx.side == Direction.DOWN) ctx.horizontalPlayerFacing.opposite else ctx.side)
    }

    private fun getItem(): Item {
        return when (this) {
            CommunicatedBlocks.OAK_MAILBOX_BLOCK -> CommunicatedItems.OAK_MAILBOX
            CommunicatedBlocks.SPRUCE_MAILBOX_BLOCK -> CommunicatedItems.SPRUCE_MAILBOX
            CommunicatedBlocks.BIRCH_MAILBOX_BLOCK -> CommunicatedItems.BIRCH_MAILBOX
            CommunicatedBlocks.JUNGLE_MAILBOX_BLOCK -> CommunicatedItems.JUNGLE_MAILBOX
            CommunicatedBlocks.ACACIA_MAILBOX_BLOCK -> CommunicatedItems.ACACIA_MAILBOX
            CommunicatedBlocks.DARK_OAK_MAILBOX_BLOCK -> CommunicatedItems.DARK_OAK_MAILBOX
            CommunicatedBlocks.MANGROVE_MAILBOX_BLOCK -> CommunicatedItems.MANGROVE_MAILBOX
            CommunicatedBlocks.PALE_OAK_MAILBOX_BLOCK -> CommunicatedItems.PALE_OAK_MAILBOX
            CommunicatedBlocks.CHERRY_MAILBOX_BLOCK -> CommunicatedItems.CHERRY_MAILBOX
            else -> CommunicatedItems.OAK_MAILBOX
        }
    }

    override fun onBreak(world: World, pos: BlockPos, state: BlockState?, player: PlayerEntity): BlockState {
        val blockEntity = world.getBlockEntity(pos)
        if (blockEntity is MailboxBlockEntity) {
            if (!world.isClient) {
                val itemStack = ItemStack(getItem())
                itemStack.applyComponentsFrom(blockEntity.createComponentMap())

                val nbt = blockEntity.createComponentlessNbt(world.registryManager)
                if(!nbt.isEmpty) {
                    nbt.putString("id", BlockEntityType.getId(blockEntity.getType()).toString())
                    itemStack.set(DataComponentTypes.BLOCK_ENTITY_DATA, NbtComponent.of(nbt))
                }

                val itemEntity = ItemEntity(world, pos.x.toDouble() + 0.5, pos.y.toDouble() + 0.5, pos.z.toDouble() + 0.5, itemStack)
                itemEntity.setToDefaultPickupDelay()
                world.spawnEntity(itemEntity)
            }
        }

        return super.onBreak(world, pos, state, player)
    }
}