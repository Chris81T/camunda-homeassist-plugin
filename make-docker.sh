#!/bin/bash

echo "Clean up target/camunda-docker..."
rm -rf target/camunda-docker
mkdir target/camunda-docker

echo "Build camunda-homeassist-automation..."
mvn install -f ./pom.xml

echo "Copy Dockerfile to target/camunda-docker..."
cp ./src/docker/Dockerfile target/camunda-docker/

echo "Build Docker image"
docker build -f ./target/camunda-docker/Dockerfile -t camunda-homeassist-platform .

echo "Export generated Docker image to to target..."
docker save camunda-homeassist-platform:latest > target/camunda-docker/camunda-homeassist-platform.tar

echo "Building docker image camunda-homeassist-platform finished :-)"