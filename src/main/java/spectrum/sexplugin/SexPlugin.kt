package spectrum.sexplugin

import kotlinx.coroutines.*
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import spectrum.sexplugin.hardcore.HardcoreModule
import spectrum.sexplugin.menu.MenuModule
import spectrum.sexplugin.whitelist.WhitelistModule

class SexPlugin : JavaPlugin() {
    companion object {
        lateinit var MainDispatcher: MainCoroutineDispatcher
        private set
        lateinit var DefaultDispatcher: CoroutineDispatcher
        private set
        lateinit var mainScope: CoroutineScope
        private set
        lateinit var defaultScope: CoroutineScope
        private set
        lateinit var plugin: JavaPlugin
        private set
    }

    override fun onEnable() {
        plugin = this
        MainDispatcher = MainThreadDispatcher(this)
        DefaultDispatcher = PluginDispatcher(this)
        mainScope = CoroutineScope(MainDispatcher) + SupervisorJob()
        defaultScope = CoroutineScope(DefaultDispatcher) + SupervisorJob()
        initConfig()
        registerEvents()
        init()
    }

    override fun onDisable() {
        MainDispatcher.cancel()
        DefaultDispatcher.cancel()
        Thread.sleep(100)
    }

    private fun initConfig()
    {
        saveDefaultConfig()
    }
    fun registerEventListener(listener: Listener) {
        server.pluginManager.registerEvents(listener, this)
    }

    private fun registerEvents() {
        MenuModule.eventListeners.forEach(::registerEventListener)
    }

    private fun init() {
        WhitelistModule.init(this)
        HardcoreModule.init(this)
    }
}

inline fun task(crossinline block: () -> Unit) = object : BukkitRunnable() {
    override fun run() {
        block()
    }
}