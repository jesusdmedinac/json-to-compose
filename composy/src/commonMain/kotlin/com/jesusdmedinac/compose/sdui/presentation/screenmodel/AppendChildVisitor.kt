package com.jesusdmedinac.compose.sdui.presentation.screenmodel

import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.NodeProperties

class AppendChildVisitor(
    private val newChild: ComposeNode
) : NodePropertiesVisitor<NodeProperties> {

    override fun visit(props: NodeProperties.ColumnProps) = 
        props.copy(children = (props.children ?: emptyList()) + newChild)

    override fun visit(props: NodeProperties.RowProps) = 
        props.copy(children = (props.children ?: emptyList()) + newChild)

    override fun visit(props: NodeProperties.BoxProps) = 
        props.copy(children = (props.children ?: emptyList()) + newChild)

    override fun visit(props: NodeProperties.NavigationBarProps) = 
        props.copy(children = (props.children ?: emptyList()) + newChild)

    override fun visit(props: NodeProperties.NavigationRailProps) = 
        props.copy(children = (props.children ?: emptyList()) + newChild)

    override fun visit(props: NodeProperties.TabRowProps) = 
        props.copy(children = (props.children ?: emptyList()) + newChild)

    override fun visit(props: NodeProperties.FlowRowProps) = 
        props.copy(children = (props.children ?: emptyList()) + newChild)

    override fun visit(props: NodeProperties.FlowColumnProps) = 
        props.copy(children = (props.children ?: emptyList()) + newChild)

    override fun visit(props: NodeProperties.SegmentedButtonRowProps) = 
        props.copy(children = (props.children ?: emptyList()) + newChild)

    override fun visit(props: NodeProperties.PagerProps) = 
        props.copy(pages = (props.pages ?: emptyList()) + newChild)

    override fun visit(props: NodeProperties.SearchBarProps) = 
        props.copy(children = (props.children ?: emptyList()) + newChild)

    override fun visit(props: NodeProperties.BottomBarProps) = 
        props.copy(children = (props.children ?: emptyList()) + newChild)

    override fun visit(props: NodeProperties.ButtonProps) = 
        props.copy(child = newChild)

    override fun visit(props: NodeProperties.CardProps) = 
        props.copy(child = newChild)

    override fun visit(props: NodeProperties.OutlinedCardProps) = 
        props.copy(child = newChild)

    override fun visit(props: NodeProperties.SurfaceProps) = 
        props.copy(child = newChild)

    override fun visit(props: NodeProperties.ModalBottomSheetProps) = 
        props.copy(child = newChild)

    override fun visit(props: NodeProperties.ScaffoldProps) = 
        props.copy(child = newChild)

    override fun visit(props: NodeProperties.NavigationDrawerProps) = 
        props.copy(child = newChild)

    override fun visit(props: NodeProperties.FabProps) = 
        props.copy(icon = newChild)

    override fun visit(props: NodeProperties.BadgedBoxProps) = 
        props.copy(child = newChild)

    override fun visitDefault(props: NodeProperties) = props
}
