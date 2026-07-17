# Data Serializer

## How it was made (I forgot to commit earlier)
- The tag system was originally created for another project a year ago, and is based on Minecraft NBT.
  - This only includes the number, string, list/array, and compound tags. The binary tag and wrapper tags are newer and were created as part of this project.
- The Codec system was also part of that project and is based on [Mojang's DataFixerUpper library](https://github.com/Mojang/DataFixerUpper)
  - DataFixerUpper is also a dependency of this project
  - You can use either codec system, but I like mine better, and mine is supposed to be easier to use
- The Entry/Item/Value classes are newer and were originally part of this project
  - The tags were later updated to implement these interfaces

## Usage
You will need to [install Java](https://www.oracle.com/java/technologies/downloads/) to use this project (at least Java 25)

