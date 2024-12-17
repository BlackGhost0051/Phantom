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

```
[CellTowerTask.java](app%2Fsrc%2Fmain%2Fjava%2Fcom%2Fblackghost%2Fphantom%2FClass%2FCellTowerTask.java)
[SearchCellTowerTask.java](app%2Fsrc%2Fmain%2Fjava%2Fcom%2Fblackghost%2Fphantom%2FClass%2FSearchCellTowerTask.java)
[CellTowerFragment.java](app%2Fsrc%2Fmain%2Fjava%2Fcom%2Fblackghost%2Fphantom%2FFragments%2FCellTowerFragment.java)
[CellTowersMapFragment.java](app%2Fsrc%2Fmain%2Fjava%2Fcom%2Fblackghost%2Fphantom%2FFragments%2FCellTowersMapFragment.java)
[MainFragment.java](app%2Fsrc%2Fmain%2Fjava%2Fcom%2Fblackghost%2Fphantom%2FFragments%2FMainFragment.java)
[SettingsFragment.java](app%2Fsrc%2Fmain%2Fjava%2Fcom%2Fblackghost%2Fphantom%2FFragments%2FSettingsFragment.java)
[CellTowerInterface.java](app%2Fsrc%2Fmain%2Fjava%2Fcom%2Fblackghost%2Fphantom%2FInterfaces%2FCellTowerInterface.java)
[SearchCellTowerInterface.java](app%2Fsrc%2Fmain%2Fjava%2Fcom%2Fblackghost%2Fphantom%2FInterfaces%2FSearchCellTowerInterface.java)
[DataBaseManager.java](app%2Fsrc%2Fmain%2Fjava%2Fcom%2Fblackghost%2Fphantom%2FManagers%2FDataBaseManager.java)
[PhoneStateManager.java](app%2Fsrc%2Fmain%2Fjava%2Fcom%2Fblackghost%2Fphantom%2FManagers%2FPhoneStateManager.java)
[SettingsManager.java](app%2Fsrc%2Fmain%2Fjava%2Fcom%2Fblackghost%2Fphantom%2FManagers%2FSettingsManager.java)
[MainActivity.java](app%2Fsrc%2Fmain%2Fjava%2Fcom%2Fblackghost%2Fphantom%2FMainActivity.java)
```

### Cell tower info
```
    type, 	mcc, 	mnc, 	lac, 	cell_id	lan     			lon			        range	samples
```
### Cell tower database

```
SVG

# data manager | online connect and update data base | need API key !!!
# Afk chaker
# Mod settings
```

## Fragments

### Cell Towers Map

The `CellTowersMapFragment` displays an interactive map, allowing users to view and interact with cell tower markers. It integrates OpenStreetMap for map functionality and displays real-time cell tower data.

### Cell Tower

The `CellTowerFragment` is responsible for displaying details about a specific cell tower, including its ID, signal strength, operator, and additional information. This fragment retrieves cell tower details using the `TelephonyManager`.

### Settings

The `SettingsFragment` is a preference-based fragment that enables users to configure app settings related to map behavior and UI preferences.

Status : `Development`
