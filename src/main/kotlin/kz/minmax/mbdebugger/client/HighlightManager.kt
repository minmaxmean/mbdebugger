package kz.minmax.mbdebugger.client

import com.lowdragmc.lowdraglib.client.utils.RenderUtils
import kz.minmax.mbdebugger.MOD_ID
import net.minecraft.core.BlockPos
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.client.event.RenderLevelStageEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import org.apache.logging.log4j.LogManager

@Mod.EventBusSubscriber(
  modid = MOD_ID,
  bus = Mod.EventBusSubscriber.Bus.FORGE,
  value = [Dist.CLIENT]
)
object HighlightManager {
  val LOGGER = LogManager.getLogger(MOD_ID)

  var errorPos: BlockPos? = null
  var timeout: Long = 0

  fun highlight(pos: BlockPos) {
    LOGGER.info("Highlighting $pos")
    errorPos = pos
    timeout = System.currentTimeMillis() + 10000 // 10 seconds timeout
  }

  @SubscribeEvent
  fun onRenderLevel(event: RenderLevelStageEvent) {
    if (event.stage != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) return
    val pos = errorPos ?: return
    if (System.currentTimeMillis() > timeout) {
      errorPos = null
      return
    }

    val camera = event.camera
    val view = camera.position
    val poseStack = event.poseStack
    poseStack.pushPose()
    // Translate relative to camera
    poseStack.translate(-view.x, -view.y, -view.z)

    // Render solid red box slightly larger than the block
    RenderUtils.renderBlockOverLay(poseStack, pos, 1.0f, 0.0f, 0.0f, 1.02f)

    poseStack.popPose()
  }
}
