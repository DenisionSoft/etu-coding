# Elevator simulator

This is an elevator simulator written in Java 19. It features a multi-window GUI and a control system to emulate multiple elevators. A user acts as a 'summoner' of passengers by making calls on different floors, picking destinations and putting an elevator to motion.

It was developed as a coursework for an Object-oriented Programming course in ETU.

## Try It Out
There are two ways to try this software - build it yourself or use a pre-built binary. Both ways are for Windows.

### Use a binary
You can just download a pre-built binary that includes JDK 21 and allows for a 1-click launch. Find `elevator.exe` on the [Releases](https://github.com/DenisionSoft/etu-coding/releases) page. If it doesn't work, please try again with JDK 21 or later installed.

### Build yourself
First, make sure you have JDK 19 or later installed. Then follow these steps:
1. Clone this repository and enter this folder, then `src`.
2. Run `javac -d ./build *.java` to compile classes.
3. Run `cp ../*.png ./build/` to copy assets.
4. Run `cd build` to move to the build directory.
5. Run `jar cfe Main.jar Main *` to build executable.
6. Run `java -jar .\Main.jar` to launch the app.