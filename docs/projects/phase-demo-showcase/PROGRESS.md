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
- [x] Hero section with Box + BackgroundColor + centered text
- [x] Component category cards in LazyRow (Layout, Content, Input, Containers)
- [x] Featured components list with mini live demos
- [x] Modifier highlights row (Shadow, Clip, Border, Rotate)
- [x] Built entirely with ComposeNode trees

## Feature 4: Components Screen — Interactive Demos
- [x] Layout demos (Column, Row, Box with arrangements)
- [x] Content demos (Text, Image URL, Image resource)
- [x] Interactive input demos (Button, TextField, Switch, Checkbox with StateHost)
- [x] Container demos (Card, AlertDialog, mini-Scaffold)
- [x] Lazy list demos (LazyColumn, LazyRow)
- [x] Navigation component demos (TopAppBar, BottomBar)
- [x] Custom renderer demo (ProductCard)
- [x] Built entirely with ComposeNode trees

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
