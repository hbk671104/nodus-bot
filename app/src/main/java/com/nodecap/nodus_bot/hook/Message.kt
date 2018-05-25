package com.nodecap.nodus_bot.hook

import com.gh0u1l5.wechatmagician.spellbook.interfaces.IMessageStorageHook
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.nodecap.nodus_bot.hook.SendMsgHooker.wxMsgSplitStr
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers

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
            // experimental request
            Fuel.get("http://192.168.1.189:9001/api/nodus-bot?content=$field_content&name=$field_talker&topic=$field_type").responseJson { request, response, result ->
                val content = result.component1()?.obj()?.getString("data")
                if (content != "empty_message") {
                    Objects.ChattingFooterEventImpl?.apply {
                        val replyContent = "$field_talker$wxMsgSplitStr$content"
                        val success = Methods.ChattingFooterEventImpl_SendMsg.invoke(this, replyContent) as Boolean
                        if (success) {
                            XposedBridge.log("reply success!")
                        }
                    }
                }
            }
        }
    }

    private fun printMsgObj(msg: Any) {
        val fieldNames = msg::class.java.fields
        fieldNames.forEach {
            val field = it.get(msg)
            if (field is Array<*>) {
                val s = StringBuffer()
                field.forEach {
                    s.append(it.toString() + " , ")
                }
                XposedBridge.log("$it = $s")
            } else {
                XposedBridge.log("$it = $field")
            }
        }
    }
}