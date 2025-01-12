package github.leavesc.compose_chat.ui.chat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import github.leavesc.compose_chat.base.model.PersonProfile
import github.leavesc.compose_chat.ui.common.CommonDivider

/**
 * @Author: leavesC
 * @Date: 2021/7/5 23:33
 * @Desc:
 * @Github：https://github.com/leavesC
 */
@Composable
fun ChatScreenTopBar(
    friendProfile: PersonProfile,
    onClickBackMenu: () -> Unit,
    onClickMoreMenu: () -> Unit,
) {
    TopAppBar(
        backgroundColor = MaterialTheme.colors.primaryVariant,
        elevation = 0.dp,
        contentPadding = PaddingValues(
            top = 0.dp,
            bottom = 0.dp,
        ),
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            val (backMenu, showName, moreMenu, divider) = createRefs()
            Icon(
                modifier = Modifier
                    .size(size = 28.dp)
                    .constrainAs(ref = backMenu) {
                        start.linkTo(anchor = parent.start, margin = 12.dp)
                        top.linkTo(anchor = parent.top)
                        bottom.linkTo(anchor = parent.bottom)
                    }
                    .clickable(onClick = onClickBackMenu),
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null,
                tint = MaterialTheme.colors.surface
            )
            Text(
                text = friendProfile.showName,
                modifier = Modifier
                    .constrainAs(ref = showName) {
                        start.linkTo(anchor = backMenu.end)
                        end.linkTo(anchor = moreMenu.start)
                        top.linkTo(anchor = parent.top)
                        bottom.linkTo(anchor = parent.bottom)
                        width = Dimension.fillToConstraints
                    }
                    .padding(start = 20.dp, end = 20.dp),
                style = MaterialTheme.typography.subtitle1,
                fontSize = 19.sp,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Icon(
                modifier = Modifier
                    .size(size = 28.dp)
                    .constrainAs(ref = moreMenu) {
                        top.linkTo(anchor = parent.top)
                        bottom.linkTo(anchor = parent.bottom)
                        end.linkTo(anchor = parent.end, margin = 12.dp)
                    }
                    .clickable(onClick = {
                        onClickMoreMenu()
                    }),
                imageVector = Icons.Filled.MoreVert,
                contentDescription = null,
                tint = MaterialTheme.colors.surface
            )
            CommonDivider(modifier = Modifier
                .constrainAs(ref = divider) {
                    start.linkTo(anchor = parent.start)
                    bottom.linkTo(anchor = parent.bottom)
                    end.linkTo(anchor = parent.end)
                })
        }
    }
}