package dev.jaims.mcutils.bukkit

import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import java.util.regex.Pattern

/**
 * A chat colorization util that supports hex and PlaceholderAPI placeholders for a [player] if one is provided.
 * Loosely based off of https://gist.github.com/iGabyTM/7415263c2209653ede82457c289de697
 *
 * @sample dev.jaims.mcutils.tests.BukkitTests.chatColorizeTest()
 */
fun String.colorize(player: Player? = null): String {
    val pattern = Pattern.compile(
        "<(#[a-f0-9]{6}|aqua|black|blue|dark_(aqua|blue|gray|green|purple|red)|gray|gold|green|light_purple|red|white|yellow)>",
        Pattern.CASE_INSENSITIVE
    )

    var final = this
    val matcher = pattern.matcher(final)
    while (matcher.find()) {
        try {
            val color = net.md_5.bungee.api.ChatColor.of(matcher.group().replace("<", "").replace(">", ""))
            if (color != null) final = final.replace(matcher.group(), color.toString())
        } catch (ignored: IllegalArgumentException) {
        }
    }
    final = when (player == null) {
        true -> final
        false -> PlaceholderAPI.setPlaceholders(player, final)
    }
    return ChatColor.translateAlternateColorCodes('&', final)

}

/**
 * Colorize a list of strings
 */
fun List<String>.colorize(player: Player? = null): List<String> {
    return map { it.colorize(player) }
}