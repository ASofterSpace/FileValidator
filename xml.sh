#!/bin/bash

java -classpath "/cyber/prog/asofterspace/FileValidator/bin" -Xms16m -Xmx1024m com.asofterspace.fileValidator.FileValidator --xml "$@"
