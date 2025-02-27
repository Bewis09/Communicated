package bewis09.communicated.block

import bewis09.communicated.block.entity.MailboxBlockEntity
import bewis09.communicated.item.components.CommunicatedComponents
import bewis09.communicated.item.interfaces.GeneratedTranslationBlock
import com.mojang.serialization.MapCodec
import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.server.world.ServerWorld
import net.minecraft.state.StateManager
import net.minecraft.state.property.EnumProperty
import net.minecraft.util.ActionResult
import net.minecraft.util.BlockMirror
import net.minecraft.util.BlockRotation
import net.minecraft.util.ItemScatterer
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
    }

    override fun getCodec(): MapCodec<out BlockWithEntity> {
        return CODEC
    }

    override fun onUse(state: BlockState?, world: World, pos: BlockPos?, player: PlayerEntity, hit: BlockHitResult?): ActionResult {
        val mailboxBlockEntity = world.getBlockEntity(pos) as? MailboxBlockEntity

        if (world is ServerWorld && mailboxBlockEntity != null) {
            val i = mailboxBlockEntity.addStack(player.inventory.mainHandStack)

            if(i == player.inventory.mainHandStack && (mailboxBlockEntity.key == null || mailboxBlockEntity.key == player.inventory.mainHandStack.get(CommunicatedComponents.KEY)))
                player.openHandledScreen(mailboxBlockEntity)
            else
                player.inventory.setStack(player.inventory.selectedSlot, i)
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

    override fun onStateReplaced(state: BlockState, world: World?, pos: BlockPos?, newState: BlockState, moved: Boolean) {
        ItemScatterer.onStateReplaced(state, newState, world, pos)
        super.onStateReplaced(state, world, pos, newState, moved)
    }
}