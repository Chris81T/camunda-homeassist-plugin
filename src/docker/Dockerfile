FROM camunda/camunda-bpm-platform:tomcat-latest
MAINTAINER Christian Thomas <christian@ckthomas.de>

# remove demo app
RUN rm -r webapps/camunda-invoice

# copy custom bpm-platform.xml & co
COPY ./config/bpm-platform.xml /camunda/conf/bpm-platform.xml

# copy hassio engine plugin in lib folder (activated over bpm-platform.xml)
COPY ./camunda-homeassist-plugin-1.0.0-SNAPSHOT.jar /camunda/lib/hassio-engine-plugin.jar