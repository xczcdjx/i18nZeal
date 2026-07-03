# i18nZeal

English README. Chinese documentation is available at [docs/README.zh-CN.md](docs/README.zh-CN.md).

`i18nZeal` is a Kotlin Multiplatform i18n helper for Compose Multiplatform apps.

It has two main parts:

- `i18n-runtime`: runtime APIs for `Locale`, language state, `CompositionLocal`, and translation lookup
- `i18n-gradle-plugin`: a Gradle plugin that generates Kotlin constants and an `I18nEngine` implementation from locale files

## Modules

- `i18n-runtime`: publishable KMP runtime library
- `i18n-gradle-plugin`: publishable Gradle plugin
- `shared`: sample shared module showing plugin and runtime usage
- `androidApp`, `desktopApp`, `webApp`, `iosApp`: sample app targets, including Web support

## Gradle Setup

### 1. Apply the plugin in your shared module

```kotlin
plugins {
    // ...
    id("io.github.xczcdjx.i18nzeal")
}

i18nZeal {
    sourceLocales = listOf("en", "zh")
    packageName = "com.example.app.i18n" // generated package name
    // inputDir = "src/commonMain/i18n" // default input directory
    // fileType = I18nFileType.JSON // default file type
}
```

Default extension values:

- `fileType = I18nFileType.JSON`
- `sourceLocales = emptyList()` (must be configured)
- `inputDir = "src/commonMain/i18n"`
- `outputDir = "generated/i18nzeal/commonMain/kotlin"`
- `packageName = null` (must be configured)
- `objectName = "I18nZeal"`

### 2. Add the generated source directory to `commonMain`

```kotlin
kotlin {
    sourceSets {
        commonMain {
            kotlin.srcDir(layout.buildDirectory.dir("generated/i18nzeal/commonMain/kotlin"))
        }
    }
}
```

### 3. Make compilation depend on code generation

```kotlin
tasks.withType<KotlinCompilationTask<*>>().configureEach {
    dependsOn(tasks.named("generateI18nKt"))
}
```

If you want to switch to `.properties` input:

```kotlin
i18nZeal {
    sourceLocales = listOf("en", "zh")
    inputDir = "src/commonMain/i18n-properties"
    fileType = I18nFileType.PROPERTIES
    packageName = "com.example.app.i18n"
}
```

Supported `fileType` values:

- `I18nFileType.JSON`
- `I18nFileType.YAML`
- `I18nFileType.PROPERTIES`
- `I18nFileType.KT`

If you use the published plugin:

```kotlin
plugins {
    id("io.github.xczcdjx.i18nzeal") version "0.1.0"
}
```

Add the runtime dependency:

```kotlin
commonMain.dependencies {
    implementation("io.github.xczcdjx:i18n-runtime:0.1.0")
}
```

If your project also uses Compose resources fonts or other shared resources, keep:

```kotlin
commonMain.dependencies {
    implementation(compose.components.resources)
}
```

## Generated Code

The plugin generates Kotlin code like this:

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

Usage:

```kotlin
tr(I18nKeys.count, count)
```

## Runtime Usage

Initialize the runtime:

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

### Translate inside composables

```kotlin
Text(tr(I18nKeys.app_name))
Text(tr(I18nKeys.count, count))
Text(I18nKeys.app_name.tri18n())
Text(I18nKeys.count.tri18n(count))
```

### Translate outside composables

```kotlin
val title = trn(I18nKeys.app_name)
val countText = I18nKeys.count.trnI18n(count)
```

Notes:

- `tr(...)` and `tri18n(...)` are `@Composable` helpers and read `AppLocalLangProvider.current`
- `trn(...)` and `trnI18n(...)` are non-Compose helpers and read `AppLangState.current.value`

### Change language

```kotlin
AppLangState.change(Lang_En)
AppLangState.change(Lang_Zh)
```

## Supported Input File Formats

The plugin supports:

- `json`
- `yaml` / `yml`
- `properties`
- `kt`

Use one file type per input directory and set `fileType` explicitly.

### JSON Example

Directory:

```text
shared/src/commonMain/i18n/en.json
shared/src/commonMain/i18n/zh.json
```

Content:

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

### YAML Example

Directory:

```text
shared/src/commonMain/i18n-yaml/en.yaml
shared/src/commonMain/i18n-yaml/zh.yaml
```

Content:

```yaml
app:
  name: i18nZeal
lang:
  current: Current Language
  system: System
  en: English
  zh: Chinese
count: "Count,{0}"
```

### Properties Example

Directory:

```text
shared/src/commonMain/i18n-properties/en.properties
shared/src/commonMain/i18n-properties/zh.properties
```

Content:

```properties
app.name=i18nZeal
lang.current=Current Language
lang.system=System
lang.en=English
lang.zh=Chinese
count=Count,{0}
```

Notes:

- `.properties` files are read as UTF-8, so you can write Chinese directly without converting to `\uXXXX`

### Kotlin Map Example

Directory:

```text
shared/src/commonMain/i18n-kt/en.kt
shared/src/commonMain/i18n-kt/zh.kt
```

Content:

```kotlin
object I18nZeal_en {
    val map = mapOf(
        "app.name" to "i18nZeal",
        "lang.current" to "Current Language",
        "lang.system" to "System",
        "lang.en" to "English",
        "lang.zh" to "Chinese",
        "count" to "Count,{0}",
    )
}
```

## Key Rules

Nested JSON/YAML keys are flattened to dot notation:

- `app.name` becomes `I18nKeys.app_name`
