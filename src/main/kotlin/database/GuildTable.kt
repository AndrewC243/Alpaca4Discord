package database

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Guilds : IntIdTable() {
    val alpacaOauth = varchar("oauth", 255)
    val guildId = long("guild_id")
}

class Guild(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Guild>(Guilds)
    var alpacaOauth by Guilds.alpacaOauth
    var guildId by Guilds.guildId
}
