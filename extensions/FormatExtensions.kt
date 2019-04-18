package org.zapomni.venturers.extensions

import java.text.SimpleDateFormat
import java.util.*

fun Date.formatWithSimpleDate() = SimpleDateFormat("dd/MM/yyyy", Locale.US).format(this)
fun Date.formatWithSimpleDateChat() = SimpleDateFormat("HH:mm", Locale.US).format(this)
fun Date.formatWithSimpleDateMeet() = SimpleDateFormat("dd MMM HH:mm", Locale.getDefault()).format(this)
fun Date.formatForCompareDate() = SimpleDateFormat("ddMMyy", Locale.US).format(this)