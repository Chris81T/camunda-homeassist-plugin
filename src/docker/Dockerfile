FROM camunda/camunda-bpm-platform:tomcat-7.15.0
MAINTAINER Christian Thomas <christian@ckthomas.de>

# remove demo app
RUN rm -r webapps/camunda-invoice

# copy custom bpm-platform.xml
COPY ./src/docker/config/bpm-platform.xml /camunda/conf/bpm-platform.xml

# copy hassio engine plugin in lib folder (activated over bpm-platform.xml)
COPY ./target/camunda-homeassist-plugin-1.0.0-SNAPSHOT.jar /camunda/lib/hassio-engine-plugin.jar

# copy serveral 3rd party libs
COPY ./target/lib/ /camunda/lib/