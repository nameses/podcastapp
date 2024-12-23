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

rootProject.name = "podcastapp"
include(":app")
include(":features:auth:data")
include(":features:auth:domain")
include(":features:auth:ui")
include(":features:main:data")
include(":features:main:domain")
include(":features:main:ui")
include(":core:network")
include(":core:common")
include(":core:feature_api")
include(":features:profile:data")
include(":features:profile:domain")
include(":features:profile:ui")
include(":core:commonrepos")
include(":core:commonui")
include(":features:podcast_details:data")
include(":features:podcast_details:domain")
include(":features:podcast_details:ui")
