@echo off
SET ${name.uppercase}_HOME=..
java -jar -Djava.library.path=./%${name.uppercase}_HOME%/native-libs/ -Xmx2048M ./%${name.uppercase}_HOME%/lib/${artifactId}.jar