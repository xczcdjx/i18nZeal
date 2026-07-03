import java.util.Properties

val localPublishSecretsFile = file("../publish-secrets.properties")
if (localPublishSecretsFile.isFile) {
    val localPublishSecrets = Properties()
    localPublishSecretsFile.inputStream().use(localPublishSecrets::load)
    gradle.beforeProject {
        localPublishSecrets.forEach { key, value ->
            val propertyName = key.toString().trim()
            if (propertyName.isNotEmpty() && !extensions.extraProperties.has(propertyName)) {
                extensions.extraProperties.set(propertyName, value.toString())
            }
        }
    }
}

rootProject.name = "i18n-gradle-plugin"
