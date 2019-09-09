#!/bin/sh
mvn clean package && docker build -t de.rieckpil.sample/sample-java-ee-and-microprofile-project .
docker rm -f sample-java-ee-and-microprofile-project || true && docker run -d -p 9080:9080 -p 9443:9443 --name sample-java-ee-and-microprofile-project de.rieckpil.sample/sample-java-ee-and-microprofile-project