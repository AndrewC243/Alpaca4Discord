package discord

import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.Commands


fun main() {
    val slashCommands = listOf(
        Commands.slash("ping", "Responds with \"pong!\"").setGuildOnly(true),
        Commands.slash("link", "Link your Alpaca account with the bot").setGuildOnly(true)
            .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)),
        Commands.slash("oauth", "Set the oauth token for the bot to use").setGuildOnly(true)
            .addOption(OptionType.STRING, "code", "OAuth token", true)
            .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)),
        Commands.slash("orders", "Retrieve all the order made on the account").setGuildOnly(true),
        Commands.slash("benchmark", "Get the time it takes to query the database").setGuildOnly(true)
    )
    val api = JDABuilder.createDefault(System.getenv("JDA_TOKEN"))
        .addEventListeners(SetupListener)
        .addEventListeners(QueryListener)
        .build()
    api.updateCommands().addCommands(slashCommands).queue()
}
