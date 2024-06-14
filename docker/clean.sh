#!/bin/sh

for i in $(docker ps -aq); do
  docker stop $i
done

for i in $(docker ps -aq); do
  docker rm $i
done

docker network prune -f
docker image prune -f
docker volume prune -f

for i in $(docker volume ls --filter dangling=true -q); do
  docker volume rm $i
done
