# Inkball Game

A Java-based interactive game developed using the Processing library and Gradle as the dependency manager. This project is a university assignment for **INFO1113 / COMP9003**, where players direct colored balls into matching holes while avoiding penalties for mismatches.

## Table of Contents
- [Description](#description)
- [Gameplay](#gameplay)
- [Features](#features)
- [Setup](#setup)
- [Controls](#controls)

## Description

The game involves directing balls of various colors into their matching holes. Players can draw lines to reflect and guide balls across the game board. Points are awarded for successful matches, while mismatches deduct points. The game ends when all balls are captured correctly or the timer runs out.

## Gameplay

### Entities
1. **Balls**: Spawn with random velocities and need to be directed into matching holes.
2. **Walls**: Reflect balls and may change their colors.
3. **Holes**: Capture balls of matching colors.
4. **Player-Drawn Lines**: Temporary lines to guide balls.

### Levels
Each level is loaded from a configuration file, determining:
- Wall positions.
- Spawner and hole locations.
- Ball colors and spawn timing.

## Features
- Random ball spawning and trajectories.
- Collision mechanics for walls, lines, and balls.
- Timer-based levels with score tracking.
- Gradle-based build and dependency management.
- Object-oriented design for easy extension.

## Setup

### Prerequisites
- Java 8 or higher.
- Gradle installed.
- Clone the repository:
  ```bash
  git clone https://github.com/<your-username>/<repository-name>.git
  ```

### Run the Game
1. Navigate to the project directory:
   ```bash
   cd <repository-name>
   ```
2. Build and run the game:
   ```bash
   gradle run
   ```

### Testing
Run unit tests to ensure functionality:
```bash
gradle test
```

## Controls
- **Mouse**: Draw lines with left click; remove lines with right click.
- **'r' Key**: Restart the current level or game.
- **Spacebar**: Pause or unpause the game.

## Extension

### Implemented Extension
A unique feature has been added: **[Custom Extension Name]**. This extension involves [brief description of the feature, such as "one-way walls" or "timed tiles that become transparent over time"].
