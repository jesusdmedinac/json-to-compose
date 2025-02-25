package com.jesusdmedinac.compose.sdui.presentation.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.ComposeTreeScreenModel
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.ComposeTreeState
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.MainScreenModel
import com.jesusdmedinac.compose.sdui.presentation.screenmodel.MainScreenState
import com.jesusdmedinac.jsontocompose.ToCompose

@Composable
fun DeviceLayer(
    modifier: Modifier = Modifier
) {
    val navigator = LocalNavigator.currentOrThrow
    val mainScreenModel: MainScreenModel = navigator.koinNavigatorScreenModel()
    val mainScreenState by mainScreenModel.state.collectAsState()
    val composeTreeScreenModel: ComposeTreeScreenModel = navigator.koinNavigatorScreenModel()
    val composeTreeState by composeTreeScreenModel.state.collectAsState()

    val deviceType = mainScreenState.deviceType
    val deviceSize = mainScreenState.deviceSize
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .then(
                    with(deviceSize) {
                        Modifier.requiredSize(width.dp, height.dp)
                    }
                )
                .clip(MaterialTheme.shapes.small)
                .background(MaterialTheme.colorScheme.background)
        ) {
            composeTreeState.composeNodeRoot.ToCompose()
        }
        Text(
            text = with(deviceSize) { "$deviceType: $width x $height" },
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset(y = -with(deviceSize) { (height / 2).dp } - with(LocalDensity.current) { MaterialTheme.typography.bodyLarge.fontSize.toDp() }),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}