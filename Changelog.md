# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/), and this project follows to [Ragnarök Versioning Convention](https://shor.cz/ragnarok_versioning_convention).

## Re-Crystallized Wing Version 1.2 Changelog - 2023-09-11

### Added

- Added `sir_squidly` to the credits

### Changed

- Update the mod description in game
- Changed default durability of the crystal wing from `16` to `32`
- Changed default durability of the burnt wing from `8` to `16`
- Changed default durability of the ender scepter from `64` to `128`

### Fixed

- Fixed Re-Crystallized Wing using its id instead of its name for logging
- Fixed items using a deprecated way of declaring their rarity
- Fixed items not changing rarity when enchanted
- Fixed logo not showing up in game

### Optimized

- As a result of a general cleanup Re-Crystallized Wing should be faster and use slightly less ram

### Internal

- Changed the package to the new domain
- Stopped including Jafama
- Renamed config fields to follow camelCase
- Renamed constants to follow TALL_SNAKE_CASE
- Organized lang entries
- Updated to Gradle 8.3
- Updated the build script to follow the MMT
- General Cleanup
- Changed license to MIT

## Re-Crystallized Wing Version 1.1 Changelog - 2023-6-13

### Changed

- `Ender Scepter Reach Mutliplier` config got changed to `Ender Scepter Reach` and is now in blocks
- Added `DaftPVF` to the credits as they where the original creator of the mod in Minecraft 1.2.5

### Fixed

- Fixed Ender Scepter creative reach multiplier always being applied
- Fixed Ender Scepter using the Crystal Wing configuration for durability


### Internal

- Switched to [Raven] amazing [RetroFuturaGradle]
- Updated to Gradle 8.1.1

[Raven]: https://github.com/eigenraven
[RetroFuturaGradle]: https://github.com/GTNewHorizons/RetroFuturaGradle
