@echo off
call mvn clean package
call docker build -t de.rieckpil.sample/sample-java-ee-and-microprofile-project .
call docker rm -f sample-java-ee-and-microprofile-project
call docker run -d -p 9080:9080 -p 9443:9443 --name sample-java-ee-and-microprofile-project de.rieckpil.sample/sample-java-ee-and-microprofile-project