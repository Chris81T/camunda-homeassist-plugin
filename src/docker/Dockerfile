FROM camunda/camunda-bpm-platform:tomcat-latest
MAINTAINER Christian Thomas <christian@ckthomas.de>

# remove demo app
RUN rm -r webapps/camunda-invoice

# the postgres driver is needed
ADD mssql-jdbc-7.2.2.jre8.jar /camunda/lib/

# copy custom bpm-platform.xml
COPY ./src/docker/config/bpm-platform.xml /camunda/conf/bpm-platform.xml

# copy hassio engine plugin in lib folder (activated over bpm-platform.xml)
COPY ./target/camunda-homeassist-plugin-1.0.0-SNAPSHOT.jar /camunda/lib/hassio-engine-plugin.jar