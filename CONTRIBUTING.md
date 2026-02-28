# Contributing to Railcraft Reborn

## Requirements

- Java 21 (JDK)
- Git
- Gradle 8.12.1 (included via wrapper, no separate install needed)

## Development Setup

### Windows

```shell
git clone https://github.com/railcraft-reborn/railcraft.git
cd railcraft
gradlew build
```

### Linux / macOS

```shell
git clone https://github.com/railcraft-reborn/railcraft.git
cd railcraft
./gradlew build
```

### Replit

The repo includes a `.replit` configuration file. Java 21 is provided via Nix.
The build workflow runs:

```shell
JAVA_HOME=$(dirname $(dirname $(which java))) ./gradlew build --no-daemon --console=plain
```

## Running the Client

```shell
./gradlew runClient
```

This launches a development Minecraft client with the mod loaded. Useful for
manual testing of in-game features.

## Building the Release JAR

```shell
./gradlew build
```

The mod JAR is produced at:

```
build/libs/railcraft-reborn-<minecraft_version>-<mod_version>.jar
```

Use the JAR **without** `-api` in the name. The `-api` JAR is for other mods
that depend on the Railcraft API at compile time.

To build without the changelog task (useful in CI or shallow clones):

```shell
./gradlew build -PskipChangelog=true
```

## Code Formatting

The project uses [Spotless](https://github.com/diffplug/spotless) for code
formatting. Before submitting a PR, ensure your code passes the format check:

```shell
./gradlew spotlessCheck
```

To auto-fix formatting issues:

```shell
./gradlew spotlessApply
```

## Running Checks

A full check (compile + test + format) is included in the standard build:

```shell
./gradlew build
```

Or run checks individually:

```shell
./gradlew test              # Unit tests
./gradlew spotlessCheck     # Code formatting
```

## Project Structure

```
src/api/java/          Public API (MIT licensed)
src/main/java/         Main mod source
src/main/resources/    Mod resources (assets, data, META-INF)
src/main/templates/    Template files for mod metadata generation
src/generated/resources/  Data generator output
src/test/              Test source and resources
docs/bugs/             Per-bug documentation (BUG-NNNN-*.md)
docs/interop/          NeoForge capability and interop documentation
docs/fluids/           Fluid system and mod interop notes
docs/audit/            Audit findings
```

## PR Workflow

1. Fork the repository and create a branch from `1.21.x`.
2. Make your changes with clear, scoped commits.
3. Run `./gradlew build` to confirm compilation, tests, and formatting pass.
4. Test in-game with `./gradlew runClient` if your change affects gameplay.
5. Open a pull request against `1.21.x` with a description of what changed and
   why.

## Debug Logging

All debug logging must be gated behind `logger.debug()` (SLF4J) so it is
suppressed at the default INFO log level. Do not add `System.out.println` or
unconditional logging for debug purposes.

If adding a config-gated debug flag, document it here and default it to
`false`.
