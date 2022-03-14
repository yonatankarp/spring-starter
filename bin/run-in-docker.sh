#!/usr/bin/env sh

print_help() {
  echo "Script to run docker containers for Spring Boot App Template API service

  Usage:

  ./run-in-docker.sh [OPTIONS]

  Options:
    --clean, -c                   Clean and install current state of source code
    --install, -i                 Install current state of source code
    --help, -h                    Print this help block
  "
}

# script execution flags
GRADLE_CLEAN=false
GRADLE_INSTALL=false

execute_script() {
  cd "$(dirname "$0")/.."  || exit 1

  if [ ${GRADLE_CLEAN} = true ]
  then
    echo "Clearing previous build.."
    ./gradlew clean
  fi

  if [ ${GRADLE_INSTALL} = true ]
  then
    echo "Assembling distribution.."
    ./gradlew assemble
  fi

  echo "Bringing up docker containers.."

  docker-compose up
}

while true ; do
  case "$1" in
    -h|--help) print_help ; shift ; break ;;
    -c|--clean) GRADLE_CLEAN=true ; GRADLE_INSTALL=true ; shift ;;
    -i|--install) GRADLE_INSTALL=true ; shift ;;
    *) execute_script ; break ;;
  esac
done
