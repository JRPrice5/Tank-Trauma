# Tank-Trauma

## Overview
Tank-Trauma is a **LibGDX-based** top-down tank combat game featuring **physics-based movement**, **bullet ricochet**, and **procedurally generated mazes**.

## Features
- **Physics-Based Tank Movement**: Realistic controls with momentum and rotation.
- **Projectile System**: Bullets with lifespans and physics interactions.
- **Maze Generation**: Randomized battlefield layout using a custom maze generator.
- **Collision Handling**: A robust Box2D physics system for accurate object interactions.
- **Multiple Tank Colors**: Customizable tank appearances.
- **Menu & Settings**: Start, configure, and adjust gameplay settings.

## Installation & Setup

### Prerequisites
- **Java Development Kit (JDK)** (Version 8 or higher)
- **Gradle** (For project building)
- **LibGDX** (Game development framework)

### Cloning the Repository
```sh
 git clone https://github.com/JRPrice5/Tank-Trauma.git
 cd Tank-Trauma
```

### Building the Project
1. **Navigate to the Project Directory**:
   ```sh
   cd Tank-Trauma
   ```
2. **Run Gradle Build**:
   ```sh
   ./gradlew build
   ```
   This will compile the project and handle dependencies.

### Running the Game
After building:
- **Desktop**: Execute the generated JAR file located in `desktop/build/libs/`.
  ```sh
  java -jar desktop-1.0.jar
  ```
- **Android**: Deploy the APK found in `android/build/outputs/apk/` to an emulator or device.

## Gameplay & Controls

### Controls
- **Tank 1 (Player 1)**:
  - **Move**: `WASD`
  - **Fire**: `Spacebar`
  - **Rotate turret**: `Mouse`
  
- **Tank 2 (Player 2, if applicable)**:
  - **Move**: `Arrow Keys`
  - **Fire**: `Right Control / Right Alt`
  - **Rotate turret**: `Mouse`

- **Game Controls**:
  - **Pause**: `Escape`
  - **Navigate Menu**: `Mouse`

### Game Modes
- **Multiplayer (Local)**: Compete against another player.

## Code Structure

### Key Classes
- **`Tank.java`** - Handles tank movement, shooting, and physics.
- **`Bullet.java`** - Defines projectile behavior and interactions.
- **`Projectile.java`** - Abstract base class for all projectiles.
- **`GameScreen.java`** - Main game logic, rendering, and physics updates.
- **`MenuScreen.java`** - Main menu UI and navigation.
- **`SettingsScreen.java`** - Configurable game options.
- **`CollisionListener.java`** - Detects and handles collisions between game objects.
- **`MazeGenerator.java`** - Generates randomized maps for battles.
- **`MazeHitboxParser.java`** - Processes maze data into Box2D hitboxes.
- **`TankTrauma.java`** - Main game entry point and state management.

## Contribution Guidelines
We welcome contributions! Please:
1. **Fork the Repository**.
2. **Create a New Branch**: `git checkout -b feature-name`.
3. **Commit Your Changes**: `git commit -m 'Description of feature'`.
4. **Push to Branch**: `git push origin feature-name`.
5. **Submit a Pull Request**.

## License
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## Acknowledgments
- **LibGDX**: For providing a robust game development framework.
- **Contributors**: Thanks to all who have contributed to this project.
