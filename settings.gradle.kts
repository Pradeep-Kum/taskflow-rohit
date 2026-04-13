plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "taskflow-rohit"

include(":app")
include(":common-data")
include(":common-domain")
include(":auth-data")
include(":auth-domain")
include(":auth-service")
include(":projects-data")
include(":projects-domain")
include(":projects-service")
include(":tasks-data")
include(":tasks-domain")
include(":tasks-service")

project(":common-data").projectDir = file("common/data")
project(":common-domain").projectDir = file("common/domain")
project(":auth-data").projectDir = file("auth/data")
project(":auth-domain").projectDir = file("auth/domain")
project(":auth-service").projectDir = file("auth/service")
project(":projects-data").projectDir = file("projects/data")
project(":projects-domain").projectDir = file("projects/domain")
project(":projects-service").projectDir = file("projects/service")
project(":tasks-data").projectDir = file("tasks/data")
project(":tasks-domain").projectDir = file("tasks/domain")
project(":tasks-service").projectDir = file("tasks/service")
