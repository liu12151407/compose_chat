package github.leavesc.compose_chat.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.statusBarsPadding
import github.leavesc.compose_chat.base.model.Message
import github.leavesc.compose_chat.base.model.TextMessage
import github.leavesc.compose_chat.base.model.TimeMessage
import github.leavesc.compose_chat.extend.LocalInputMethodManager
import github.leavesc.compose_chat.extend.ProvideInputMethodManager
import github.leavesc.compose_chat.extend.navigate
import github.leavesc.compose_chat.logic.ChatViewModel
import github.leavesc.compose_chat.model.Screen
import github.leavesc.compose_chat.ui.theme.ChatTheme
import github.leavesc.compose_chat.utils.showToast
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

/**
 * @Author: leavesC
 * @Date: 2021/7/3 23:53
 * @Desc:
 * @Github：https://github.com/leavesC
 */
@Composable
fun ChatScreen(
    navController: NavHostController,
    friendId: String
) {
    ChatTheme {
        ProvideInputMethodManager {
            ProvideWindowInsets {
                val chatViewModel = viewModel<ChatViewModel>(factory = object :
                    ViewModelProvider.NewInstanceFactory() {
                    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                        return ChatViewModel(friendId) as T
                    }
                })
                val friendProfile by chatViewModel.friendProfile.collectAsState()
                val chatScreenState by chatViewModel.chatScreenState.collectAsState()
                val loadFinish by rememberUpdatedState(newValue = chatScreenState.loadFinish)
                val clipboardManager = LocalClipboardManager.current
                val inputMethodManager = LocalInputMethodManager.current
                val view = LocalView.current
                val listState = rememberLazyListState()
                val coroutineScope = rememberCoroutineScope()
                LaunchedEffect(listState) {
                    launch {
                        snapshotFlow {
                            if (loadFinish) {
                                false
                            } else {
                                listState.layoutInfo.totalItemsCount - listState.firstVisibleItemIndex < 16
                            }
                        }.distinctUntilChanged()
                            .filter { it }
                            .collect {
                                chatViewModel.onLoadMoreMessage()
                            }
                    }
                    launch {
                        chatViewModel.mushScrollToBottom.collect {
                            listState.animateScrollToItem(index = 0)
                        }
                    }
                }
                DisposableEffect(key1 = Unit) {
                    onDispose {
                        chatViewModel.onDispose()
                    }
                }
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = MaterialTheme.colors.primaryVariant)
                        .statusBarsPadding()
                        .navigationBarsWithImePadding()
                ) {
                    Column {
                        ChatScreenTopBar(
                            friendProfile = friendProfile,
                            onClickBackMenu = {
                                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
                                navController.popBackStack()
                            },
                            onClickMoreMenu = {
                                val userId = friendProfile.userId
                                if (userId.isNotBlank()) {
                                    navController.navigate(
                                        Screen.FriendProfileScreen(friendId = userId)
                                    )
                                }
                            }
                        )
                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            state = listState,
                            reverseLayout = true,
                            contentPadding = PaddingValues(
                                top = 20.dp,
                                bottom = 60.dp
                            ),
                            verticalArrangement = Arrangement.Top,
                        ) {
                            for (message in chatScreenState.messageList) {
                                item(key = message.msgId) {
                                    MessageItem(
                                        message = message,
                                        onClickSelfAvatar = {

                                        },
                                        onClickFriendAvatar = {
                                            navController.navigate(
                                                Screen.FriendProfileScreen(friendId = it)
                                            )
                                        },
                                        onLongPressMessage = {
                                            val msg = (message as? TextMessage)?.msg
                                            if (!msg.isNullOrBlank()) {
                                                clipboardManager.setText(AnnotatedString(msg))
                                                showToast("已复制")
                                            }
                                        },
                                    )
                                }
                            }
                            if (chatScreenState.showLoadMore) {
                                item {
                                    LoadMoreMessageItem()
                                }
                            }
                        }
                        ChatScreenBottomBord(
                            sendMessage = {
                                coroutineScope.launch {
                                    chatViewModel.sendMessage(it)
                                }
                            })
                    }
                }
            }
        }
    }
}

@Composable
fun LoadMoreMessageItem() {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter) {
        CircularProgressIndicator()
    }
}

@Composable
fun MessageItem(
    message: Message,
    onClickSelfAvatar: () -> Unit,
    onClickFriendAvatar: (String) -> Unit,
    onLongPressMessage: (Message) -> Unit,
) {
    val unit = when (message) {
        is TextMessage -> {
            when (message) {
                is TextMessage.SelfTextMessage -> {
                    SelfTextMessageItem(
                        textMessage = message,
                        onClickSelfAvatar = onClickSelfAvatar,
                        onLongPressMessage = onLongPressMessage,
                    )
                }
                is TextMessage.FriendTextMessage -> {
                    FriendTextMessageItem(
                        textMessage = message,
                        onClickFriendAvatar = onClickFriendAvatar,
                        onLongPressMessage = onLongPressMessage,
                    )
                }
            }
        }
        is TimeMessage -> {
            TimeMessageItem(timeMessage = message)
        }
    }
}