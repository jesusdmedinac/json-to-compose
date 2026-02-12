# Phase: Demo Showcase — Progress

## Feature 1: Navigation Infrastructure
- [x] Add navigation-compose dependency to composeApp
- [x] Define screen routes as serializable objects
- [x] Provide NavHostController via CompositionLocal
- [x] Register NavHost as a Custom renderer
- [x] Bottom bar navigates between screens
- [x] Bottom navigation reflects the currently selected screen

## Feature 2: App Shell with Scaffold and Design Language
- [x] App shell renders a Scaffold with TopAppBar
- [x] App shell renders a BottomBar with three items
- [x] Consistent color palette applied across screens
- [x] Helper functions produce reusable styled nodes

## Feature 3: Catalog Screen — Home
- [ ] Hero section with Box + BackgroundColor + centered text
- [ ] Component category cards in LazyRow (Layout, Content, Input, Containers)
- [ ] Featured components list with mini live demos
- [ ] Modifier highlights row (Shadow, Clip, Border, Rotate)
- [ ] Built entirely with ComposeNode trees

## Feature 4: Components Screen — Interactive Demos
- [ ] Layout demos (Column, Row, Box with arrangements)
- [ ] Content demos (Text, Image URL, Image resource)
- [ ] Interactive input demos (Button, TextField, Switch, Checkbox with StateHost)
- [ ] Container demos (Card, AlertDialog, mini-Scaffold)
- [ ] Lazy list demos (LazyColumn, LazyRow)
- [ ] Navigation component demos (TopAppBar, BottomBar)
- [ ] Custom renderer demo (ProductCard)
- [ ] Built entirely with ComposeNode trees

## Feature 5: Styles Screen — Modifier Gallery
- [ ] Sizing modifier demos (Padding, FillMaxWidth, Width+Height, FillMaxSize)
- [ ] Color palette demos (5 colored boxes with hex labels)
- [ ] Shape and clip demos (Circle, RoundedCorner, per-corner)
- [ ] Border and shadow comparison
- [ ] Alpha and rotation gallery (25/50/75/100%, 0/15/45/90 degrees)
- [ ] Combined modifiers demo
- [ ] Built entirely with ComposeNode trees

## Feature 6: Multi-Platform Compilation
- [ ] Compiles on Android
- [ ] Compiles on Desktop
- [ ] Compiles on iOS
- [ ] Compiles on WASM
- [ ] Code organized into separate files (App.kt, CatalogScreen.kt, ComponentsScreen.kt, StylesScreen.kt, DemoHelpers.kt)
- [ ] No navigation dependency added to library module
