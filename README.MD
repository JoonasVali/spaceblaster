SpaceBlaster is a free unlicensed "Space Invaders" like game written in Java.

I made it to get to know LibGDX a little better, and I'm hoping there will be at least one person who will benefit from my work. :)

## Demo Video: ##
https://www.youtube.com/watch?v=hDvjagaqedQ

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


Build the game using maven:

For windows version:
  mvn clean package -Pwin

For unix/mac version:
  mvn clean package -Punix

The application will be ready to distribute under {project.folder}/desktop/target/dist


LICENSE:

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