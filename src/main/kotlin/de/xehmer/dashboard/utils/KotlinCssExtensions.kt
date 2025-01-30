package de.xehmer.dashboard.utils

import kotlinx.css.CssBuilder
import kotlinx.css.GridTemplateColumns
import kotlinx.css.GridTemplateRows
import kotlinx.css.LinearDimension
import kotlinx.html.CoreAttributeGroupFacade
import kotlinx.html.HtmlTagMarker
import kotlinx.html.style
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

fun GridTemplateRows.Companion.repeat(count: Int, dimension: LinearDimension) =
    repeat("$count, minmax(0, $dimension)")
fun GridTemplateColumns.Companion.repeat(count: Int, dimension: LinearDimension) =
    repeat("$count, minmax(0, $dimension)")

@HtmlTagMarker
@OptIn(ExperimentalContracts::class)
inline fun CoreAttributeGroupFacade.inlineStyle(block: CssBuilder.() -> Unit) {
    contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }
    this.style = CssBuilder().apply(block).toString().filter { it != '\n' }
}
