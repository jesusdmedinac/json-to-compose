package com.jesusdmedinac.compose.sdui.presentation.screenmodel

import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties

class MapPropertiesVisitor(val newType: ComposeType) : NodePropertiesVisitor<NodeProperties> {

    override fun visitDefault(props: NodeProperties): NodeProperties {
        return createNewPropsWith(children = null, child = null, text = null)
    }

    // --- Containers with children ---
    override fun visit(props: NodeProperties.ColumnProps): NodeProperties = createNewPropsWith(children = props.children)
    override fun visit(props: NodeProperties.RowProps): NodeProperties = createNewPropsWith(children = props.children)
    override fun visit(props: NodeProperties.BoxProps): NodeProperties = createNewPropsWith(children = props.children)
    override fun visit(props: NodeProperties.FlowRowProps): NodeProperties = createNewPropsWith(children = props.children)
    override fun visit(props: NodeProperties.FlowColumnProps): NodeProperties = createNewPropsWith(children = props.children)
    override fun visit(props: NodeProperties.TabRowProps): NodeProperties = createNewPropsWith(children = props.children)
    override fun visit(props: NodeProperties.NavigationBarProps): NodeProperties = createNewPropsWith(children = props.children)

    // --- Containers with single child ---
    override fun visit(props: NodeProperties.ButtonProps): NodeProperties = createNewPropsWith(child = props.child)
    override fun visit(props: NodeProperties.CardProps): NodeProperties = createNewPropsWith(child = props.child)
    override fun visit(props: NodeProperties.OutlinedCardProps): NodeProperties = createNewPropsWith(child = props.child)
    override fun visit(props: NodeProperties.SurfaceProps): NodeProperties = createNewPropsWith(child = props.child)
    override fun visit(props: NodeProperties.BadgedBoxProps): NodeProperties = createNewPropsWith(child = props.child)

    // --- Leaves with text ---
    override fun visit(props: NodeProperties.TextProps): NodeProperties = createNewPropsWith(text = props.text)
    override fun visit(props: NodeProperties.TextFieldProps): NodeProperties = createNewPropsWith(text = props.value)

    private fun createNewPropsWith(
        children: List<ComposeNode>? = null,
        child: ComposeNode? = null,
        text: String? = null
    ): NodeProperties {
        val defaultProps = newType.createDefaultProperties()

        val childrenToInject = children ?: child?.let { listOf(it) }
        val childToInject = child ?: children?.firstOrNull()

        return when (defaultProps) {
            is NodeProperties.ColumnProps -> defaultProps.copy(children = childrenToInject)
            is NodeProperties.RowProps -> defaultProps.copy(children = childrenToInject)
            is NodeProperties.BoxProps -> defaultProps.copy(children = childrenToInject)
            is NodeProperties.FlowRowProps -> defaultProps.copy(children = childrenToInject)
            is NodeProperties.FlowColumnProps -> defaultProps.copy(children = childrenToInject)
            is NodeProperties.TabRowProps -> defaultProps.copy(children = childrenToInject)
            is NodeProperties.NavigationBarProps -> defaultProps.copy(children = childrenToInject)

            is NodeProperties.ButtonProps -> defaultProps.copy(child = childToInject)
            is NodeProperties.CardProps -> defaultProps.copy(child = childToInject)
            is NodeProperties.OutlinedCardProps -> defaultProps.copy(child = childToInject)
            is NodeProperties.SurfaceProps -> defaultProps.copy(child = childToInject)
            is NodeProperties.BadgedBoxProps -> defaultProps.copy(child = childToInject)

            is NodeProperties.TextProps -> defaultProps.copy(text = text ?: defaultProps.text)
            is NodeProperties.TextFieldProps -> defaultProps.copy(value = text ?: defaultProps.value)

            else -> defaultProps
        }
    }
}

fun NodeProperties.mapTo(newType: ComposeType): NodeProperties = this.accept(MapPropertiesVisitor(newType))
