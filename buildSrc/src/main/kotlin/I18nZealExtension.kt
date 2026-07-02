import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.api.provider.ListProperty
import org.gradle.kotlin.dsl.property

enum class I18nFileType {
    JSON,
    YAML,
    KT,
    PROPERTIES
}

open class I18nZealExtension(project: Project) {

    /**
     * 文件类型（默认 JSON）
     */
    var fileType: I18nFileType = I18nFileType.JSON

    /**
     * 支持的语言列表
     * 例如: ["en", "zh"]
     */
    var sourceLocales: List<String> = emptyList()

    /**
     * 输入目录
     */
    var inputDir: String = "src/commonMain/i18n"

    /**
     * 输出目录
     */
    var outputDir: String =
        "generated/i18nzeal/commonMain/kotlin"

    /**
     * 生成包名
     */
    var packageName: String? = null

    /**
     * 生成对象名
     */
    var objectName: String = "I18nZeal"
}