#!/bin/bash

# Script to initialise project by executing steps as follows:
#   - Replace port number
#   - Replace package `spring-boot-app-template`
#   - Replace app name from `spring-boot-app-template`
#   - Clean-up README file from template related info
#   - Self-destruct

read -p "Port number for new app (press Enter for default 8080): " port
if [ -z "$port" ]
then
      port="8080"
fi

read -p "Replace application name with: " component_name
read -p "Replace \`spring-boot-app-template\` package name with: " package

pushd "$(dirname "$0")"/.. > /dev/null || exit 1

declare -a files_with_port=(
  "README.md"
  "./spring-boot-app-template/Dockerfile"
  "./spring-boot-app-template/src/main/resources/application.yml"
)

declare -a files_with_slug=(
  "build.gradle.kts"
  "settings.gradle.kts"
  "README.md"
  "docker-compose.yml"
  "./buildSrc/src/main/groovy/spring-boot-app-template.java-conventions.gradle"
  "./spring-boot-app-template/build.gradle.kts"
  "./spring-boot-app-template/Dockerfile"
  "./spring-boot-app-template/src/main/resources/application.yml"
  "./spring-boot-app-template/src/main/kotlin/com/yonatankarp/springbootapptemplate/controllers/RootController.kt"
  "./.github/workflows/ci.yml"
  "./.github/workflows/linting.yml"
)

declare -a subdirectories_to_rename_package=(
  "main"
  "test"
)

declare -a headers_to_delete=(
  "Purpose"
  "What's inside"
  "Setup"
)


# Replace port number
for i in "${files_with_port[@]}"
do
  perl -i -pe "s/8080/$port/g" "${i}"
done

# Replace spring-boot-template slug
for i in "${files_with_slug[@]}"
do
  perl -i -pe "s/spring-boot-app-template/$component_name/g" "${i}"
done

# Replace demo package in all files under ./src
find ./spring-boot-app-template/src -type f -print0 | xargs -0 perl -i -pe "s/karp.springbootapptemplate/karp.$package/g"
perl -i -pe "s/karp.spring-boot-app-template/karp.$package/g" build.gradle.kts

# Rename directory to provided package name

rm -rf "${component_name}"

for dir in "${subdirectories_to_rename_package[@]}"
do
  git mv "./spring-boot-app-template/src/${dir}/kotlin/com/yonatankarp/springbootapptemplate" "./spring-boot-app-template/src/${dir}/kotlin/com/yonatankarp/${package}"
done

git mv "./spring-boot-app-template" "${component_name}"

# Rename buildSrc files

find . -type f -name "spring-boot-app-template*" |
sed 's/\(.*\)\(spring-boot-app-template\)\(.*\)/git mv "\1\2\3" "\1'"${component_name}"'\3"/g' |
sh

# Clean-up README file
for i in "${headers_to_delete[@]}"
do
  perl -0777 -i -p0e "s/## $i.+?\n(## )/\$1/s" README.md
done

# Rename title to slug
perl -i -pe "s/.*\n/# $component_name\n/g if 1 .. 1" README.md

# Self-destruct
echo  "Self destroy in 3... 2... 1..."
rm bin

# Return to original directory
popd > /dev/null || exit 1
