package net.azisaba.rcdamagecore.extension

import io.lumine.mythic.api.config.MythicLineConfig

fun MythicLineConfig.getStr(vararg aliases: String): String? = getString(aliases)

fun MythicLineConfig.getInt(vararg aliases: String) = getInteger(aliases)
