package kz.minmax.mbdebugger.network

import com.lowdragmc.lowdraglib.networking.IHandlerContext
import com.lowdragmc.lowdraglib.networking.IPacket
import kz.minmax.mbdebugger.client.HighlightManager
import net.minecraft.core.BlockPos
import net.minecraft.network.FriendlyByteBuf
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.fml.loading.FMLEnvironment

class SPacketHighlightBlock(var pos: BlockPos? = null) : IPacket {
  override fun encode(buf: FriendlyByteBuf) {
    val nonNullPos = pos
    if (nonNullPos != null) {
      buf.writeBoolean(true)
      buf.writeBlockPos(nonNullPos)
    } else {
      buf.writeBoolean(false)
    }
  }

  override fun decode(buf: FriendlyByteBuf) {
    if (buf.readBoolean()) {
      pos = buf.readBlockPos()
    } else {
      pos = null
    }
  }

  override fun execute(context: IHandlerContext) {
    if (FMLEnvironment.dist == Dist.CLIENT) {
      val nonNullPos = pos
      if (nonNullPos != null) {
        HighlightManager.highlight(nonNullPos)
      }
    }
  }
}
