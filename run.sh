# 本地测试
./gradlew :i18n-runtime:publishToMavenLocal
cd i18n-gradle-plugin
../gradlew publishToMavenLocal

./gradlew publishAllToMavenCentral --no-configuration-cache


ls ~/.m2/repository/io/github/xczcdjx