# i18nZeal 中文文档

`i18nZeal` 是一个面向 Compose Multiplatform 的 Kotlin Multiplatform 国际化工具。

它主要由两部分组成：

- `i18n-runtime`：提供 `Locale`、当前语言状态、`CompositionLocal` 和翻译查询能力。
- `i18n-gradle-plugin`：根据多语言文件生成 Kotlin 常量和 `I18nEngine` 实现。

## 模块说明

- `i18n-runtime`：可发布的 KMP runtime 库。
- `i18n-gradle-plugin`：可发布的 Gradle 插件。
- `shared`：示例共享模块，演示插件和 runtime 的使用方式。
- `androidApp`、`desktopApp`、`webApp`、`iosApp`：示例应用目标，包含 Web 支持。

## Gradle 配置使用

### 1.在共享模块中应用插件：

```kotlin

plugins {
    // ...
    id("com.djx.i18nzeal")
}

i18nZeal {
    sourceLocales = listOf("en", "zh")
    packageName = "com.example.app.i18n" // generated 包名,请与你的项目一致
    // inputDir = "src/commonMain/i18n" // 默认来源目录
    // fileType = I18nFileType.JSON 默认json
}
```
### 2.把生成目录加入 `commonMain`：

```kotlin
kotlin {
    sourceSets {
        commonMain {
            kotlin.srcDir(layout.buildDirectory.dir("generated/i18nzeal/commonMain/kotlin"))
        }
    }
}
```

### 3.让编译依赖生成任务：

```kotlin
tasks.withType<KotlinCompilationTask<*>>().configureEach {
    dependsOn(tasks.named("generateI18nKt"))
}
```

如果你想切换到 `.properties`：

```kotlin
i18nZeal {
    sourceLocales = listOf("en", "zh")
    inputDir = "src/commonMain/i18n-properties"
    fileType = I18nFileType.PROPERTIES
    packageName = "com.example.app.i18n"
}
```

支持的 `fileType`：

- `I18nFileType.JSON`
- `I18nFileType.YAML`
- `I18nFileType.PROPERTIES`
- `I18nFileType.KT`

如果使用已发布插件：

```kotlin
plugins {
    id("com.djx.i18nzeal") version "0.1.0"
}
```

添加 runtime 依赖：

```kotlin
commonMain.dependencies {
    implementation("com.djx.i18nzeal:i18n-runtime:0.1.0")
}
```

如果项目里还使用 Compose 资源字体或其他共享资源，建议同时保留：

```kotlin
commonMain.dependencies {
    implementation(compose.components.resources)
}
```

## 生成代码

插件会生成类似下面的 Kotlin 文件：

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

占位符使用从 `0` 开始的索引：

```json
{
  "count": "Count,{0}"
}
```

调用方式：

```kotlin
tr(I18nKeys.count, count)
```

## Runtime 用法

初始化 runtime：

```kotlin
I18nRuntime.init(I18nZeal)
```

在 Compose 中提供当前语言：

```kotlin
CompositionLocalProvider(
    AppLocalLangProvider provides AppLangState.current.value
) {
    MainContent()
}
```

### Composable 内翻译

```kotlin
Text(tr(I18nKeys.app_name))
Text(tr(I18nKeys.count, count))
Text(I18nKeys.app_name.tri18n())
Text(I18nKeys.count.tri18n(count))
```

### Composable 外翻译

```kotlin
val title = trn(I18nKeys.app_name)
val countText = I18nKeys.count.trnI18n(count)
```

说明：

- `tr(...)`、`tri18n(...)` 是 `@Composable` 方法，会读取 `AppLocalLangProvider.current`
- `trn(...)`、`trnI18n(...)` 是非 Compose 方法，会读取 `AppLangState.current.value`

### 切换语言

```kotlin
AppLangState.change(Lang_En)
AppLangState.change(Lang_Zh)
```


## 支持的输入文件格式

插件支持以下多语言文件格式：

- `json`
- `yaml` / `yml`
- `properties`
- `kt`

同一个输入目录内建议只放同一种文件类型，并通过 `fileType` 明确指定。

### JSON 示例

目录：

```text
shared/src/commonMain/i18n/en.json
shared/src/commonMain/i18n/zh.json
```

内容：

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

### YAML 示例

目录：

```text
shared/src/commonMain/i18n-yaml/en.yaml
shared/src/commonMain/i18n-yaml/zh.yaml
```

内容：

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

### Properties 示例

目录：

```text
shared/src/commonMain/i18n-properties/en.properties
shared/src/commonMain/i18n-properties/zh.properties
```

内容：

```properties
app.name=i18nZeal
lang.current=Current Language
lang.system=System
lang.en=English
lang.zh=Chinese
count=Count,{0}
```

说明：

- `.properties` 文件现在按 UTF-8 读取，可以直接写中文，不需要转成 `\uXXXX`。

### Kotlin Map 示例

目录：

```text
shared/src/commonMain/i18n-kt/en.kt
shared/src/commonMain/i18n-kt/zh.kt
```

内容：

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

## 键名规则

嵌套 JSON/YAML 会被拍平成点分格式：

- `app.name` 会生成 `I18nKeys.app_name`