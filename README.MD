## Introduction: ##
SpaceBlaster is a free unlicensed "Space Invaders" like game written in Java.

I made it to get to know LibGDX a little better, and I'm hoping there will be at least one person who will benefit from my work. 

## Demo Video: ##
https://www.youtube.com/watch?v=hDvjagaqedQ


## Contents: ##
Currently the game contains:

* Small range of enemy types with capability of adding more in the future without hassle
* Small selection of weapons to get from enemies
* Space Invader's like movement system
* Life and health system
* Episodes and levels
* Simplest and the most straight forward level editing capability (in text file)
* Difficulty choice
* Menus
* LibGDX particle system implementation for effects

The game bundles a Java Runtime Image with it on build, meaning the end user doesn't
need to have Java installed on their machine, as long as they get the distribution for 
the correct operating system.

## How to build: ##
You need maven and java 10 JDK installed on your machine.
Build the game using maven:

For windows version:
  `mvn clean package -Pwin`

For unix/mac version:
  `mvn clean package -Punix`

The application will be ready to distribute under {project.folder}/desktop/target/dist

### Building cross-platform ###
To build Unix version on Windows, or vice versa, you need to have toolchain configured
on your machine for the target version. https://maven.apache.org/guides/mini/guide-using-toolchains.html 
and the necessary JDK present in your computer.
So, when you configure the toolchain you pass the necessary values to maven build with the `dist.jdk` VM argument 

    `-Ddist.jdk=key1=value1,key2=value2`

For example if I wanted to build Unix distribution on Windows machine : 
    `mvn clean package -Punix -Ddist.jdk=version=10,vendor=openjdk,platform=linux-x64`                

### Can you exclude the bundled Java Runtime image? ###

Yes, just remove or comment out the `create-runtime-image` execution from pom.xml in spaceblaster-desktop module.
Then change the `run.sh` and `run.cmd` files under `bin` folder to invoke general `java` instead of java subfolder.
This then works without bundled java, assuming computer knows where to find java. In that case you also don't need 
to have toolchains configured and can create cross-platform version of the game on either OS.

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