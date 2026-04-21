package kz.minmax.mbdebugger

import kz.minmax.mbdebugger.items.DebuggerItem
import net.minecraft.world.item.CreativeModeTabs
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import thedarkcolour.kotlinforforge.forge.MOD_BUS
import thedarkcolour.kotlinforforge.forge.runForDist

const val MOD_ID = "mbdebugger"

@Mod(MOD_ID)
object MBDebugger {
  val LOGGER: Logger = LogManager.getLogger(MOD_ID)

  val ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID)
  val DEBUGGER_ITEM: RegistryObject<DebuggerItem> = ITEMS.register("debugger") { DebuggerItem() }

  init {
    LOGGER.log(Level.INFO, "Hello world!")

    // Register the KDeferredRegister to the mod-specific event bus
    ITEMS.register(MOD_BUS)

    runForDist(
      clientTarget = {
        MOD_BUS.addListener(::onClientSetup)
        "test"
      },
      serverTarget = {
        MOD_BUS.addListener(::onServerSetup)
        "test"
      }
    )
  }

  @SubscribeEvent
  fun addCreative(event: BuildCreativeModeTabContentsEvent) {
    if (event.tabKey == CreativeModeTabs.TOOLS_AND_UTILITIES) {
      event.accept(DEBUGGER_ITEM.get())
    }
  }

  /**
   * This is used for initializing client specific things such as renderers and keymaps Fired on the
   * mod specific event bus.
   */
  private fun onClientSetup(event: FMLClientSetupEvent) {
    LOGGER.log(Level.INFO, "Initializing client...")
  }

  /** Fired on the global Forge bus. */
  private fun onServerSetup(event: FMLDedicatedServerSetupEvent) {
    LOGGER.log(Level.INFO, "Server starting...")
  }
}
