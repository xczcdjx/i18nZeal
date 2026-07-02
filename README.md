# i18nZeal

i18nZeal is a Kotlin Multiplatform i18n helper for Compose Multiplatform apps.

It has two parts:

- `i18n-runtime`: small runtime APIs for `Locale`, language state, CompositionLocal access, and translation lookup.
- `i18n-gradle-plugin`: a Gradle plugin that generates Kotlin constants and an `I18nEngine` implementation from locale files.

## Modules

- `i18n-runtime`: publishable KMP runtime library.
- `i18n-gradle-plugin`: publishable Gradle plugin.
- `shared`: sample Compose Multiplatform shared UI using the plugin and runtime.
- `androidApp`, `desktopApp`, `webApp`, `iosApp`: sample app targets.

## Input Files

Put locale files in the configured input directory, for example:

```text
shared/src/commonMain/i18n/en.json
shared/src/commonMain/i18n/zh.json
```

Example JSON:

```json
{
  "app": {
    "name": "i18nZeal"
  },
  "lang": {
    "current": "Current Language",
    "system": "System",
    "en": "English",
    "zh": "Chinese"
  },
  "count": "Count,{0}"
}
```

Nested keys are flattened with dots, so `app.name` becomes `I18nKeys.app_name`.

## Gradle Setup

When developing this repository, the plugin is loaded from the included build:

```kotlin
pluginManagement {
    includeBuild("i18n-gradle-plugin")
}
```

Then apply it in the app/shared module:

```kotlin
plugins {
    id("com.djx.i18nzeal")
}

i18nZeal {
    sourceLocales = listOf("en", "zh")
    packageName = "com.example.app.i18n"
}
```

Add the generated source directory:

```kotlin
kotlin {
    sourceSets {
        commonMain {
            kotlin.srcDir(layout.buildDirectory.dir("generated/i18nzeal/commonMain/kotlin"))
        }
    }
}
```

Make compilation depend on generation:

```kotlin
tasks.withType<KotlinCompilationTask<*>>().configureEach {
    dependsOn(tasks.named("generateI18nKt"))
}
```

For a published plugin, use:

```kotlin
plugins {
    id("com.djx.i18nzeal") version "0.1.0-SNAPSHOT"
}
```

Runtime dependency:

```kotlin
commonMain.dependencies {
    implementation("com.djx.i18nzeal:i18n-runtime:0.1.0-SNAPSHOT")
}
```

## Generated Code

The plugin generates a Kotlin file like this:

```kotlin
package com.example.app.i18n

import com.djx.i18n.runtime.export.Locale
import com.djx.i18n.runtime.interfaces.I18nEngine

val Lang_En = Locale("en")
val Lang_Zh = Locale("zh")

object I18nKeys {
    const val app_name = "app.name"
    const val count = "count"
}

object I18nZeal : I18nEngine {
    override fun get(
        key: String?,
        locale: Locale,
        fallback: String,
        vararg args: Any?,
    ): String {
        // generated lookup and placeholder formatting
        TODO()
    }
}
```

Placeholders use zero-based indexes:

```json
{
  "count": "Count,{0}"
}
```

```kotlin
tr(I18nKeys.count, count)
```

## Runtime Usage

Initialize the runtime with the generated engine:

```kotlin
I18nRuntime.init(I18nZeal)
```

Provide the current language in Compose:

```kotlin
CompositionLocalProvider(
    AppLocalLangProvider provides AppLangState.current.value
) {
    MainContent()
}
```

Translate inside composables:

```kotlin
Text(tr(I18nKeys.app_name))
Text(tr(I18nKeys.count, count))
```

Change language:

```kotlin
AppLangState.change(Lang_En)
AppLangState.change(Lang_Zh)
```
