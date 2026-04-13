plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "taskflow-rohit"

include(":app")

// Shared Layers
include(":common:data")
include(":common:domain")

// Auth Feature Layers
include(":auth:data")
include(":auth:domain")
include(":auth:service")

// Projects Feature Layers
include(":projects:data")
include(":projects:domain")
include(":projects:service")

// Tasks Feature Layers
include(":tasks:data")
include(":tasks:domain")
include(":tasks:service")