package com.nodecap.nodus_bot

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage
import com.gh0u1l5.wechatmagician.spellbook.SpellBook
import com.gh0u1l5.wechatmagician.spellbook.util.BasicUtil
import com.nodecap.nodus_bot.hook.Alert
import com.nodecap.nodus_bot.hook.Message
import com.nodecap.nodus_bot.hook.SendMsgHooker

class WechatHook : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        BasicUtil.tryVerbosely {
            if (SpellBook.isImportantWechatProcess(lpparam)) {
                SpellBook.startup(lpparam, listOf(Alert, Message), listOf(SendMsgHooker))
            }
        }
    }
}