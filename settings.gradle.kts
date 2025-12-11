pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = uri("https://www.jitpack.io"))
    }
}

rootProject.name = "Lumber-Universal"
include(":app")

// Apply the React Native config from the correct path inside the PokeExplorer project
apply(from = file("PokeExplorer/node_modules/@react-native-community/cli-platform-android/native_modules.gradle"))
applyNativeModulesSettings(settings)
