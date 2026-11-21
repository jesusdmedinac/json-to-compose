package com.jesusdmedinac.jsontocompose.parser.a2ui

import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties

/**
 * Fast, linear, error-resilient parser for the A2UI Express DSL.
 *
 * Transpiles linear line-by-line component definitions (e.g., `header = Text("Bienvenido")`,
 * `root = Column([header])`) into typed [ComposeNode] component hierarchies.
 */
class A2UiExpressParser {

    /**
     * Parses an A2UI Express DSL snippet into a root [ComposeNode].
     *
     * @param dsl The raw A2UI Express DSL string.
     * @return The root [ComposeNode], or null if no valid nodes were parsed.
     */
    fun parse(dsl: String): ComposeNode? {
        val symbolTable = mutableMapOf<String, ComposeNode>()
        val referencedSymbols = mutableSetOf<String>()
        val lines = dsl.lines()

        for (rawLine in lines) {
            val line = rawLine.trim()
            if (shouldIgnoreLine(line)) continue

            parseLine(line, symbolTable, referencedSymbols)
        }

        if (symbolTable.isEmpty()) return null

        // 1. Explicit 'root' symbol takes highest priority
        symbolTable["root"]?.let { return it }

        // 2. Unreferenced top-level symbol (not used as a child of any other component)
        val unreferenced = symbolTable.filterKeys { it !in referencedSymbols }
        if (unreferenced.isNotEmpty()) {
            return unreferenced.values.last()
        }

        // 3. Fallback to last defined symbol
        return symbolTable.values.lastOrNull()
    }

    private fun shouldIgnoreLine(line: String): Boolean {
        if (line.isEmpty()) return true
        if (line.startsWith("#") || line.startsWith("//")) return true
        if (line.startsWith("```") || line == "<a2ui>" || line == "</a2ui>") return true
        // Reactive state declarations like $/booking/cabin = "economy"
        if (line.startsWith("$/")) return true
        return false
    }

    private fun parseLine(
        line: String,
        symbolTable: MutableMap<String, ComposeNode>,
        referencedSymbols: MutableSet<String>,
    ) {
        val equalsIndex = findTopLevelAssignmentOperator(line)
        val (symbolName, callExpr) = if (equalsIndex != -1) {
            val name = line.substring(0, equalsIndex).trim()
            val expr = line.substring(equalsIndex + 1).trim()
            name to expr
        } else {
            val generatedName = "_anon_${symbolTable.size}"
            generatedName to line
        }

        val node = parseCallExpression(callExpr, symbolTable, referencedSymbols) ?: return
        symbolTable[symbolName] = node
    }

    private fun parseCallExpression(
        expr: String,
        symbolTable: MutableMap<String, ComposeNode>,
        referencedSymbols: MutableSet<String>,
    ): ComposeNode? {
        val openParen = expr.indexOf('(')
        val closeParen = expr.lastIndexOf(')')
        if (openParen == -1 || closeParen == -1 || closeParen <= openParen) {
            return null
        }

        val typeName = expr.substring(0, openParen).trim()
        val argsString = expr.substring(openParen + 1, closeParen).trim()

        val parsedArgs = splitTopLevel(argsString, ',')
        val positionalArgs = mutableListOf<String>()
        val namedArgs = mutableMapOf<String, String>()

        for (arg in parsedArgs) {
            val trimmed = arg.trim()
            if (trimmed.isEmpty()) continue
            val eqIdx = findTopLevelAssignmentOperator(trimmed)
            if (eqIdx != -1) {
                val key = trimmed.substring(0, eqIdx).trim()
                val value = trimmed.substring(eqIdx + 1).trim()
                namedArgs[key] = value
            } else {
                positionalArgs.add(trimmed)
            }
        }

        return createNodeFromArgs(typeName, positionalArgs, namedArgs, symbolTable, referencedSymbols)
    }

    private fun createNodeFromArgs(
        typeName: String,
        positionalArgs: List<String>,
        namedArgs: Map<String, String>,
        symbolTable: Map<String, ComposeNode>,
        referencedSymbols: MutableSet<String>,
    ): ComposeNode? {
        val composeType = mapComposeType(typeName) ?: return null

        val properties = when (composeType) {
            ComposeType.Column -> {
                val children = resolveChildren(positionalArgs.firstOrNull(), namedArgs["children"], symbolTable, referencedSymbols)
                NodeProperties.ColumnProps(
                    children = children,
                    verticalArrangement = namedArgs["verticalArrangement"] ?: namedArgs["gap"]?.let { "spacedBy($it)" },
                    horizontalAlignment = namedArgs["horizontalAlignment"] ?: namedArgs["align"],
                )
            }
            ComposeType.Row -> {
                val children = resolveChildren(positionalArgs.firstOrNull(), namedArgs["children"], symbolTable, referencedSymbols)
                NodeProperties.RowProps(
                    children = children,
                    horizontalArrangement = namedArgs["horizontalArrangement"] ?: namedArgs["gap"]?.let { "spacedBy($it)" },
                    verticalAlignment = namedArgs["verticalAlignment"] ?: namedArgs["align"],
                )
            }
            ComposeType.Box -> {
                val children = resolveChildren(positionalArgs.firstOrNull(), namedArgs["children"], symbolTable, referencedSymbols)
                NodeProperties.BoxProps(
                    children = children,
                    contentAlignment = namedArgs["contentAlignment"] ?: namedArgs["align"],
                )
            }
            ComposeType.Text -> {
                val rawText = namedArgs["text"] ?: positionalArgs.firstOrNull() ?: ""
                val text = unquote(rawText)
                NodeProperties.TextProps(
                    text = text,
                    fontSize = namedArgs["fontSize"]?.toDoubleOrNull(),
                    fontWeight = namedArgs["fontWeight"],
                    color = namedArgs["color"]?.let { unquote(it) },
                )
            }
            ComposeType.Button, ComposeType.OutlinedButton, ComposeType.TextButton,
            ComposeType.ElevatedButton, ComposeType.FilledTonalButton -> {
                val rawText = namedArgs["text"] ?: positionalArgs.firstOrNull()
                val childNode = if (rawText != null && !rawText.startsWith("[")) {
                    val label = unquote(rawText)
                    ComposeNode(
                        type = ComposeType.Text,
                        properties = NodeProperties.TextProps(text = label),
                    )
                } else null
                NodeProperties.ButtonProps(
                    child = childNode,
                    onClickEventName = namedArgs["onClick"]?.let { extractEventName(it) },
                )
            }
            ComposeType.Card, ComposeType.OutlinedCard, ComposeType.ElevatedCard -> {
                val children = resolveChildren(positionalArgs.firstOrNull(), namedArgs["children"], symbolTable, referencedSymbols)
                val singleChild = if (children.size == 1) {
                    children.first()
                } else if (children.size > 1) {
                    ComposeNode(
                        type = ComposeType.Column,
                        properties = NodeProperties.ColumnProps(children = children),
                    )
                } else null
                NodeProperties.CardProps(
                    child = singleChild,
                )
            }
            else -> composeType.createDefaultProperties()
        }

        return ComposeNode(
            type = composeType,
            properties = properties,
        )
    }

    private fun mapComposeType(typeName: String): ComposeType? {
        return when (typeName.lowercase()) {
            "column" -> ComposeType.Column
            "row" -> ComposeType.Row
            "box" -> ComposeType.Box
            "text", "heading" -> ComposeType.Text
            "button" -> ComposeType.Button
            "outlinedbutton" -> ComposeType.OutlinedButton
            "textbutton" -> ComposeType.TextButton
            "elevatedbutton" -> ComposeType.ElevatedButton
            "card" -> ComposeType.Card
            "spacer" -> ComposeType.Spacer
            "image" -> ComposeType.Image
            "textfield" -> ComposeType.TextField
            "lazycolumn" -> ComposeType.LazyColumn
            "lazyrow" -> ComposeType.LazyRow
            else -> ComposeType.entries.firstOrNull { it.name.equals(typeName, ignoreCase = true) }
        }
    }

    private fun resolveChildren(
        positional: String?,
        named: String?,
        symbolTable: Map<String, ComposeNode>,
        referencedSymbols: MutableSet<String>,
    ): List<ComposeNode> {
        val target = named ?: positional ?: return emptyList()
        val trimmed = target.trim()
        if (!trimmed.startsWith("[") || !trimmed.endsWith("]")) {
            // Single symbol reference
            val cleanSymbol = unquote(trimmed)
            referencedSymbols.add(cleanSymbol)
            return listOfNotNull(symbolTable[cleanSymbol])
        }

        val content = trimmed.substring(1, trimmed.length - 1).trim()
        if (content.isEmpty()) return emptyList()

        val items = splitTopLevel(content, ',')
        val result = mutableListOf<ComposeNode>()
        for (item in items) {
            val symbolName = item.trim()
            if (symbolName.isNotEmpty()) {
                referencedSymbols.add(symbolName)
                symbolTable[symbolName]?.let { result.add(it) }
            }
        }
        return result
    }

    private fun extractEventName(raw: String): String {
        val trimmed = raw.trim()
        if (trimmed.startsWith("Event(") && trimmed.endsWith(")")) {
            return unquote(trimmed.substring(6, trimmed.length - 1))
        }
        return unquote(trimmed)
    }

    private fun unquote(value: String): String {
        val trimmed = value.trim()
        if ((trimmed.startsWith("\"") && trimmed.endsWith("\"")) ||
            (trimmed.startsWith("'") && trimmed.endsWith("'"))
        ) {
            return trimmed.substring(1, trimmed.length - 1)
        }
        return trimmed
    }

    private fun findTopLevelAssignmentOperator(str: String): Int {
        var depthParen = 0
        var depthBracket = 0
        var depthBrace = 0
        var inQuotes = false
        var quoteChar = ' '

        for (i in str.indices) {
            val c = str[i]
            if (inQuotes) {
                if (c == quoteChar && (i == 0 || str[i - 1] != '\\')) {
                    inQuotes = false
                }
            } else {
                when (c) {
                    '"', '\'' -> {
                        inQuotes = true
                        quoteChar = c
                    }
                    '(' -> depthParen++
                    ')' -> if (depthParen > 0) depthParen--
                    '[' -> depthBracket++
                    ']' -> if (depthBracket > 0) depthBracket--
                    '{' -> depthBrace++
                    '}' -> if (depthBrace > 0) depthBrace--
                    '=' -> {
                        if (depthParen == 0 && depthBracket == 0 && depthBrace == 0) {
                            // Ensure it's not == or != or <= or >=
                            val prev = if (i > 0) str[i - 1] else ' '
                            val next = if (i + 1 < str.length) str[i + 1] else ' '
                            if (prev != '=' && prev != '!' && prev != '<' && prev != '>' && next != '=') {
                                return i
                            }
                        }
                    }
                }
            }
        }
        return -1
    }

    private fun splitTopLevel(str: String, delimiter: Char): List<String> {
        val result = mutableListOf<String>()
        var depthParen = 0
        var depthBracket = 0
        var depthBrace = 0
        var inQuotes = false
        var quoteChar = ' '
        var startIndex = 0

        for (i in str.indices) {
            val c = str[i]
            if (inQuotes) {
                if (c == quoteChar && (i == 0 || str[i - 1] != '\\')) {
                    inQuotes = false
                }
            } else {
                when (c) {
                    '"', '\'' -> {
                        inQuotes = true
                        quoteChar = c
                    }
                    '(' -> depthParen++
                    ')' -> if (depthParen > 0) depthParen--
                    '[' -> depthBracket++
                    ']' -> if (depthBracket > 0) depthBracket--
                    '{' -> depthBrace++
                    '}' -> if (depthBrace > 0) depthBrace--
                    else -> {
                        if (c == delimiter && depthParen == 0 && depthBracket == 0 && depthBrace == 0) {
                            result.add(str.substring(startIndex, i))
                            startIndex = i + 1
                        }
                    }
                }
            }
        }
        if (startIndex <= str.length) {
            result.add(str.substring(startIndex))
        }
        return result
    }
}
