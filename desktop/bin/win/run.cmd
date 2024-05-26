@echo off
SET ${name.uppercase}_HOME=.
"jre/bin/java.exe" -jar -Djava.library.path=%${name.uppercase}_HOME%/native-libs/ -Xmx1024M %${name.uppercase}_HOME%/lib/${project.artifactId}.jar