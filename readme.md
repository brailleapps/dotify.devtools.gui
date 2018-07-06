[![Build Status](https://travis-ci.org/brailleapps/dotify.devtools.gui.svg?branch=master)](https://travis-ci.org/brailleapps/dotify.devtools.gui)
[![Type](https://img.shields.io/badge/type-application-blue.svg)](https://github.com/brailleapps/wiki/wiki/Types)
[![License: LGPL v2.1](https://img.shields.io/badge/License-LGPL%20v2%2E1%20%28or%20later%29-blue.svg)](https://www.gnu.org/licenses/lgpl-2.1)

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

## Requirements & Compatibility ##
- Requires JDK 8
- Compatible with SPI and OSGi

## More information ##
See the [common wiki](https://github.com/brailleapps/wiki/wiki) for more information.
