package com.djx.i18nzeal

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform