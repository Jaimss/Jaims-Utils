package dev.jaims.mcutils.bukkit.util

import dev.jaims.mcutils.bukkit.KotlinPlugin
import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.util.regex.Pattern
import javax.print.attribute.standard.Severity

/**
 * A chat colorization util that supports hex and PlaceholderAPI placeholders for a [player] if one is provided.
 * Loosely based off of https://gist.github.com/iGabyTM/7415263c2209653ede82457c289de697
 */
fun String.colorize(player: Player? = null): String
{
    val pattern = Pattern.compile(
        "<(#[a-f0-9]{6}|aqua|black|blue|dark_(aqua|blue|gray|green|purple|red)|gray|gold|green|light_purple|red|white|yellow)>",
        Pattern.CASE_INSENSITIVE
    )

    var message = this
    val matcher = pattern.matcher(message)
    while (matcher.find())
    {
        try
        {
            val color = net.md_5.bungee.api.ChatColor.of(matcher.group().replace("<", "").replace(">", ""))
            if (color != null) message = message.replace(matcher.group(), color.toString())
        } catch (ignored: IllegalArgumentException)
        {
            // ignored :)
        }
    }
    message = PlaceholderAPI.setPlaceholders(player, message)
    return ChatColor.translateAlternateColorCodes('&', message)

}

/**
 * Colorize a list of strings
 */
fun List<String>.colorize(player: Player? = null): List<String> = map { it.colorize(player) }

fun String.log(severity: Severity = Severity.REPORT)
{
    val plugin = JavaPlugin.getPlugin(KotlinPlugin::class.java)
    when (severity)
    {
        Severity.ERROR -> plugin.logger.severe(this.colorize())
        Severity.WARNING -> plugin.logger.warning(this.colorize())
        else -> plugin.logger.info(this.colorize())
    }
}