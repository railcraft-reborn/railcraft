# Contributing
Regarding new features/behavior changes, please submit a Suggestion Issue to the Tracker before you write a single line of code. Keeping everyone on the same page saves time and effort and reduces negative experiences all around when a change turns out to be controversial.

Please adhere to the following guidlines when submitting pull requests:

* Use the [Google style guide](https://github.com/google/styleguide)
* Add `@Override` annotations where appropriate to make overrides explicitly clear
* Rename semi-obfuscated variables (e.g. p_77624_1_) to meaningful names
* Rename Parchment parameter names by removing lowercase p (`pIsMoving` -> `isMoving`)
* Turn on compiler warnings; resolve all of them (raw types, resource leaks, unused imports, unused variables etc.)
* Always use `this` keyword to make code as clear/readable as possible and to avoid ambiguous naming conflicts (which can go without notice)
* Always use curly braces `{}` around if statements to make them clearer and easily expandable, e.g.

```java
if(foo) {
  bar();
}
```

instead of

```java
if(foo)
  bar();
```

* Names of fields being used as constants should be all upper-case, with underscores separating words. The following are considered to be constants:

1. All `static final` primitive types (Remember that all interface fields are inherently static final).
2. All `static final` object reference types that are never followed by "`.`" (dot).
3. All `static final` arrays that are never followed by "`[`" (opening square bracket).

Examples of constants:
`MIN_VALUE, MAX_BUFFER_SIZE, OPTIONS_FILE_NAME`

Examples of non-constants:
`logger, clientConfig`

## Building

Requirements:
- Java 17
- Git

Railcraft follows standard Forge conventions for setting up and building a project.

Initial setup from a terminal:

* For Eclipse, run `gradlew genEclipseRuns`
* For IntelliJ, run `gradlew genIntellijRuns`
* For VSCode, run `gradlew genVSCodeRuns`

To build, run:

```sh
gradlew build
```

More information [here](https://docs.minecraftforge.net/en/1.20.x/gettingstarted/).
