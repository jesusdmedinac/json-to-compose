def generate():
    layouts = [
        ("ColumnProps", "children"), ("RowProps", "children"), ("BoxProps", "children"),
        ("NavigationBarProps", "children"), ("NavigationRailProps", "children"),
        ("TabRowProps", "children"),
        ("FlowRowProps", "children"), ("FlowColumnProps", "children"),
        ("SegmentedButtonRowProps", "children"),
        ("PagerProps", "pages"), ("SearchBarProps", "children"), ("BottomBarProps", "children")
    ]
    single_children = [
        ("ButtonProps", "child"), ("CardProps", "child"), ("OutlinedCardProps", "child"),
        ("SurfaceProps", "child"), ("ModalBottomSheetProps", "child"),
        ("ScaffoldProps", "child"), ("NavigationDrawerProps", "child"),
        ("FabProps", "icon"), ("BadgedBoxProps", "child")
    ]
    
    print("    private fun addNodeToChildren(composeNode: ComposeNode): ComposeNode {")
    print("        val updatedNode = composeNode.applyDefaultTextIfComposeTypeIsText()")
    print("        val parentNode = updatedNode.parent ?: return updatedNode")
    print("        val updatedProperties = when (val props = parentNode.properties) {")
    for prop, attr in layouts:
        print(f"            is NodeProperties.{prop} -> props.copy({attr} = (props.{attr} ?: emptyList()) + updatedNode)")
    for prop, attr in single_children:
        print(f"            is NodeProperties.{prop} -> props.copy({attr} = updatedNode)")
    print("            else -> return updatedNode")
    print("        }")
    print("        return parentNode.copy(properties = updatedProperties)")
    print("    }\n")

    print("    private fun updateNodeRecursive(currentNode: ComposeNode, updatedNode: ComposeNode): ComposeNode {")
    print("        if (currentNode.id == updatedNode.id) return updatedNode")
    print("        val updatedProps = when (val props = currentNode.properties) {")
    for prop, attr in layouts:
        print(f"            is NodeProperties.{prop} -> props.copy({attr} = props.{attr}?.map {{ updateNodeRecursive(it, updatedNode) }} ?: emptyList())")
    for prop, attr in single_children:
        print(f"            is NodeProperties.{prop} -> props.copy({attr} = props.{attr}?.let {{ updateNodeRecursive(it, updatedNode) }})")
    print("            else -> return currentNode")
    print("        }")
    print("        return currentNode.copy(properties = updatedProps)")
    print("    }")

generate()
