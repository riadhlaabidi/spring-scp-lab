#!/bin/bash

rm -rf $(find . -name ".settings" -type d)
rm -rf $(find . -name "bin" -type d)
find . -name ".project" -delete
find . -name ".classpath" -delete
