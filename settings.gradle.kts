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
    }
}

rootProject.name = "Currency Watcher"
include(":app")
include(":common")
include(":core")
include(":domain")
include(":data")
include(":uikit")
include(":feature:exchange")
include(":feature:watcher")
include(":feature:watcher:add_watcher")
include(":feature:watcher:watcher_list")
include(":feature:watcher:watcher_details")
