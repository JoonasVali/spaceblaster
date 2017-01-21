@echo off
SET ${name.uppercase}_HOME=..
java -jar -Djava.library.path=./%${name.uppercase}_HOME%/native-libs/ -Xmx2048M -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 ./%${name.uppercase}_HOME%/lib/${artifactId}.jar