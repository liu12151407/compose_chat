package github.leavesc.compose_chat.base.model

import github.leavesc.compose_chat.base.utils.TimeUtil

/**
 * @Author: leavesC
 * @Date: 2021/6/20 23:40
 * @Desc:
 * @Github：https://github.com/leavesC
 */
sealed class MessageState {

    object Sending : MessageState()

    object SendFailed : MessageState()

    object Completed : MessageState()

}

sealed class Message(
    val msgId: String,
    val timestamp: Long,
    val state: MessageState,
    val sender: PersonProfile,
) {

    val conversationTime by lazy {
        TimeUtil.toConversationTime(timestamp)
    }

    val chatTime by lazy {
        TimeUtil.toChatTime(timestamp)
    }

    var tag: Any? = null

    override fun toString(): String {
        return "Message(msgId='$msgId', timestamp=$timestamp, state=$state, sender=$sender, conversationTime='$conversationTime', chatTime='$chatTime', tag=$tag)"
    }

}

class TimeMessage(
    targetMessage: Message
) : Message(
    msgId = (targetMessage.timestamp + targetMessage.msgId.hashCode()).toString(),
    timestamp = targetMessage.timestamp,
    state = MessageState.Completed,
    sender = PersonProfile.Empty,
)

sealed class TextMessage(
    msgId: String,
    timestamp: Long,
    state: MessageState,
    sender: PersonProfile,
    val msg: String,
) : Message(
    msgId = msgId,
    timestamp = timestamp,
    state = state,
    sender = sender
) {

    class SelfTextMessage(
        msgId: String,
        state: MessageState,
        timestamp: Long,
        sender: PersonProfile,
        msg: String,
    ) : TextMessage(
        msgId = msgId,
        timestamp = timestamp,
        state = state,
        sender = sender,
        msg = msg
    )

    class FriendTextMessage(
        msgId: String,
        timestamp: Long,
        sender: PersonProfile,
        msg: String,
    ) : TextMessage(
        msgId = msgId,
        timestamp = timestamp,
        state = MessageState.Completed,
        sender = sender,
        msg = msg
    )

    fun copy(
        msgId: String = this.msgId,
        msg: String = this.msg,
        timestamp: Long = this.timestamp,
        state: MessageState = this.state,
        sender: PersonProfile = this.sender
    ): TextMessage {
        return when (this) {
            is FriendTextMessage -> {
                FriendTextMessage(
                    msgId = msgId,
                    msg = msg,
                    timestamp = timestamp,
                    sender = sender,
                )
            }
            is SelfTextMessage -> {
                SelfTextMessage(
                    msgId = msgId,
                    msg = msg,
                    state = state,
                    timestamp = timestamp,
                    sender = sender,
                )
            }
        }
    }

}