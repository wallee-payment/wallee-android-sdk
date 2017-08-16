#!/usr/bin/env bash

if [ -z "${TRAVIS_TAG}" ];
then
  echo "[INFO] This is not tagged build. Skipping decryption of 'signingkey.gpg.enc'.";
elif [ "$TRAVIS_PULL_REQUEST" != "false" ];
then
  echo "This is pull request. Skipping decryption of 'signingkey.gpg.enc'."
else
  echo "[INFO] Current directory: "
  pwd
  echo "[INFO] The listing of current directory: "
  ls -la; 
  echo "[INFO] Decrypting and importing 'signingkey.gpg.enc'..."
  openssl aes-256-cbc -K $encrypted_da13544aea09_key -iv $encrypted_da13544aea09_iv -in signingkey.gpg.enc -out signingkey.gpg -d && \
  gpg --fast-import signingkey.gpg && \
  gpg --export-secret-keys >${HOME}/.gnupg/secring.gpg;
fi
