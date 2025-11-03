#!/bin/bash
cd "$(dirname "$0")"
mvn clean compile
java --module-path /usr/share/openjfx/lib --add-modules javafx.controls,javafx.web -cp target/classes com.dungeon.Main

