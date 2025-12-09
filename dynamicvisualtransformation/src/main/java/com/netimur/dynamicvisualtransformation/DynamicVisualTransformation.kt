package com.netimur.dynamicvisualtransformation

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import java.lang.StringBuilder
import java.util.SortedMap
import java.util.TreeMap

/**
 * A flexible `VisualTransformation` implementation that dynamically inserts formatting symbols
 * into the user's input based on a template string. Unlike fixed masks (e.g., "(123) 456-7890"),
 * this transformation can handle any combination of template characters and placeholder symbols.
 *
 * ## How it works
 *
 * The transformation uses a **template** that contains a special `templateSymbol` character.
 * Every occurrence of this symbol corresponds to a character entered by the user.
 * All other characters in the template (e.g., "+", "-", "(", ")") are treated as fixed
 * formatting symbols and are automatically inserted into the transformed text.
 *
 * Example:
 * ```
 * template = "+7(***) ***-**-**"
 * templateSymbol = '*'
 * userInput = "9876543210"
 *
 * → transformed output: "+7(987) 654-32-10"
 * ```
 *
 * The transformation also supports optional placeholder filling using `DynamicPlaceholder`.
 * This allows displaying missing characters using a styled placeholder string
 * (e.g., gray faded digits).
 *
 * ## Offset mapping
 *
 * The inner `DynamicOffsetMapping` ensures that cursor movement and text selection
 * stay consistent with the user’s input, ignoring the inserted formatting symbols.
 * This means:
 * - Cursor movement feels natural
 * - The user cannot place the cursor *inside* the formatting symbols
 *
 * @param template
 * A string describing the final visual structure of the text.
 * Every occurrence of `templateSymbol` represents a character typed by the user.
 *
 * @param templateSymbol
 * The special character used inside `template` to represent user input positions.
 *
 * @param placeholder
 * Optional placeholder configuration that defines additional visible characters
 * and their color after the user input ends.
 *
 * @throws IllegalArgumentException
 * If the template does not contain at least one `templateSymbol`
 *
 * @see DynamicPlaceholder
 *
 * @author netimur@internet.ru
 */
class DynamicVisualTransformation(
    template: String,
    private val templateSymbol: Char,
    private val placeholder: DynamicPlaceholder = DynamicPlaceholder()
) : VisualTransformation {
    private val sortedMap = parseTemplate(template)
    override fun filter(text: AnnotatedString): TransformedText {
        val stringBuilder = StringBuilder(text.text)
        stringBuilder.insertVisualSymbols(sortedMap)

        return TransformedText(
            text = stringBuilder.expandByAnnotatedPlaceholder(
                placeholder.placeholder,
                placeholder.color
            ),
            offsetMapping = DynamicOffsetMapping(sortedMap, text.length)
        )
    }

    fun parseTemplate(placeholder: String): SortedMap<Int, String> {
        if (!placeholder.contains(templateSymbol)) {
            throw IllegalArgumentException("template must contain at least one templateSymbol")
        }

        val symbolsMap = TreeMap<Int, String>()
        var tempString = placeholder
        while (tempString.containsExtraCharacters()) {
            val startIndex = tempString.indexOfFirst { it != templateSymbol }
            val endIndex = tempString.indexOf(char = templateSymbol, startIndex = startIndex).takeIf { it > startIndex } ?: tempString.length
            val mask = tempString.substring(startIndex = startIndex, endIndex = endIndex)
            tempString = tempString.removeRange(startIndex = startIndex, endIndex = endIndex)
            symbolsMap[startIndex] = mask
        }
        return symbolsMap
    }

    private fun String.containsExtraCharacters() = any { it != templateSymbol }
}

private class DynamicOffsetMapping(
    private val visualSymbolsIndexed: SortedMap<Int, String>,
    private val userInputLength: Int
) : OffsetMapping {
    override fun originalToTransformed(offset: Int): Int {
        return offset + visualSymbolsIndexed.filter { offset >= it.key }.values.sumOf { it.length }
    }

    override fun transformedToOriginal(offset: Int): Int {
        val resultOffset = offset - visualSymbolsIndexed
            .mapTransformed()
            .filter { offset > it.first }
            .sumOf { it.second.length }
        if (resultOffset > userInputLength) return userInputLength
        return resultOffset.takeIf { it >= 0 } ?: 0
    }
}

private fun SortedMap<Int, String>.mapTransformed(): Sequence<Pair<Int, String>> {
    var symbolsOffset = 0
    return map {
        symbolsOffset += it.key
        it.key + symbolsOffset to it.value
    }.asSequence()
}

private fun StringBuilder.toAnnotatedString() =
    buildAnnotatedString { append(this@toAnnotatedString.toString()) }

private fun StringBuilder.insertVisualSymbols(symbolsIndexed: SortedMap<Int, String>) {
    for (entry in symbolsIndexed.entries.reversed()) {
        entry.takeIf { entry.key <= length }?.let { insert(it.key, it.value) }
    }
}

private fun StringBuilder.expandByAnnotatedPlaceholder(
    placeholder: String,
    color: Color
): AnnotatedString {
    return buildAnnotatedString {
        append(this@expandByAnnotatedPlaceholder.toAnnotatedString())
        withStyle(SpanStyle(color = color)) {
            if (placeholder.length >= this@expandByAnnotatedPlaceholder.toAnnotatedString().length) {
                append(
                    placeholder.removeRange(
                        0,
                        this@expandByAnnotatedPlaceholder.toAnnotatedString().length
                    )
                )
            }
        }
    }
}