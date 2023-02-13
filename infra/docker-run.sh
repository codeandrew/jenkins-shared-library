#!/bin/bash

docker rm jenkins-blueocean
docker rm jenkins-docker

docker network create jenkins

mkdir -p $HOME/docker_volumes/jenkins/cert
docker volume create --name jenkins-docker-certs -d local -o mountpoint=$HOME/docker_volumes/jenkins/cert

mkdir -p $HOME/docker_volumes/jenkins/data
docker volume create --name jenkins-data -d local -o type=none -o o=bind -o device=$HOME/docker_volumes/jenkins/data

docker run --name jenkins-docker --rm --detach \
  --privileged --network jenkins --network-alias docker \
  --env DOCKER_TLS_CERTDIR=/certs \
  -v /var/run/docker.sock:/var/run/docker.sock \
  --volume jenkins-docker-certs:/certs/client \
  --volume jenkins-data:/var/jenkins_home \
  --publish 2376:2376 \
  docker:dind --storage-driver overlay2


docker build -t myjenkins-blueocean:2.375.2-1 .
docker run --name jenkins-blueocean --restart=on-failure --detach \
  --network jenkins --env DOCKER_HOST=tcp://docker:2376 \
  -v /var/run/docker.sock:/var/run/docker.sock \
  --env DOCKER_CERT_PATH=/certs/client --env DOCKER_TLS_VERIFY=1 \
  --publish 8080:8080 --publish 50000:50000 \
  --volume jenkins-data:/var/jenkins_home \
  --volume jenkins-docker-certs:/certs/client:ro \
  myjenkins-blueocean:2.375.2-1

echo ================================
echo "[+] Jenkins Initial Password "
docker exec jenkins-blueocean cat /var/jenkins_home/secrets/initialAdminPassword
echo ================================


docker ps 
echo "Go to http://localhost:8080"