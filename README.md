# Data Serializer

This is a Java app and library to convert files and data to my own serialization format.

## How it was made
### Original project
- The tag system was originally created for another project a year ago, and is based on Minecraft NBT.
  - This only includes the number, string, list/array, and compound tags. The binary tag and wrapper tags are newer and were created as part of this project.
- The Codec system was also part of that project and is based on [Mojang's DataFixerUpper library](https://github.com/Mojang/DataFixerUpper)
  - DataFixerUpper is also a dependency of this project
  - You can use either codec system, but I like mine better, and mine is supposed to be easier to use
- The Entry/Item/Value classes are newer and were originally part of this project
  - The tags were later updated to implement these interfaces
- All other features not mentioned were created for this project

### Libraries
- Apache Commons Lang, Collections, and CLI
- Jackson
- Netty
- Fastutil
- Mojang Brigadier and DataFixerUpper
- My own [Java Utils](https://github.com/SuperYoshi10000/JavaUtils) library, which has also been updated to work with this project

### AI
AI was only used to complete lines I was already typing

## Usage
You will need to [install Java](https://www.oracle.com/java/technologies/downloads/) to use this project (at least Java 25).

To run, use the command:
```sh
java -jar DataSerializer-<version>.jar [options] <command> [<args>]
```
All dependencies are included in the jar file, so you don't need to install them separately.

Commands:
- `help` - Show the help message
- `serialize` - Convert to my serialization format
- `deserialize` - Convert from my serialization format
- `convert` - Convert between two serialization formats, like JSON and YAML

Options:
- `-h`, `--help` - Show the help message
- `-c`, `--compress` - Compress the serialization output using zlib deflate (not necessary for deserialization)
- `-f <format>`, `--format <format>` - Specify the input/output format for serialization/deserialization, if it isn't obvious from the file extension
- `-d`, `--debug` - Show debug information in logs