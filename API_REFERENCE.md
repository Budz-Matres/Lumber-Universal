# PokeExplorer API Reference

## Overview
Complete API reference for all services used in PokeExplorer application. This document covers TypeScript, JavaScript, Swift, Ruby, and Node.js implementations.

---

## Table of Contents

1. [Notifications Service](#notifications-service)
2. [Firebase Service](#firebase-service)
3. [PokeAPI Service](#pokeapi-service)
4. [Cache Service](#cache-service)
5. [Type Definitions](#type-definitions)
6. [Error Handling](#error-handling)
7. [Examples](#examples)

---

## Notifications Service

### TypeScript/React Native

#### `requestNotificationPermission()`
Requests notification permissions from the user.

```typescript
async function requestNotificationPermission(): Promise<boolean>
```

**Returns:** `Promise<boolean>` - true if permission granted

**Example:**
```typescript
const allowed = await requestNotificationPermission();
if (allowed) {
  console.log('User granted notification permission');
}
```

---

#### `initializeNotifications(config?)`
Initializes the notification service with Firebase and local push notifications.

```typescript
async function initializeNotifications(
  config?: NotificationServiceConfig
): Promise<() => void>
```

**Parameters:**
- `config` (optional): NotificationServiceConfig
  - `requestPermissions`: boolean - Auto-request permissions
  - `onNotificationReceived`: Function - Callback when notification received
  - `onNotificationTapped`: Function - Callback when notification tapped
  - `channelId`: string - Android channel ID

**Returns:** `Promise<() => void>` - Unsubscribe function

**Example:**
```typescript
const unsubscribe = await initializeNotifications({
  requestPermissions: true,
  onNotificationReceived: (notification) => {
    console.log('Notification received:', notification);
  },
  onNotificationTapped: (notification) => {
    console.log('User tapped notification:', notification);
  }
});

// Later, to stop listening
unsubscribe();
```

---

#### `showNotification(title, message, data?)`
Displays a local notification on the device.

```typescript
function showNotification(
  title: string,
  message: string,
  data?: Record<string, any>
): void
```

**Parameters:**
- `title`: string - Notification title
- `message`: string - Notification message body
- `data` (optional): Record<string, any> - Custom data to attach

**Example:**
```typescript
showNotification(
  'Pokemon Found!',
  'A wild Pikachu appeared near you',
  { pokemonId: 25, location: 'park' }
);
```

---

#### `getFCMToken()`
Gets the Firebase Cloud Messaging token for this device.

```typescript
async function getFCMToken(): Promise<string | null>
```

**Returns:** `Promise<string | null>` - FCM token or null if error

**Example:**
```typescript
const token = await getFCMToken();
if (token) {
  // Send token to backend for saving
  await sendTokenToBackend(token);
}
```

---

#### `removeFCMToken()`
Removes the FCM token (useful for logout).

```typescript
async function removeFCMToken(): Promise<NotificationResponse>
```

**Returns:** `Promise<NotificationResponse>`

**Example:**
```typescript
const response = await removeFCMToken();
if (response.success) {
  console.log('Token removed successfully');
}
```

---

#### `subscribeToTopic(topic)`
Subscribes to a topic for topic-based messaging.

```typescript
async function subscribeToTopic(
  topic: string
): Promise<NotificationResponse>
```

**Parameters:**
- `topic`: string - Topic name

**Returns:** `Promise<NotificationResponse>`

**Example:**
```typescript
const response = await subscribeToTopic('fire-type-hunters');
if (response.success) {
  console.log('Subscribed to topic');
}
```

---

#### `unsubscribeFromTopic(topic)`
Unsubscribes from a topic.

```typescript
async function unsubscribeFromTopic(
  topic: string
): Promise<NotificationResponse>
```

**Parameters:**
- `topic`: string - Topic name

**Returns:** `Promise<NotificationResponse>`

---

### JavaScript (React Native)

All TypeScript functions have JavaScript equivalents with `JS` suffix:

```javascript
const { notificationServiceJS } = require('./notifications');

// Usage
const allowed = await notificationServiceJS.requestPermissionJS();
const unsubscribe = await notificationServiceJS.initializeJS({ ... });
notificationServiceJS.showNotificationJS('Title', 'Message', {});
const token = await notificationServiceJS.getFCMTokenJS();
```

---

### Swift (iOS)

```swift
let manager = NotificationManager.shared

// Request permission
manager.requestNotificationPermission { granted in
    if granted {
        print("Permission granted")
    }
}

// Show notification
manager.showNotification(
    title: "Pokemon Found!",
    message: "A Pikachu appeared nearby",
    data: ["pokemonId": 25]
)

// Get FCM token
manager.getFCMToken { token in
    print("Token: \(token ?? "nil")")
}

// Subscribe to topic
manager.subscribeToTopic("fire-type-hunters") { response in
    print("Success: \(response.success)")
}
```

---

### Ruby (Rails)

```ruby
service = NotificationService.new

# Send to single user
response = service.send_notification(
    user_id,
    "Pokemon Found!",
    "A Pikachu appeared",
    { pokemon_id: 25 }
)

# Send to topic
response = service.send_to_topic(
    "fire-type-hunters",
    "Fire Type Alert",
    "Fire pokemon spawning!",
    { event_id: 'fire_spawn_001' }
)

# Subscribe user
response = service.subscribe_to_topic(user_id, "fire-type-hunters")

# Batch send
response = service.send_batch_notifications(
    user_ids,
    "Pokemon Event",
    "New pokemon spawning event!",
    { event_id: 'event_001' }
)
```

---

### Node.js/Express

```javascript
const NotificationService = require('./NotificationService');
const service = new NotificationService(admin);

// Send to single user
const response = await service.sendNotification({
    userId: 123,
    fcmToken: 'token_here',
    title: 'Pokemon Found!',
    message: 'A wild Pokemon appeared',
    data: { pokemonId: 25 }
});

// Send to topic
const response = await service.sendToTopic(
    'fire-type-hunters',
    'Fire Type Event',
    'Fire pokemon spawning!',
    { eventId: 'fire_spawn_001' }
);

// Subscribe to topic
const response = await service.subscribeToTopic(fcmToken, 'topic-name');

// Batch send
const response = await service.sendBatchNotifications(
    userIds,
    'Title',
    'Message',
    { data: 'here' }
);

// Web notification
service.showWebNotification('Title', {
    body: 'Message',
    icon: '/icon.png'
});
```

---

## Firebase Service

### `signUp(email, password, displayName)`
Creates a new user account.

```typescript
async function signUp(
    email: string,
    password: string,
    displayName: string
): Promise<AuthResponse>
```

**Returns:** `Promise<AuthResponse>`
```typescript
{
    success: boolean,
    user?: UserProfile,
    message?: string,
    error?: any
}
```

**Example:**
```typescript
const response = await signUp('user@email.com', 'password123', 'John Doe');
if (response.success) {
    console.log('Account created:', response.user);
}
```

---

### `signIn(email, password)`
Signs in a user.

```typescript
async function signIn(email: string, password: string): Promise<AuthResponse>
```

---

### `signOut()`
Signs out the current user.

```typescript
async function signOut(): Promise<AuthResponse>
```

---

### `resetPassword(email)`
Sends password reset email.

```typescript
async function resetPassword(email: string): Promise<AuthResponse>
```

---

### `getUserProfile(uid)`
Retrieves user profile.

```typescript
async function getUserProfile(uid: string): Promise<UserProfile | null>
```

---

### `updateUserProfile(uid, updates)`
Updates user profile.

```typescript
async function updateUserProfile(
    uid: string,
    updates: Partial<UserProfile>
): Promise<AuthResponse>
```

---

### `addDiscoveredPokemon(uid, pokemonId)`
Adds a Pokémon to discovered list.

```typescript
async function addDiscoveredPokemon(
    uid: string,
    pokemonId: number
): Promise<AuthResponse>
```

---

### `removeDiscoveredPokemon(uid, pokemonId)`
Removes a Pokémon from discovered list.

```typescript
async function removeDiscoveredPokemon(
    uid: string,
    pokemonId: number
): Promise<AuthResponse>
```

---

### `isDiscovered(uid, pokemonId)`
Checks if Pokémon is discovered.

```typescript
async function isDiscovered(uid: string, pokemonId: number): Promise<boolean>
```

---

### `onAuthStateChanged(callback)`
Listens to auth state changes.

```typescript
function onAuthStateChanged(callback: (user: any) => void): () => void
```

---

## PokeAPI Service

### `fetchPokemonList(offset, limit, useCache)`
Fetches list of Pokémon with pagination.

```typescript
async function fetchPokemonList(
    offset?: number,
    limit?: number,
    useCache?: boolean
): Promise<PokemonListItem[]>
```

**Parameters:**
- `offset`: number (default: 0)
- `limit`: number (default: 151)
- `useCache`: boolean (default: true)

**Returns:** `Promise<PokemonListItem[]>`

**Example:**
```typescript
const pokemon = await fetchPokemonList(0, 151, true);
console.log(pokemon); // [{ id: 1, name: 'bulbasaur', sprite: '...', url: '...' }, ...]
```

---

### `fetchPokemonDetails(nameOrId, useCache)`
Fetches detailed Pokémon information.

```typescript
async function fetchPokemonDetails(
    nameOrId: string | number,
    useCache?: boolean
): Promise<PokemonDetails>
```

**Returns:** `Promise<PokemonDetails>`

**Example:**
```typescript
const details = await fetchPokemonDetails(25); // or 'pikachu'
console.log(details.types, details.stats, details.abilities);
```

---

### `fetchPokemonByType(type, useCache)`
Fetches Pokémon by type.

```typescript
async function fetchPokemonByType(
    type: string,
    useCache?: boolean
): Promise<PokemonListItem[]>
```

**Example:**
```typescript
const fireTypes = await fetchPokemonByType('fire');
```

---

### `searchPokemonByName(query, allPokemon)`
Searches Pokémon by name (client-side).

```typescript
function searchPokemonByName(
    query: string,
    allPokemon: PokemonListItem[]
): PokemonListItem[]
```

---

### `searchPokemonById(id, allPokemon)`
Searches Pokémon by ID (client-side).

```typescript
function searchPokemonById(
    id: number,
    allPokemon: PokemonListItem[]
): PokemonListItem | undefined
```

---

### `clearPokemonCache()`
Clears all cached Pokémon data.

```typescript
async function clearPokemonCache(): Promise<void>
```

---

## Cache Service

### `setCache(key, data, duration?)`
Stores data in cache.

```typescript
async function setCache<T>(
    key: string,
    data: T,
    duration?: number
): Promise<boolean>
```

---

### `getCache(key, returnExpired?)`
Retrieves data from cache.

```typescript
async function getCache<T>(
    key: string,
    returnExpired?: boolean
): Promise<T | null>
```

---

### `isCacheValid(key)`
Checks if cache is valid.

```typescript
async function isCacheValid(key: string): Promise<boolean>
```

---

### `removeCache(key)`
Removes a cache entry.

```typescript
async function removeCache(key: string): Promise<boolean>
```

---

### `removeCacheByPattern(pattern)`
Removes cache by pattern.

```typescript
async function removeCacheByPattern(
    pattern: string | RegExp
): Promise<number>
```

---

### `clearAllCache()`
Clears all cache.

```typescript
async function clearAllCache(): Promise<boolean>
```

---

### `getCacheStats()`
Gets cache statistics.

```typescript
async function getCacheStats(): Promise<CacheStats>
```

**Returns:**
```typescript
{
    totalItems: number,
    totalSize: string,
    oldestItem?: { key: string, age: string },
    newestItem?: { key: string, age: string }
}
```

---

## Type Definitions

### UserProfile
```typescript
interface UserProfile {
    uid: string;
    email: string;
    displayName: string;
    discoveredPokemon: number[];
    totalDiscovered: number;
    photoURL?: string;
    lastUpdated: number;
    createdAt: number;
}
```

---

### PokemonDetails
```typescript
interface PokemonDetails {
    id: number;
    name: string;
    height: number;
    weight: number;
    baseExperience: number;
    types: Array<{ slot: number, type: { name: string, url: string } }>;
    abilities: Array<{ ... }>;
    stats: Array<{ baseStat: number, stat: { name: string } }>;
    sprites: { frontDefault: string, backDefault: string, ... };
    moves: Array<{ move: { name: string } }>;
    flavorText?: string;
    habitat?: string;
}
```

---

### NotificationResponse
```typescript
interface NotificationResponse {
    success: boolean;
    message?: string;
    data?: any;
}
```

---

## Error Handling

All services use consistent error handling:

```typescript
try {
    const result = await someService();
    if (result.success) {
        // Handle success
    } else {
        console.error('Error:', result.message);
        // Handle error
    }
} catch (error) {
    console.error('Unexpected error:', error);
    // Handle exception
}
```

---

## Examples

### Complete Authentication Flow

```typescript
// Sign up
const signUpResponse = await signUp('user@email.com', 'password123', 'John');
if (!signUpResponse.success) {
    Alert.alert('Error', signUpResponse.message);
    return;
}

// Request notifications
const allowed = await requestNotificationPermission();

// Initialize notifications
await initializeNotifications({
    onNotificationReceived: (notification) => {
        console.log('New notification:', notification);
    }
});

// Get FCM token and save it
const token = await getFCMToken();
if (token) {
    await updateUserProfile(signUpResponse.user.uid, { fcmToken: token });
}
```

---

### Pokémon Discovery Flow

```typescript
// Fetch all Pokémon
const allPokemon = await fetchPokemonList(0, 151);

// Filter by type
const fireTypes = await fetchPokemonByType('fire');

// Get details
const details = await fetchPokemonDetails(25);

// Add to discovered
const response = await addDiscoveredPokemon(userId, 25);
if (response.success) {
    showNotification('Pokemon Discovered!', `You found ${details.name}!`);
}

// Check if discovered
const isFound = await isDiscovered(userId, 25);
```

---

### Caching Strategy

```typescript
// Check cache first
const cached = await getCache('pokemon_list');
if (cached) {
    // Use cached data
    setPokemon(cached);
} else {
    // Fetch and cache
    const list = await fetchPokemonList();
    await setCache('pokemon_list', list, 24 * 60 * 60 * 1000);
    setPokemon(list);
}
```

---

**Last Updated:** December 11, 2025  
**Version:** 1.0.0

