package spectrum.sexplugin.whitelist.services

import kotlinx.coroutines.runBlocking
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import spectrum.sexplugin.SexPlugin

interface WhitelistService {
    fun onPlayerJoin(player: Player)

    class Fake: WhitelistService {
        override fun onPlayerJoin(player: Player) {}
    }

    class Api(
        private val api: spectrum.sexplugin.whitelist.Api
    ): WhitelistService {
        override fun onPlayerJoin(player: Player) {
            runBlocking {
                if (!api.hasPlayerAccess(player.name)) {
                    player.kick(Component.text(SexPlugin.plugin.config.getString("kickstring")!!))
                    SexPlugin.plugin.logger.info("${player.name} не прошёл")
                }
            }
        }
    }
}