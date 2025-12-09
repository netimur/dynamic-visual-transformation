package com.netimur.dynamicvisualtransformation

import androidx.compose.ui.graphics.Color

/**
 * Defines how the visual transformation should display placeholder characters
 * after the end of the user's actual input.
 *
 * This is useful for masks that show the remaining available characters,
 * such as phone numbers or IDs, using a faded or colored style.
 *
 * Example:
 * ```
 * placeholder = "••••••••••"
 * color = Color.Gray
 *
 * User input: "123"
 * → "123•••••••••" (the rest shown using placeholder style)
 * ```
 *
 * @param placeholder
 * A string containing characters that will be appended after the user input.
 * Only the part that extends beyond the input length will be visible.
 *
 * @param color
 * Style color applied to the placeholder characters.
 * If `Color.Unspecified`, the placeholder is rendered with default text color.
 *
 * @author netimur@internet.ru
 */
data class DynamicPlaceholder(
    val placeholder: String = "",
    val color: Color = Color.Unspecified
)