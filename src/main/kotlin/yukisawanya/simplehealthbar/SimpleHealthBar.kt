package yukisawanya.simplehealthbar

import net.fabricmc.api.ClientModInitializer

// For support join https://discord.gg/v6v4pMv

//@Suppress("unused")
//fun init() {
//    // This code runs as soon as Minecraft is in a mod-load-ready state.
//    // However, some things (like resources) may still be uninitialized.
//    // Proceed with mild caution.
//
//    println("Hello Fabric world!")
//}
class SimpleHealthBar : ClientModInitializer{
    override fun onInitializeClient() {
        println("Hello Fabric world!")
    }

}