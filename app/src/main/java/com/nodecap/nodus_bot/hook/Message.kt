package com.nodecap.nodus_bot.hook

import com.gh0u1l5.wechatmagician.spellbook.interfaces.IMessageStorageHook
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.nodecap.nodus_bot.hook.SendMsgHooker.wxMsgSplitStr
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import kotlin.math.log

object Message : IMessageStorageHook {
    override fun onMessageStorageInserted(msgId: Long, msgObject: Any) {
        super.onMessageStorageInserted(msgId, msgObject)

        val field_content = XposedHelpers.getObjectField(msgObject, "field_content") as String?
        val field_talker = XposedHelpers.getObjectField(msgObject, "field_talker") as String?
        val field_type = (XposedHelpers.getObjectField(msgObject, "field_type") as Int).toInt()
        val field_isSend = (XposedHelpers.getObjectField(msgObject, "field_isSend") as Int).toInt()

        if (field_isSend == 1) {
            return
        }
        if (field_type == 1) {
            val replyContent = "$field_content"
            Objects.ChattingFooterEventImpl?.apply {
                // 将 wx_id 和 回复的内容用分隔符分开
                val content = "$field_talker$wxMsgSplitStr$replyContent"
                val success = Methods.ChattingFooterEventImpl_SendMsg.invoke(this, content) as Boolean
            }

            // experimental request

            Fuel.get("https://api.coinmarketcap.com/v2/listings/").responseJson { request, response, result ->
                print(result)
            }
        }
    }
}