package com.project.lolchampions

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.yariksoffice.lingver.Lingver

class LocaleChangeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action == Intent.ACTION_LOCALE_CHANGED) {
            val systemLocale = context.resources.configuration.locales[0]
            Lingver.getInstance().setLocale(context, systemLocale.language)
        }
    }
}