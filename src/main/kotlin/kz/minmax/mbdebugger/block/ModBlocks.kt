package kz.minmax.mbdebugger.block

import kz.minmax.mbdebugger.MBDebugger
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries

object ModBlocks {
  val REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, MBDebugger.ID)
}