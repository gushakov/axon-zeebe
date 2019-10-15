#!/bin/bash

# from https://docs.docker.com/develop/develop-images/dockerfile_best-practices/
# install docker entrypoint helper script

# from https://github.com/zeebe-io/zeebe/blob/develop/Dockerfile
# original entrypoint
# ENTRYPOINT ["tini", "--", "/usr/local/bin/startup.sh"]

# from http://bigdatums.net/2017/11/07/how-to-keep-docker-containers-running/
# keep Docker container running

# from https://docs.zeebe.io/introduction/quickstart.html#step-3-deploy-a-workflow
# deploy workflow with zbctl

/usr/local/bin/startup.sh &

sleep 30s

dockerize -wait http://monitor:8080

/zbctl deploy /trip-saga.bpmn &

tail -f /dev/null