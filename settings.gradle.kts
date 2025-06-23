pluginManagement {
    repositories {
     //   mavenLocal()
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
      //  mavenLocal()
        google()
        mavenCentral()
    }
}

rootProject.name = "WeatherAppTask"
include(":app")
include(":data")
include(":core")
include(":features")
include(":features:cityInput")
include(":features:currentWeather")
include(":features:forecast")
include(":weatherutils")
