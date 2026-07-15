package com.djx.i18n.runtime

import kotlin.test.Test
import kotlin.test.assertEquals

class I18nFormatterTest {

    @Test
    fun formatsNamedPairs() {
        assertEquals(
            expected = "Hello, Codex! You have 3 messages.",
            actual = formatI18nText(
                "Hello, {name}! You have {count} messages.",
                "name" to "Codex",
                "count" to 3,
            ),
        )
    }

    @Test
    fun formatsNamedMap() {
        assertEquals(
            expected = "Hello, Codex!",
            actual = formatI18nText(
                "Hello, {name}!",
                mapOf("name" to "Codex"),
            ),
        )
    }

    @Test
    fun fillsNamedPlaceholdersInSourceOrderWithPlainArguments() {
        assertEquals(
            expected = "Hello, ZhangSan! You have 3 messages.",
            actual = formatI18nText(
                "Hello, {name}! You have {count} messages.",
                "ZhangSan",
                3,
            ),
        )
    }

    @Test
    fun fillsRemainingNamedPlaceholderAfterExplicitArgument() {
        assertEquals(
            expected = "Hello, ZhangSan! You have 3 messages.",
            actual = formatI18nText(
                "Hello, {name}! You have {count} messages.",
                "name" to "ZhangSan",
                3,
            ),
        )
    }

    @Test
    fun supportsNamedAndPositionalArgumentsTogether() {
        assertEquals(
            expected = "Codex has 3 messages.",
            actual = formatI18nText(
                "{name} has {0} messages.",
                "name" to "Codex",
                3,
            ),
        )
    }

    @Test
    fun replacesNullWithEmptyString() {
        assertEquals("Hello, !", formatI18nText("Hello, {name}!", "name" to null))
    }
}
