package discord

import alpaca.canConnect
import alpaca.getOrders
import alpaca.oauthExchange
import database.DatabaseHandler
import net.dv8tion.jda.api.events.guild.GuildJoinEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import kotlin.system.measureTimeMillis

object SetupListener : ListenerAdapter() {
    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        when (event.name) {
            "ping" -> event.reply("pong!")
            "link" -> event.reply("""
                To link your accounts, paste the following link into your browser:
                https://app.alpaca.markets/oauth/authorize?response_type=code&client_id=ba79e2d8748945c9eabd4889324a5080&redirect_uri=https://127.0.0.1&scope=account:write%20trading
                Please note that this SHOULD NOT BE DONE WITH LIVE TRADING ACCOUNTS!
                When you authorize the bot, you will get redirected to your own computer (127.0.0.1) with a token looking something like this:
                code=XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXX
                Enter the oauth command passing this token (without "code=") as a parameter.
            """.trimIndent()).queue()
            "oauth" -> {
                event.deferReply(true).queue()
                val accessToken = oauthExchange(event.options[0].asString)
                if (canConnect(accessToken!!)) {
                    DatabaseHandler.setToken(accessToken, event.guild!!.idLong)
                    event.hook.sendMessage("Successfully connected!").queue()
                }
            }
        }
    }

    override fun onGuildJoin(event: GuildJoinEvent) {
        DatabaseHandler.addGuild(event.guild.idLong)
    }
}

object QueryListener : ListenerAdapter() {
    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        when (event.name) {
            "orders" -> {
                val orderList = getOrders(event.guild!!.idLong)
                event.reply(orderList.toString()).queue()
            }
        }
    }
}