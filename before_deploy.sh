#!/usr/bin/env bash

if [ -z "${TRAVIS_TAG}" ];
then
  echo "[INFO] This is not tagged build. Skipping decryption of 'codesigning.asc.enc'.";
elif [ "$TRAVIS_PULL_REQUEST" != "false" ];
then
  echo "This is pull request. Skipping decryption of 'codesigning.asc.enc'."
else
	echo "[INFO] Decrypting and importing 'codesigning.asc.enc'..."
  openssl aes-256-cbc -K $encrypted_da13544aea09_key -iv $encrypted_da13544aea09_iv -in codesigning.asc.enc -out codesigning.asc -d && \
	gpg --fast-import codesigning.asc;
fi
