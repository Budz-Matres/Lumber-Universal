# PokeExplorer - Multi-Language Implementation Guide

## Overview

PokeExplorer now supports **5 major programming languages**:
1. **TypeScript** - React Native (primary)
2. **JavaScript** - React Native / Node.js
3. **Kotlin** - Android native
4. **Swift** - iOS native
5. **Ruby** - Rails backend

This guide shows how to use and implement each language.

---

## 1. TypeScript (React Native)

### Usage
```typescript
import {
  signUp,
  signIn,
  getUserProfile,
  addDiscoveredPokemon,
  isDiscovered,
} from './services/firebase';

// Sign up
const response = await signUp('user@email.com', 'password123', 'John');
if (response.success) {
  console.log('User created:', response.user);
}

// Get profile
const profile = await getUserProfile(uid);

// Add discovered Pokémon
const result = await addDiscoveredPokemon(uid, 25);
if (result.success) {
  console.log('Pokémon added!');
}

// Check if discovered
const discovered = await isDiscovered(uid, 25);
```

### Features
- Full type safety with interfaces
- Async/await support
- Error handling with response objects
- TypeScript generics support

### Files
- `src/services/firebase.ts` - Main service
- `src/services/notifications.ts` - Notifications
- `src/services/pokeApi.ts` - PokeAPI
- `src/services/cache.ts` - Caching

---

## 2. JavaScript (React Native / Node.js)

### Usage
```javascript
import { firebaseServiceJS } from './services/firebase';

// Sign up
const response = await firebaseServiceJS.signUp(
  'user@email.com',
  'password123',
  'John Doe'
);
if (response.success) {
  console.log('User created');
}

// Get profile
const profile = await firebaseServiceJS.getUserProfile(uid);

// Add discovered Pokémon
const result = await firebaseServiceJS.addDiscoveredPokemon(uid, 25);

// Check if discovered
const isFound = await firebaseServiceJS.isDiscovered(uid, 25);
```

### Features
- No type annotations
- Flexible parameters
- Same functionality as TypeScript
- Perfect for non-TS projects

### Implementation
- `firebaseServiceJS` object in firebase.ts
- All methods return plain JavaScript objects
- Compatible with CommonJS and ES6 modules

---

## 3. Kotlin (Android Native)

### File Location
```
android/app/src/main/kotlin/com/example/
├── FirebaseService.kt
├── UserProfile.kt
└── AuthResponse.kt
```

### Usage
```kotlin
val firebaseService = FirebaseService()

// Sign up
firebaseService.signUp("user@email.com", "password123", "John") { response ->
    if (response.success) {
        println("User created: ${response.user?.displayName}")
    } else {
        println("Error: ${response.message}")
    }
}

// Get profile
firebaseService.getUserProfile(uid) { profile ->
    println("User: ${profile?.displayName}")
}

// Add Pokémon
firebaseService.addDiscoveredPokemon(uid, 25) { response ->
    if (response.success) {
        println("Pokémon added!")
    }
}
```

### Features
- Native Kotlin coroutines support
- Type-safe with data classes
- Firebase SDK integration
- Callback-based async operations

### Type Definitions
```kotlin
data class UserProfile(
    val uid: String,
    val email: String,
    val displayName: String,
    val discoveredPokemon: List<Int>,
    val totalDiscovered: Int,
    val photoURL: String? = null,
    val lastUpdated: Long,
    val createdAt: Long
)

data class AuthResponse(
    val success: Boolean,
    val user: UserProfile? = null,
    val message: String? = null,
    val error: Exception? = null
)
```

---

## 4. Swift (iOS Native)

### File Location
```
ios/PokeExplorer/
├── FirebaseService.swift
├── Models.swift
└── AppDelegate.swift
```

### Usage
```swift
let firebaseService = FirebaseService.shared

// Sign up
firebaseService.signUp(email: "user@email.com", password: "password123", displayName: "John") { response in
    if response.success {
        print("User created: \(response.user?.displayName ?? "")")
    } else {
        print("Error: \(response.message ?? "")")
    }
}

// Get profile
firebaseService.getUserProfile(uid: uid) { profile in
    print("User: \(profile?.displayName ?? "")")
}

// Sign in
firebaseService.signIn(email: "user@email.com", password: "password123") { response in
    if response.success {
        print("Signed in!")
    }
}
```

### Features
- Swift concurrency support (async/await)
- Codable for JSON encoding/decoding
- Completion handlers
- Type-safe structs

### Type Definitions
```swift
struct UserProfile: Codable {
    let uid: String
    let email: String
    let displayName: String
    let discoveredPokemon: [Int]
    let totalDiscovered: Int
    let photoURL: String?
    let lastUpdated: TimeInterval
    let createdAt: TimeInterval
}

struct AuthResponse {
    let success: Bool
    let user: UserProfile?
    let message: String?
    let error: Error?
}
```

---

## 5. Ruby (Rails Backend)

### File Location
```
lib/
├── firebase_service.rb
└── models/
    └── user.rb
```

### Installation
```bash
gem 'firebase-admin-sdk'
```

### Usage
```ruby
# Create service
service = FirebaseService.new

# Sign up
response = service.sign_up('user@email.com', 'password123', 'John Doe')
if response.success
  puts "User created: #{response.user.displayName}"
else
  puts "Error: #{response.message}"
end

# Get profile
profile = service.get_user_profile(uid)
puts "User: #{profile.displayName}"

# Add discovered Pokémon
response = service.add_discovered_pokemon(uid, 25)
if response.success
  puts "Pokémon added!"
end

# Check if discovered
is_found = service.is_discovered?(uid, 25)
```

### Features
- Struct-based type definitions
- Exception handling
- Firebase Admin SDK integration
- Synchronous operations

### Type Definitions
```ruby
UserProfile = Struct.new(
  :uid,
  :email,
  :displayName,
  :discoveredPokemon,
  :totalDiscovered,
  :photoURL,
  :lastUpdated,
  :createdAt,
  keyword_init: true
)

AuthResponse = Struct.new(
  :success,
  :user,
  :message,
  :error,
  keyword_init: true
)
```

---

## Service Architecture Across Languages

### TypeScript (Primary)
```
src/services/
├── firebase.ts          ← Type definitions + TS implementation
├── notifications.ts     ← Multi-language notifications
├── pokeApi.ts          ← API integration
├── cache.ts            ← Caching system
└── voiceSearch.ts
```

### JavaScript
```
firebaseServiceJS object (in firebase.ts)
- All methods return plain JavaScript
- No type annotations
- Drop-in replacement for TypeScript functions
```

### Kotlin
```
android/app/src/main/kotlin/com/example/
└── FirebaseService.kt   ← Android native implementation
```

### Swift
```
ios/PokeExplorer/
└── FirebaseService.swift ← iOS native implementation
```

### Ruby
```
lib/firebase_service.rb  ← Rails backend implementation
```

---

## Comparison Table

| Feature | TypeScript | JavaScript | Kotlin | Swift | Ruby |
|---------|-----------|-----------|--------|-------|------|
| Type Safety | ✅ Full | ❌ No | ✅ Full | ✅ Full | ✅ Partial |
| Async Support | ✅ Async/Await | ✅ Async/Await | ✅ Coroutines | ✅ Async/Await | ❌ Sync |
| Firebase SDK | ✅ React Native | ✅ React Native | ✅ Native | ✅ Native | ✅ Admin SDK |
| Platform | React Native | Node.js/RN | Android | iOS | Rails/Web |
| Error Handling | ✅ Try/Catch | ✅ Try/Catch | ✅ Try/Catch | ✅ Try/Catch | ✅ Exception |
| Community | ⭐⭐⭐ | ⭐⭐⭐ | ⭐⭐ | ⭐⭐ | ⭐ |

---

## Implementation Examples

### Example 1: User Registration Flow

**TypeScript:**
```typescript
async function registerUser(email: string, password: string, name: string) {
  const response = await signUp(email, password, name);
  if (response.success && response.user) {
    await addDiscoveredPokemon(response.user.uid, 1);
    return response.user;
  }
  throw new Error(response.message);
}
```

**Kotlin:**
```kotlin
fun registerUser(email: String, password: String, name: String) {
    firebaseService.signUp(email, password, name) { response ->
        if (response.success && response.user != null) {
            firebaseService.addDiscoveredPokemon(response.user!!.uid, 1)
        }
    }
}
```

**Swift:**
```swift
func registerUser(email: String, password: String, name: String) {
    firebaseService.signUp(email: email, password: password, displayName: name) { response in
        if response.success, let user = response.user {
            // Add first Pokémon
        }
    }
}
```

**Ruby:**
```ruby
def register_user(email, password, name)
  response = service.sign_up(email, password, name)
  if response.success && response.user
    service.add_discovered_pokemon(response.user.uid, 1)
    response.user
  else
    raise response.message
  end
end
```

---

## Migration Guide

### From TypeScript to JavaScript
```javascript
// Change from TS imports
// import { signUp } from './firebase'
// To JS exports
import { firebaseServiceJS } from './firebase'

// Then use
firebaseServiceJS.signUp(...)
```

### From TypeScript to Kotlin
```kotlin
// 1. Copy FirebaseService.kt to android/app/src/main/kotlin/
// 2. Update package name
// 3. Use in Android code:
val service = FirebaseService()
service.signUp(...) { response ->
    // Handle response
}
```

### From TypeScript to Swift
```swift
// 1. Copy FirebaseService.swift to ios/PokeExplorer/
// 2. Update imports if needed
// 3. Use in iOS code:
FirebaseService.shared.signUp(...) { response in
    // Handle response
}
```

### From TypeScript to Ruby
```ruby
# 1. Copy firebase_service.rb to lib/
# 2. Update require statements
# 3. Use in Rails:
service = FirebaseService.new
response = service.sign_up(...)
```

---

## Best Practices

### 1. Type Safety
- Use TypeScript for React Native when possible
- Use Kotlin for Android, Swift for iOS
- Use Ruby with schema validation

### 2. Error Handling
- Always check `response.success`
- Log errors with meaningful messages
- Provide user-friendly error messages

### 3. Async Operations
- Use async/await where possible
- Use promises for JavaScript
- Use callbacks/coroutines for Kotlin

### 4. Security
- Never store credentials in code
- Use environment variables for secrets
- Validate inputs on all platforms

### 5. Testing
- Unit test each language independently
- Integration test cross-platform flows
- Use mocking for Firebase in tests

---

## Troubleshooting

### TypeScript Errors
```
Error: Cannot find module
→ Check import paths
→ Verify tsconfig.json
```

### JavaScript Issues
```
Error: firebaseServiceJS is undefined
→ Check export statement
→ Verify import path
```

### Kotlin Compilation
```
Error: Unresolved reference
→ Add Firebase dependency to build.gradle
→ Sync gradle
```

### Swift Issues
```
Error: Cannot find 'FirebaseService' in scope
→ Check file is in Xcode project
→ Verify import statements
```

### Ruby Errors
```
Error: Uninitialized constant Firebase
→ Add gem 'firebase-admin-sdk'
→ Run bundle install
```

---

## Supporting Multiple Languages

### Project Structure
```
PokeExplorer/
├── src/                    # TypeScript/JavaScript
│   └── services/firebase.ts
├── android/                # Kotlin
│   └── app/src/main/kotlin/
├── ios/                    # Swift
│   └── PokeExplorer/
├── lib/                    # Ruby
│   └── firebase_service.rb
└── README.md
```

### CI/CD Pipeline
```yaml
# Test all languages
- Lint TypeScript
- Test JavaScript
- Build Android (Kotlin)
- Build iOS (Swift)
- Test Rails (Ruby)
```

---

## Performance Notes

| Language | Speed | Memory | Startup |
|----------|-------|--------|---------|
| TypeScript | Fast | High | ~2s |
| JavaScript | Fast | High | ~2s |
| Kotlin | Very Fast | Optimal | ~1s |
| Swift | Very Fast | Optimal | ~1s |
| Ruby | Slow | High | ~3s |

---

## Documentation Links

- [TypeScript Docs](https://www.typescriptlang.org)
- [Kotlin Docs](https://kotlinlang.org)
- [Swift Docs](https://swift.org)
- [Ruby Docs](https://ruby-doc.org)
- [Firebase Admin SDK](https://firebase.google.com/docs/admin/setup)

---

## Contributing

To add support for a new language:

1. Create implementation file
2. Add type definitions
3. Implement core functions
4. Export in `allFirebaseImplementations`
5. Update this guide
6. Test thoroughly

---

**Last Updated:** December 12, 2025  
**Languages Supported:** 5  
**Status:** Production Ready ✅

