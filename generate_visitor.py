import re

with open("library/src/commonMain/kotlin/com/jesusdmedinac/jsontocompose/model/NodeProperties.kt") as f:
    content = f.read()

classes = re.findall(r'@SerialName\("([^"]+Props)"\)', content)

# Remove duplicates
classes = sorted(list(set(classes)))

interface_methods = []
accept_cases = []

for c in classes:
    interface_methods.append(f"    fun visit(props: NodeProperties.{c}): T = visitDefault(props)")
    accept_cases.append(f"        is NodeProperties.{c} -> visitor.visit(this)")

kotlin_code = f"""package com.jesusdmedinac.compose.sdui.presentation.screenmodel

import com.jesusdmedinac.jsontocompose.model.NodeProperties

interface NodePropertiesVisitor<T> {{
{chr(10).join(interface_methods)}
    fun visitDefault(props: NodeProperties): T
}}

fun <T> NodeProperties.accept(visitor: NodePropertiesVisitor<T>): T = when (this) {{
{chr(10).join(accept_cases)}
}}
"""

print(kotlin_code)
with open("composy/src/commonMain/kotlin/com/jesusdmedinac/compose/sdui/presentation/screenmodel/NodePropertiesVisitor.kt", "w") as f:
    f.write(kotlin_code)

