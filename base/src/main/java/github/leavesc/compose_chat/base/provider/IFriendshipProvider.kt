package github.leavesc.compose_chat.base.provider

import github.leavesc.compose_chat.base.model.ActionResult
import github.leavesc.compose_chat.base.model.PersonProfile
import kotlinx.coroutines.flow.StateFlow

/**
 * @Author: leavesC
 * @Date: 2021/6/27 15:49
 * @Desc:
 * @Github：https://github.com/leavesC
 */
interface IFriendshipProvider {

    val friendList: StateFlow<List<PersonProfile>>

    fun getFriendList()

    suspend fun getFriendProfile(friendId: String): PersonProfile?

    suspend fun setFriendRemark(friendId: String, remark: String): ActionResult

    suspend fun addFriend(friendId: String): ActionResult

    suspend fun deleteFriend(friendId: String): ActionResult

}