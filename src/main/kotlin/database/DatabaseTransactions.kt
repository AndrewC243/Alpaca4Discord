package database

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseHandler {
    private val db = Database.connect("jdbc:h2:./src/main/resources/guilds", driver = "org.h2.Driver")
    init {
        transaction (db) {
            SchemaUtils.create(Guilds)
        }
    }
    fun addGuild(guildId: Long) = transaction (db) {
        val test = Guild.new {
            this.guildId = guildId
            this.alpacaOauth = ""
        }
    }

    fun setToken(oauth: String, guildId: Long) = transaction (db) {
        safeGet(guildId).alpacaOauth = oauth
    }

    fun getToken(guildId: Long): String = safeGet(guildId).alpacaOauth

    private fun safeGet(guild: Long): Guild = transaction (db) {
        Guild.find { Guilds.guildId eq guild }.firstOrNull() ?: Guild.new {
            this.guildId = guild
            this.alpacaOauth = ""
        }
    }
}