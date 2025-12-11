# PokeExplorer Phase 2 - Geolocation & Hunt Mode Setup Guide

## Overview

Phase 2 will implement geolocation-based Pokémon hunting, location mapping, and spawn logic. This guide prepares the codebase for these features.

---

## Phase 2 Objectives

1. ✅ **Geolocation Service** - GPS-based location tracking
2. ✅ **Hunt Map Screen** - Interactive map with Pokémon spawns
3. ✅ **Spawn Logic** - Biome-based Pokémon generation
4. ✅ **Location Notifications** - Alert users of nearby Pokémon
5. ✅ **Hunt History** - Track discovered locations

---

## Current Setup Status

### ✅ Ready Components

1. **Location Service** - `react-native-geolocation-service` (installed)
2. **Map Component** - `react-native-maps` (installed)
3. **Permissions** - `react-native-permissions` (installed)
4. **Notifications** - Fully configured in Phase 1
5. **Database** - Firebase Realtime Database ready

### ✅ Prepared Screens

- `src/screens/HuntMapScreen.tsx` - Ready for implementation
- Navigation already integrated

### ✅ Helper Services Needed

1. Geolocation service wrapper
2. Biome detection service
3. Spawn engine (location-based Pokémon spawning)
4. Hunt tracking service

---

## Architecture Overview

```
HuntMapScreen
    │
    ├─→ Geolocation Service
    │   ├─ Get current location
    │   ├─ Watch location changes
    │   └─ Calculate distance
    │
    ├─→ Spawn Engine
    │   ├─ Detect biome (urban/rural/forest/water)
    │   ├─ Generate Pokémon based on biome
    │   └─ Calculate spawn radius
    │
    ├─→ Map Component
    │   ├─ Display user location
    │   ├─ Show Pokémon markers
    │   └─ Handle map interactions
    │
    ├─→ Notifications Service
    │   └─ Alert nearby Pokémon
    │
    └─→ Firebase Service
        ├─ Save hunt history
        └─ Sync discovered Pokémon
```

---

## Implementation Roadmap

### Step 1: Geolocation Service (PRIORITY: HIGH)

**File:** `src/services/geolocation.ts`

```typescript
interface Location {
  latitude: number;
  longitude: number;
  accuracy: number;
  timestamp: number;
}

interface GeolocationConfig {
  enableHighAccuracy?: boolean;
  timeout?: number;
  maximumAge?: number;
}

// Functions needed:
export async function getCurrentLocation(config?: GeolocationConfig): Promise<Location>
export function watchLocation(callback: (location: Location) => void, config?: GeolocationConfig): () => void
export function calculateDistance(lat1: number, lon1: number, lat2: number, lon2: number): number
export async function requestLocationPermission(): Promise<boolean>
```

**Key Features:**
- High accuracy location tracking
- Background location updates
- Distance calculation (Haversine formula)
- Permission handling
- Battery optimization

---

### Step 2: Spawn Engine (PRIORITY: HIGH)

**File:** `src/services/spawnEngine.ts`

```typescript
interface BiomeType {
  name: 'urban' | 'suburban' | 'forest' | 'water' | 'mountain' | 'cave';
  spawnRate: number;
  pokemonPool: number[];
}

interface PokemonSpawn {
  pokemonId: number;
  latitude: number;
  longitude: number;
  spawnedAt: number;
  expiresAt: number;
  rarity: 'common' | 'uncommon' | 'rare' | 'legendary';
}

// Functions needed:
export function detectBiome(latitude: number, longitude: number): Promise<BiomeType>
export function generateSpawns(latitude: number, longitude: number, radius: number): PokemonSpawn[]
export function getPokemonByBiome(biome: BiomeType): number[]
export function calculateRarity(pokemonId: number): 'common' | 'uncommon' | 'rare' | 'legendary'
```

**Biome System:**
- Urban: Pigeon, Rattata, Mankey, etc.
- Water: Magikarp, Shellder, Squirtle, etc.
- Forest: Bulbasaur, Caterpie, Weedle, etc.
- Mountain: Geodude, Cloyster, Onix, etc.
- Cave: Zubat, Diglett, Sableye, etc.

---

### Step 3: Enhanced HuntMapScreen (PRIORITY: HIGH)

**File:** `src/screens/HuntMapScreen.tsx`

```typescript
interface HuntScreenState {
  userLocation: Location | null;
  spawns: PokemonSpawn[];
  selectedSpawn: PokemonSpawn | null;
  isHunting: boolean;
  huntStats: {
    found: number;
    timeSpent: number;
    distance: number;
  };
}

// Features needed:
- Real-time map with user location
- Pokémon spawn markers
- Location history trail
- "Catch" button for nearby Pokémon
- Hunt statistics
- Time-based spawn management
- Difficulty settings
```

---

### Step 4: Hunt History Service (PRIORITY: MEDIUM)

**File:** `src/services/huntHistory.ts`

```typescript
interface HuntSession {
  id: string;
  userId: string;
  startTime: number;
  endTime: number;
  duration: number;
  location: { latitude: number; longitude: number };
  pokemonFound: Array<{ pokemonId: number; time: number }>;
  distance: number;
  biomes: string[];
}

// Functions needed:
export async function startHuntSession(userId: string, startLocation: Location): Promise<string>
export async function endHuntSession(sessionId: string): Promise<HuntSession>
export async function saveHuntSession(session: HuntSession): Promise<boolean>
export async function getHuntHistory(userId: string): Promise<HuntSession[]>
export async function getHuntStats(userId: string): Promise<HuntStats>
```

---

### Step 5: Map Features (PRIORITY: MEDIUM)

**Map Features to Implement:**
- User location marker (blue dot)
- Pokémon spawn markers (with icons)
- Hunt radius circle
- Location trail (breadcrumb)
- Zoom controls
- Provider switcher (Google Maps / Mapbox)
- Real-time location updates

---

## Data Flow

```
User Opens HuntMapScreen
    ↓
Request Location Permission
    ↓
Get Current Location (GPS)
    ↓
Detect Biome
    ↓
Generate Pokémon Spawns
    ↓
Display on Map
    ↓
Watch for Location Changes
    ↓
Calculate Distance to Spawns
    ↓
If Near Spawn (< 100m)
    → Show Capture Button
    → Send Notification
    ↓
User Taps Capture
    ↓
Add to Discovered
    ↓
Save Hunt Session
    ↓
Show Celebration Screen
```

---

## Location Permissions

**Android (AndroidManifest.xml):**
```xml
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_BACKGROUND_LOCATION" />
```

**iOS (Info.plist):**
```xml
<key>NSLocationWhenInUseUsageDescription</key>
<string>PokeExplorer needs your location to find nearby Pokémon</string>
<key>NSLocationAlwaysAndWhenInUseUsageDescription</key>
<string>PokeExplorer uses your location for Pokémon hunts</string>
<key>NSLocationAlwaysUsageDescription</key>
<string>PokeExplorer tracks your location during hunts</string>
```

---

## Biome Detection Algorithm

```typescript
function detectBiome(latitude: number, longitude: number): BiomeType {
  // Use Google Maps API or local database for:
  // 1. Land use data
  // 2. Proximity to water bodies
  // 3. Building density
  // 4. Elevation
  
  // Classification:
  if (nearWater && elevation < 100) return WATER_BIOME;
  if (forestCoverage > 70) return FOREST_BIOME;
  if (buildingDensity > 60) return URBAN_BIOME;
  if (elevation > 1000) return MOUNTAIN_BIOME;
  if (darkHours) return CAVE_BIOME;
  return SUBURBAN_BIOME;
}
```

---

## Spawn Mechanics

### Spawn Rate
- Every 30 seconds: 1-3 new Pokémon
- Radius: 500m around user
- Max simultaneous: 10 spawns
- Duration: 5 minutes per spawn

### Rarity Distribution
- Common (60%): Pidgey, Rattata, etc.
- Uncommon (25%): Bulbasaur, Charmander, etc.
- Rare (12%): Dragonite, Gengar, etc.
- Legendary (3%): Mewtwo, Articuno, etc.

### Biome-Specific Spawning
Each biome has its own Pokémon pool with weighted probabilities.

---

## Firebase Integration

**Database Structure:**
```
users/
  {uid}/
    hunts/
      {huntId}/
        startTime: number
        endTime: number
        location: { latitude, longitude }
        pokemonFound: [{ id, time }, ...]
        duration: number
        distance: number

    huntStats/
      totalHunts: number
      totalTime: number
      totalDistance: number
      favoriteBiome: string
```

---

## Testing Checklist

- [ ] Location permission request
- [ ] GPS location updates
- [ ] Biome detection accuracy
- [ ] Spawn generation
- [ ] Map rendering
- [ ] Marker updates
- [ ] Distance calculation
- [ ] Notification alerts
- [ ] Capture functionality
- [ ] Hunt history saving
- [ ] Battery optimization
- [ ] Background mode

---

## Performance Considerations

1. **Location Updates:** 
   - Update every 5 seconds (balance accuracy vs battery)
   - Use `watchPosition` instead of polling

2. **Spawn Generation:**
   - Generate spawns on-demand
   - Limit to 10 simultaneous spawns
   - Cleanup expired spawns

3. **Map Rendering:**
   - Use clustering for many markers
   - Only render visible markers
   - Optimize tile loading

4. **Database:**
   - Batch write operations
   - Use transactions for consistency
   - Implement pagination for history

---

## Known Limitations

1. **Accuracy:** GPS accuracy varies (5-20m depending on location)
2. **Cold Start:** Initial location fix takes 2-5 seconds
3. **Permissions:** Android & iOS have different permission handling
4. **Battery:** Continuous location tracking drains battery (5-10% per hour)
5. **Offline:** Map won't load without internet (cache tiles locally in Phase 3)

---

## Next Phase Preparation

### Phase 3 Will Add:
- Offline map tile caching
- AR overlay for Pokémon
- Camera capture during hunt
- Real-time multiplayer hunts
- Hunt challenges and events

### Phase 4 Will Add:
- Hunt leaderboards
- Shareable hunt routes
- Community hunt events
- Social hunt groups

---

## Dependencies Already Installed

```json
"react-native-geolocation-service": "^5.3.1"
"react-native-maps": "^1.26.20"
"react-native-permissions": "^5.4.4"
```

---

## Quick Start

1. **Create geolocation service** - Copy template from this guide
2. **Create spawn engine** - Implement biome detection
3. **Enhance HuntMapScreen** - Add map and markers
4. **Add hunt tracking** - Store sessions in Firebase
5. **Test on device** - Real GPS and network required

---

## Reference Links

- [React Native Geolocation Service](https://github.com/agontuk/react-native-geolocation-service)
- [React Native Maps](https://github.com/react-native-maps/react-native-maps)
- [Haversine Formula](https://en.wikipedia.org/wiki/Haversine_formula) - Distance calculation
- [Biome Classification](https://en.wikipedia.org/wiki/Biome) - Land use types
- [Google Places API](https://developers.google.com/maps/documentation/places/web-service)

---

## Contact & Support

For questions or issues with Phase 2:
1. Check API_REFERENCE.md
2. Review code comments
3. Check Firebase console logs
4. Test on physical device (not simulator)

---

**Document Version:** 1.0  
**Last Updated:** December 11, 2025  
**Phase:** 2 Preparation  
**Status:** Ready for Implementation ✅

