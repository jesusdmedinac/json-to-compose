package com.jesusdmedinac.compose.sdui.presentation.ui.layouts

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.UiComposable
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.util.fastForEachIndexed
import kotlin.math.max

@Composable
fun BoxWithSizeListener(
    content: @Composable @UiComposable () -> Unit,
    onSizeChanged: (IntSize) -> Unit,
    modifier: Modifier = Modifier,
    contentAlignment: Alignment,
) {
    Layout(
        content = {
            content()
        },
        modifier = modifier,
        measurePolicy = object : MeasurePolicy {
            override fun MeasureScope.measure(
                measurables: List<Measurable>,
                constraints: Constraints
            ): MeasureResult {
                if (measurables.isEmpty()) {
                    return layout(
                        constraints.minWidth,
                        constraints.minHeight
                    ) {}
                }

                val contentConstraints = constraints.copy(minWidth = 0, minHeight = 0)

                if (measurables.size == 1) {
                    val measurable = measurables[0]
                    val boxWidth: Int
                    val boxHeight: Int
                    val placeable: Placeable = measurable.measure(contentConstraints)
                    boxWidth = max(constraints.minWidth, placeable.width)
                    boxHeight = max(constraints.minHeight, placeable.height)

                    onSizeChanged(IntSize(boxWidth, boxHeight))

                    return layout(boxWidth, boxHeight) {
                        val position = contentAlignment.align(
                            IntSize(placeable.width, placeable.height),
                            IntSize(boxWidth, boxHeight),
                            layoutDirection
                        )
                        placeable.place(position)
                    }
                }

                val placeables = arrayOfNulls<Placeable>(measurables.size)
                // First measure non match parent size children to get the size of the Box.
                var boxWidth = constraints.minWidth
                var boxHeight = constraints.minHeight
                measurables.fastForEachIndexed { index, measurable ->
                    val placeable = measurable.measure(contentConstraints)
                    placeables[index] = placeable
                    boxWidth = max(boxWidth, placeable.width)
                    boxHeight = max(boxHeight, placeable.height)
                }

                // Specify the size of the Box and position its children.
                return layout(boxWidth, boxHeight) {
                    placeables.forEachIndexed { index, placeable ->
                        placeable as Placeable
                        val position = contentAlignment.align(
                            IntSize(placeable.width, placeable.height),
                            IntSize(boxWidth, boxHeight),
                            layoutDirection
                        )
                        placeable.place(position)
                    }
                }
            }
        },
    )
}