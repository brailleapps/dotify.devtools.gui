[![Build Status](https://travis-ci.org/brailleapps/dotify.devtools.gui.svg?branch=master)](https://travis-ci.org/brailleapps/dotify.devtools.gui)
[![Type](https://img.shields.io/badge/type-application-blue.svg)](https://github.com/brailleapps/wiki/wiki/Types)

# dotify.devtools.gui #
The dotify.devtools.gui project in the code repository contains a GUI for testing various Dotify components in both an OSGi and an SPI environment.

## Main Features ##
This tool allows a user to
  * translate a text to braille interactively
  * get unicode code points for a string of characters or vice versa
  * convert braille p-notation into the corresponding unicode braille pattern (block 0x2800)

## Using ##
Run the application (in OSGi mode) with

`gradlew run` (Windows) or `./gradlew run` (Mac/Linux)

The application is started (in SPI mode) from the class

`org.daisy.dotify.devtools.gui.Main` 

## Building ##
Build with `gradlew build` (Windows) or `./gradlew build` (Mac/Linux)

## More information ##
See the [common wiki](https://github.com/brailleapps/wiki/wiki) for more information.
