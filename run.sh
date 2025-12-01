#!/bin/bash
cd "$(dirname "$0")"
mvn clean compile
java -cp "/usr/share/openjfx/lib/*:target/classes" com.dungeon.Main
