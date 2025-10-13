# Play Framework 3.0.5 and Apache Pekko 1.0.3 Upgrade

## Overview

This document describes the upgrade of cert-service from Play Framework 2.7.2 with Akka 2.5.22 to Play Framework 3.0.5 with Apache Pekko 1.0.3.

## Why This Upgrade

1. License Compliance: Akka changed from Apache 2.0 to Business Source License 1.1, requiring commercial licenses for production use. Apache Pekko maintains Apache 2.0 license.
2. Security: Play 2.7.2 and Akka 2.5.22 no longer receive security updates.
3. Modernization: Access to latest features and performance improvements.

## Technology Stack Changes

- Play Framework: 2.7.2 to 3.0.5
- Actor Framework: Akka 2.5.22 to Apache Pekko 1.0.3
- Scala: 2.11.12 to 2.13.12
- Java: 8 to 11
- Guice: 3.0 to 5.1.0
- SLF4J: 1.6.1 to 2.0.9
- Logback: 1.0.7 to 1.4.14
- Jackson: 2.9.9 to 2.14.3
- Netty: 4.1.44 to 4.1.93

## Key Changes

### Dependencies

All Maven POM files updated with new versions. Play Framework groupId changed from com.typesafe.play to org.playframework. Scala library exclusions added to prevent version conflicts between Scala 2.11 and 2.13.

### Source Code

Package name updates across all Java files:
- akka.actor to org.apache.pekko.actor
- akka.pattern to org.apache.pekko.pattern
- akka.routing to org.apache.pekko.routing
- akka.util to org.apache.pekko.util
- akka.testkit to org.apache.pekko.testkit
- play.libs.akka to play.libs.pekko
- scala.compat.java8.FutureConverters to scala.jdk.javaapi.FutureConverters

### Play 3.0 API Updates

- ActorStartModule: Implements play.libs.pekko.PekkoGuiceSupport
- OnRequestHandler: Updated to use Http.Request parameter
- HealthController: Added Http.Request parameter to controller methods
- Routes: Updated to include Http.Request parameter

### Configuration

application.conf files updated with Pekko namespaces:
- akka namespace to pekko namespace
- Actor system configurations migrated
- Serialization bindings updated
- Dispatcher references changed

### Scala Version Conflicts

Added exclusions to prevent Scala 2.11 transitive dependencies:
- Excluded jackson-module-scala_2.11 from cert-processor cloud-store-sdk dependency
- Excluded scala-library and scala-reflect from all-actors and es-utils dependencies
- Explicitly declared scala-library 2.13.12 dependency

## Build Instructions

Build all modules:
```
mvn clean install -Dmaven.test.skip=true
```

Build with test compilation:
```
mvn clean install -DskipTests
```

Create distribution package:
```
cd service
mvn play2:dist
```

## Migration Impact

Business Logic: No changes to business logic or functionality
API Compatibility: Maintained, as Pekko is API-compatible with Akka 2.6
Code Changes: Primarily package name updates from akka to pekko
License: Now compliant with Apache 2.0 throughout the stack

## Testing Recommendations

1. Execute full unit test suite
2. Run integration tests for actor communication
3. Perform regression testing for all features
4. Conduct performance benchmarking
5. Test under production-like load

## Known Issues

Scala 2.11/2.13 Conflict: If you encounter NoClassDefFoundError for scala.collection.GenMap or scala.Serializable, verify dependency tree to ensure no Scala 2.11 artifacts are present. Run mvn dependency:tree and add exclusions for any scala-library or scala-reflect with version 2.11.

Jackson Module: The distribution must include jackson-module-scala_2.13 only. The jackson-module-scala_2.11 dependency is excluded from cert-processor to prevent ServiceLoader conflicts.
