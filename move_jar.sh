#!/bin/bash

# Move JAR files with "galactic_messenger_name" in their name from subproject directories to the main directory
for jar_file in $(find . -name "galactic_messenger_*.jar" -type f); do
  destination="./$(basename $jar_file)"
  
  if [ -f "$destination" ]; then
    echo "File $destination already exists. Deleting it..."
    rm "$destination"
  fi
  
  if [ -f "$jar_file" ]; then
    mv "$jar_file" .
  else
    echo "File $jar_file does not exist."
  fi
done
