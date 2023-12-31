package com.pawlowski.temperaturemanager.ui.utils

import java.text.SimpleDateFormat
import java.util.Date

fun Date.formatHHmm(): String = SimpleDateFormat("HH:mm").format(this)

fun Date.formatDDMMYYYYHHmm(): String = SimpleDateFormat("DD.MM.YYYY HH:mm").format(this)
