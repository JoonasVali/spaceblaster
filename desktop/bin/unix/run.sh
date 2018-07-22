#!/bin/bash
export ${name.uppercase}_HOME=.
java -jar -Djava.library.path=./$${name.uppercase}_HOME/native-libs/ -Xmx1024M ./$${name.uppercase}_HOME/lib/${project.artifactId}.jar
