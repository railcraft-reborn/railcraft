# Railcraft for Minecraft 1.19

[![Deploy Nightly](https://github.com/Sm0keySa1m0n/Railcraft/actions/workflows/nightly.yml/badge.svg)](https://github.com/Sm0keySa1m0n/Railcraft/releases/tag/nightly)
[![CI Suite](https://github.com/Sm0keySa1m0n/Railcraft/actions/workflows/master.yml/badge.svg)](https://github.com/Sm0keySa1m0n/Railcraft/actions/workflows/master.yml)

Here you will find the source and issue tracker for the **Official Railcraft Project**.

## What is Railcraft?
Railcraft is a mod written for the hit game [Minecraft](https://minecraft.net/). It is built on top of the [Minecraft Forge](https://github.com/MinecraftForge) API.

It greatly expands and improves the Minecart system in Minecraft. Adding many new blocks, entities, and features. It has been in development since 2012 and contains over 800 class files and hundreds of thousands of lines of code.
The mod was created by the user going by the name **CovertJaguar**.
- **CovertJaguar** is currently in charge of development for Minecraft 1.12 in this [repo](https://github.com/Railcraft/Railcraft).
- This repo is maintained by **Sm0keySa1m0n** and aims to develop for the most recent versions of Minecraft, a version for Minecraft 1.19.x is currently in development.
Some contributors who participated in the development for the most recent versions of Minecraft are:
  - [Edivad99](https://github.com/Edivad99)
  - [LetterN](https://github.com/LetterN)

## Official Links
* The Blog, Forums, and main download page: <https://www.railcraft.info>
* The Wiki: <https://railcraft.info/wiki>
* Discord: [Invite](https://discord.gg/VyaUt2r)

## Issues
Post only confirmed bugs [here](https://github.com/Sm0keySa1m0n/Railcraft/issues).

More information about issue Labels can be found [here](https://github.com/CovertJaguar/Railcraft/wiki/Issue-Labels).

## Requirements
Java 17 and Git is required.

## Contributing
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

Railcraft follows standard Forge conventions for setting up and building a project.

Initial setup from a terminal:

* For Eclipse, run `gradlew genEclipseRuns`
* For IntelliJ, run `gradlew genIntellijRuns`
* For VSCode, run `gradlew genVSCodeRuns`

To build, run:

```sh
gradlew build
```

More information [here](https://mcforge.readthedocs.io/en/1.19.x/gettingstarted/).

## License
Railcraft is licensed under a custom usage license tailored specifically for the project. It can be read [here](https://github.com/Sm0keySa1m0n/Railcraft/blob/1.19.x/LICENSE.md).

**Note: The API is licensed under the MIT license which can be found in `src\api\java\mods\railcraft\api\LICENSE.txt`**

* Key things to keep in mind:
* You may **NOT** create works using the Railcraft code (source or binary) without CovertJaguar's explicit permission except in the cases listed in this license.
* You may **NOT** create derivative Jars from Railcraft source to distribute to other users.
* You **MAY** use snippets of Railcraft Code posted on the Official Github in your own projects, but only if your project consists of less than 25% of Railcraft derived code. You must give credit to the Railcraft Project for the code used, including a link to the Github Project. Put this in your class file headers that contain Railcraft code, in your readme, and on the main download page.
* You may **NOT** use Railcraft Art Assets in other projects **unless** the project is intended to operate alongside Railcraft. Examples are Addons, Resource Packs and InterMod Integration.
* You **MAY** fork and edit the Github Project for the purpose of contributing to the Official Railcraft Project. You may **NOT** distribute any Jar created from a fork for any reason.
* All contributions to the Official Railcraft Project must be covered by a Contributor Licensing Agreement signed by the contributor.
