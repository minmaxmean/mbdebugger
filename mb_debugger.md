# Project: GTCEu Multiblock Debugger Plugin

## Overview
A standalone Forge mod designed to facilitate debugging of multiblock structures in GregTech Modern. This mod acts as a plugin/addon and does not require modifications to the core GTCEu codebase.

## Requirements
- **Dependency**: GregTech Modern (GTCEu) must be installed.
- **Development Environment**: Forge Gradle.

## Technical Approach
The mod will implement a "Debugger" item that, when used on a `MultiblockControllerMachine`, retrieves the current `MultiblockState` and reports any detected `PatternError`.

### 1. Implementation Details
- **Event Handling**: Use `PlayerInteractEvent.RightClickBlock` to detect interaction.
- **Machine Detection**: Check if the targeted `BlockEntity` implements `MultiblockControllerMachine`.
- **Error Retrieval**: 
    - Access the `MultiblockState` via the machine.
    - Check `multiblockState.getError()`.
- **User Notification**: 
    - If an error exists, send a `SystemMessage` to the player's chat containing the error description.
    - If no error is found, notify the player that the structure is valid.

### 2. Project Structure
- `src/main/java/com/gregtechceu/gtdebugging/DebuggerMod.java`: Main class for event registration.
- `src/main/java/com/gregtechceu/gtdebugging/items/DebuggerItem.java`: Logic for the interaction.
- `src/main/resources/META-INF/mods.toml`: Mod metadata and dependency declaration.
- `build.gradle`: Configured with GTCEu as a dependency/provided scope.

### 3. Constraints & Limitations
- **Single Error**: To maintain simplicity, the mod will only report the first error found in the `MultiblockState`.
- **Closed System**: No changes will be made to the `com.gregtechceu.gtceu` package.

---
*Plan generated for later execution.*
