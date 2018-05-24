package com.nodecap.nodus_bot

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage
import com.gh0u1l5.wechatmagician.spellbook.SpellBook
import com.nodecap.nodus_bot.hook.Alert

class WechatHook : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        try {
            if (SpellBook.isImportantWechatProcess(lpparam)) {
                SpellBook.startup(lpparam, listOf(Alert))
            }
        } catch (t: Throwable) {
            XposedBridge.log(t)
        }
    }
}