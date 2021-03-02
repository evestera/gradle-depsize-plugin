# Gradle Dependency Size Plugin

Gradle plugin that adds a task "depsize" which calculates and shows dependency sizes

## Installation

See [plugin page on plugins.gradle.org][plugin-page].

[plugin-page]: https://plugins.gradle.org/plugin/com.github.evestera.depsize

## Usage

```
gradle depsize [--configuration <configurationName>] [--dependency <dependencyName>]
```

Task help:

```
gradle help --task depsize
```

### Example

Example output from usage on project created with `gradle init --type kotlin-application`:

```
$ gradle depsize

> Task :app:depsize

Configuration name: "runtimeClasspath"
Total dependencies size:                                               4.56 MB

guava-29.0-jre.jar                                                 2,726.82 KB
kotlin-stdlib-1.4.20.jar                                           1,453.85 KB
checker-qual-2.11.1.jar                                              196.40 KB
kotlin-stdlib-common-1.4.20.jar                                      187.00 KB
kotlin-stdlib-jdk7-1.4.20.jar                                         21.83 KB
jsr305-3.0.2.jar                                                      19.47 KB
annotations-13.0.jar                                                  17.13 KB
kotlin-stdlib-jdk8-1.4.20.jar                                         15.85 KB
error_prone_annotations-2.3.4.jar                                     13.55 KB
j2objc-annotations-1.3.jar                                             8.58 KB
failureaccess-1.0.1.jar                                                4.51 KB
listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar         2.15 KB
```

### Filtering

Use dependency name without group.

E.g. showing only `guava` from the above
(from `implementation("com.google.guava:guava:29.0-jre")` in the `build.gradle.kts`)

```
$ gradle depsize --dependency guava

> Task :app:depsize

Configuration name: "runtimeClasspath"
Showing only guava (with children)

Total dependencies size:                                               2.90 MB

guava-29.0-jre.jar                                                 2,726.82 KB
checker-qual-2.11.1.jar                                              196.40 KB
jsr305-3.0.2.jar                                                      19.47 KB
error_prone_annotations-2.3.4.jar                                     13.55 KB
j2objc-annotations-1.3.jar                                             8.58 KB
failureaccess-1.0.1.jar                                                4.51 KB
listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar         2.15 KB
```

## Attribution

Initial code based on [this gist][gist] by [medvedev](https://github.com/medvedev)

[gist]: https://gist.github.com/medvedev/968119d7786966d9ed4442ae17aca279

## License

This project is licensed under the Apache License, Version 2.0
