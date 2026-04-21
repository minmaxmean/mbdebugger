package kz.minmax.mbdebugger.items

import com.gregtechceu.gtceu.api.machine.MetaMachine
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockControllerMachine
import com.gregtechceu.gtceu.api.pattern.MultiblockState
import com.gregtechceu.gtceu.api.pattern.error.PatternError
import kz.minmax.mbdebugger.MOD_ID
import kz.minmax.mbdebugger.client.HighlightManager
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

    if (error == MultiblockState.UNLOAD_ERROR || error == MultiblockState.UNINIT_ERROR) {
      return InteractionResult.PASS
    }
    if (error != null) {
      val errorMessage = formatError(error)

      if (level.isClientSide) {
        LOGGER.info("calling highlight ${error.pos}")
        HighlightManager.highlight(error.pos)
      } else if (player is net.minecraft.server.level.ServerPlayer) {
        kz.minmax.mbdebugger.MBDebugger.NETWORK.sendToPlayer(kz.minmax.mbdebugger.network.SPacketHighlightBlock(error.pos), player)
      }

      LOGGER.info("Multiblock error: ${errorMessage.getString(120)}")
      player.displayClientMessage(errorMessage, false)
    } else {
      LOGGER.info("Multiblock is valid")
      player.displayClientMessage(Component.literal("Multiblock is valid!"), false)
    }
    return InteractionResult.sidedSuccess(level.isClientSide)
  }

  private fun formatError(error: PatternError): Component {
    // Check if error is an instance of base class PatternError
    if (error::class == PatternError::class) {
      val component = Component.literal("Expected components: ")
      val candidates = error.candidates
      var first = true
      for (candidateList in candidates) {
        if (candidateList.isNotEmpty()) {
          if (!first) component.append(Component.literal(", "))
          component.append(candidateList[0].displayName)
          first = false
        }
      }
      component.append(Component.literal(" at (${error.pos.x}, ${error.pos.y}, ${error.pos.z})"))
      return component
    }
    return Component.literal("Unknown Error: ").append(error.errorInfo)
  }

  companion object {
    val LOGGER = LogManager.getLogger(MOD_ID)
  }
}
