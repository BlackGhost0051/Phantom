# Phantom

![](./app/src/main/res/mipmap-xxxhdpi/ic_launcher.webp "Phantom")

Phantom is a cutting-edge Android application designed to enhance your mobile security by providing detailed information on nearby cell towers. With Phantom, users can visualize the locations of cell towers on a map, access comprehensive details about each tower, and verify the authenticity of cell towers to protect against potential spoofing attacks.

- Interactive Map: View a dynamic map displaying the positions of all nearby cell towers.
- Detailed Tower Information: Access in-depth information about each cell tower, including its unique ID, signal strength, operator, and more.

Development:
- Spoof Detection: Safeguard your mobile device with our advanced spoof detection feature. Phantom cross-references each cell tower with a trusted database to verify its legitimacy.

# Content
- [Documentation](#documentation)
  - [Structure](#structure)
  - [Fragments](#fragments)
    - [Cell Towers Map](#cell-towers-map)
    - [Cell Tower](#cell-tower)
    - [Settings](#settings)

# Documentation

## Structure

## Fragments

### Cell Towers Map

The `CellTowersMapFragment` displays an interactive map, allowing users to view and interact with cell tower markers. It integrates OpenStreetMap for map functionality and displays real-time cell tower data.

### Cell Tower

The `CellTowerFragment` is responsible for displaying details about a specific cell tower, including its ID, signal strength, operator, and additional information. This fragment retrieves cell tower details using the `TelephonyManager`.

### Settings

The `SettingsFragment` is a preference-based fragment that enables users to configure app settings related to map behavior and UI preferences.

Status : `Development`
