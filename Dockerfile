FROM open-liberty:kernel-java11
COPY --chown=1001:0  target/sample-java-ee-and-microprofile-project.war /config/dropins/
COPY --chown=1001:0  server.xml /config