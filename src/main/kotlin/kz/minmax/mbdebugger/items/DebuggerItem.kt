package kz.minmax.mbdebugger.items

import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

class DebuggerItem : Item(Properties()) {
    override fun getName(pStack: ItemStack): Component {
        return Component.literal("MultiBlock Debugger")
    }
}
