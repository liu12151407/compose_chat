package github.leavesc.compose_chat.utils

import android.widget.Toast

/**
 * @Author: leavesC
 * @Date: 2021/6/20 21:52
 * @Desc:
 * @Github：https://github.com/leavesC
 */
fun showToast(msg: Any?) {
    Toast.makeText(ContextHolder.context, msg.toString(), Toast.LENGTH_SHORT).show()
}