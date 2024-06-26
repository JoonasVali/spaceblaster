## Introduction: ##

SpaceBlaster is a free, unlicensed game inspired by "Space Invaders," developed in Java. It was created to explore 
and learn more about the LibGDX framework.

![Image 1](https://i.imgur.com/NLSvNB4.jpg)
![Image 2](https://i.imgur.com/V8vM9rk.jpg)

## Demo Video: ##
https://www.youtube.com/watch?v=hDvjagaqedQ


## Contents: ##
The game currently features:

* A modest variety of enemy types with easy future expandability
* A limited selection of weapons obtainable from enemies
* A movement system inspired by Space Invaders
* A life and health system
* Episodes and levels
* Simple and straightforward level editing through text files
* Difficulty settings
* Menus
* Effects created using the LibGDX particle system

The game bundles a Java Runtime Image with it on build, meaning the end user doesn't
need to have Java installed on their machine, as long as they get the distribution for 
the correct operating system.

## How to build: ##
You need maven and java 12 JDK installed on your machine.
Build the game using maven:

For windows version: `mvn clean package -Pwin`

For unix/mac version: `mvn clean package -Punix`

The application will be ready to distribute under {project.folder}/desktop/target/dist

### Building cross-platform ###
To build Unix version on Windows, or vice versa, you need to have toolchain configured
on your machine for the target version. https://maven.apache.org/guides/mini/guide-using-toolchains.html 
and the necessary JDK present in your computer.
So, when you configure the toolchain you pass the necessary values to maven build with the `dist.jdk` VM argument 
```
-Ddist.jdk=key1=value1,key2=value2
```

For example if I wanted to build Unix distribution on Windows machine : 
```
mvn clean package -Punix -Ddist.jdk=version=10,vendor=openjdk,platform=linux-x64
```

### Can you exclude the bundled Java Runtime image? ###

Yes, just remove or comment out the `create-runtime-image` execution from pom.xml in spaceblaster-desktop module.
Then change the `run.sh` and `run.cmd` files under `bin` folder to invoke general `java` instead of java subfolder.
This then works without bundled java, assuming computer knows where to find java. In that case you also don't need 
to have toolchains configured and can create cross-platform version of the game on either OS.

## Event system

As of June 2024, SpaceBlaster includes an event system that allows players to record their gameplay as events saved to 
disk. These events provide an overview and timeline of the played game.

To enable the event system:

Edit the `spaceblaster.yaml` file in the root directory of the built game. Set `eventMode` to `true`.
This will save the gameplay recordings to the `eventlogs` folder.

The event system is meant to be used for experimental augmentation by AI.

## LICENSE: ##

This is free and unencumbered software released into the public domain.

Anyone is free to copy, modify, publish, use, compile, sell, or
distribute this software, either in source code form or as a compiled
binary, for any purpose, commercial or non-commercial, and by any
means.

In jurisdictions that recognize copyright laws, the author or authors
of this software dedicate any and all copyright interest in the
software to the public domain. We make this dedication for the benefit
of the public at large and to the detriment of our heirs and
successors. We intend this dedication to be an overt act of
relinquishment in perpetuity of all present and future rights to this
software under copyright law.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
OTHER DEALINGS IN THE SOFTWARE.

For more information, please refer to http://unlicense.org