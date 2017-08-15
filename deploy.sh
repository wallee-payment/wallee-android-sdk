#!/usr/bin/env bash

if [ -z "${TRAVIS_TAG}" ];
then
  echo "[INFO] This is not tagged build. Skipping deployment to maven.";
elif [ "$TRAVIS_PULL_REQUEST" != "false" ];
then
  echo "This is pull request. Skipping deployment to maven."
else
  echo "[INFO] Publishing artifacts to maven."
  ./gradlew sdk:uploadArchives -PnexusUsername="${OSSRH_JIRA_USERNAME}" -PnexusPassword="${OSSRH_JIRA_PASSWORD}" -Psigning.keyId="${GPG_KEY_ID}" -Psigning.password="${GPG_PASSPHRASE}" -Psigning.secretKeyRingFile="${HOME}/wallee-payment/wallee-android-sdk/codesigning.asc" && \
  ./gradlew closeAndReleaseRepository
fi
