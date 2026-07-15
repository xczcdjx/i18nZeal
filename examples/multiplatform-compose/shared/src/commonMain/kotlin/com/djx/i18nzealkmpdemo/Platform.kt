package com.djx.i18nzealkmpdemo

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform