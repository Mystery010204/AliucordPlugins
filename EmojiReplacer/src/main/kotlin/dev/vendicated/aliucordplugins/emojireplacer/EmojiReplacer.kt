/*
 * Ven's Aliucord Plugins
 * Copyright (C) 2021 Vendicated
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
*/

package dev.vendicated.aliucordplugins.emojireplacer

import android.content.Context
import com.aliucord.Logger
import com.aliucord.annotations.AliucordPlugin
import com.aliucord.entities.Plugin
import com.aliucord.patcher.Hook
import com.aliucord.settings.delegate
import com.discord.models.domain.emoji.ModelEmojiUnicode
import com.discord.stores.StoreStream

val logger = Logger("EmojiReplacer")
@AliucordPlugin
class EmojiReplacer : Plugin() {
    private val activePack : String by settings.delegate("none")

    init {
        settingsTab = SettingsTab(Settings::class.java)
    }

    override fun start(ctx: Context) {
        patcher.patch(ModelEmojiUnicode::class.java.getDeclaredMethod("getImageUri", String::class.java, Context::class.java), Hook { param ->
            val emoji = StoreStream.getEmojis().unicodeEmojiSurrogateMap[param.args[0]] ?: return@Hook
            logger.debug(emoji.toString())
        })
    }

    private val emojis = HashMap<String, String>()

    override fun stop(context: Context) {
        patcher.unpatchAll()
        commands.unregisterAll()
    }
}
