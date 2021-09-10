FROM camunda/camunda-bpm-platform:run-7.15.0
MAINTAINER Christian Thomas <christian@ckthomas.de>

# remove demo app
#RUN rm -r camunda/webapps/camunda-invoice

# copy custom bpm-platform.xml
#COPY ./src/docker/config/bpm-platform.xml /camunda/conf/bpm-platform.xml

# copy hassio engine plugin in lib folder (activated over bpm-platform.xml)
#COPY ./target/camunda-homeassist-plugin-1.0.0-SNAPSHOT.jar /camunda/lib/hassio-engine-plugin.jar

# copy serveral 3rd party libs
#COPY ./target/lib/ /camunda/lib/


### DOCU's
# https://docs.camunda.org/manual/7.15/user-guide/spring-boot-integration/configuration/
# https://github.com/camunda-consulting/code/blob/master/snippets/run-engine-plugin/src/main/java/org/company/camunda/MySpringConfig.java
# https://forum.camunda.org/t/extending-camunda-bpm-run-docker-with-custom-listener/24656/8
# https://github.com/rob2universe/bpmrun-add-to-dockerimg
