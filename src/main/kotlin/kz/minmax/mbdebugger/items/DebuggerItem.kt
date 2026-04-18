package kz.minmax.mbdebugger.items

import com.gregtechceu.gtceu.api.machine.MetaMachine
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockControllerMachine
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.UseOnContext
import org.apache.logging.log4j.LogManager

class DebuggerItem : Item(Properties()) {
  override fun getName(pStack: ItemStack): Component {
    return Component.literal("MultiBlock Debugger")
  }

  override fun useOn(context: UseOnContext): InteractionResult {
    val level = context.level
    val player = context.player ?: return InteractionResult.PASS
    val blockPos = context.clickedPos

    val machine = MetaMachine.getMachine(level, blockPos)
    if (machine !is MultiblockControllerMachine) {
      return InteractionResult.PASS
    }
    val state = machine.multiblockState
    val error = state.error

    if (error != null) {
      LOGGER.info("Multiblock error: ${error.errorInfo.getString(120)}")
      player.displayClientMessage(
              Component.literal("Unknown Error:").append(error.errorInfo),
              false
      )
    } else {
      LOGGER.info("Multiblock is valid")
      player.displayClientMessage(Component.literal("Multiblock is valid!"), false)
    }
    return InteractionResult.sidedSuccess(level.isClientSide)
  }

  companion object {
    val LOGGER = LogManager.getLogger(DebuggerItem::javaClass.name)
  }
}
