# json-to-compose

A library that dynamically converts JSON into Jetpack Compose components.

## 📝 Description

json-to-compose is an Android library that allows you to convert JSON structures into Jetpack Compose components at runtime. This makes it easy to create dynamic interfaces and update UI based on remote data.

## 💡 Use case

### 1. Server-Driven UI
This library is perfect for implementing Server-Driven UI patterns, where your backend controls the UI layout and content. Some benefits include:
- Update your app's UI without deploying new versions
- A/B testing different layouts and components
- Dynamic content presentation based on user segments
- Consistent UI across platforms by sharing the same JSON structure

Example:
```kotlin
@Composable
fun App() {
    var uiDefinition by remember { mutableStateOf(api.fetchUIDefinition()) }
    
    LaunchedEffect(Unit) {
        uiDefinition = api.fetchUIDefinition()
    }
    
    uiDefinition.ToCompose()
}
```

### 2. Generative UI
Integrate with AI services to create dynamic UIs based on user input or context:
- AI-powered layout suggestions
- Dynamic form generation based on user preferences
- Context-aware UI adaptations
- Prototype testing with AI-generated interfaces

Example:
```kotlin
@Composable
fun App(userContext: String) {
    val aiGeneratedUI = aiService.generateUIForUser(userContext)
    aiGeneratedUI.ToCompose()
}
```

## ⚡️ Installation

Add the following dependency to your `build.gradle.kts` file:

```kotlin
dependencies {
  implementation("com.jesusdmedinac:json-to-compose:1.0.1")
}
```

## 🚀 Usage

```kotlin
@Composable
@Preview
fun App() {
    MaterialTheme {
        JSON_AS_STRING.ToCompose(object : Behavior {
            override fun onClick(eventName: String) {
                println("Event: $eventName")
            }
        })
    }
}

val JSON_AS_STRING = """
    {
        "type": "Column",
        "children": [
            {
                "type": "Text",
                "text": "String to Text"
            },
            {
                "type": "Text",
                "text": "{\"type\": \"Text\",\"text\": \"String to Text\"}"
            },
            {
                "type": "Text",
                "text": "String to Text"
            },
            {
                "type": "Text",
                "text": "String to Button"
            },
            {
                "type": "Text",
                "text": "{\"type\": \"Button\",\"onClickEventName\": \"button_clicked\",\"child\": {\"type\": \"Text\",\"text\": \"Click me!\"}}"
            },
            {
                "type": "Button",
                "onClickEventName": "button_clicked",
                "child": {
                    "type": "Text",
                    "text": "Click me!"
                }
            },
            {
                "type": "Text",
                "text": "String to Column"
            },
            {
                "type": "Text",
                "text": "{\"type\": \"Column\",\"children\": [{\"type\": \"Text\",\"text\": \"First text\"}, {\"type\": \"Text\",\"text\": \"Second text\"}]}"
            },
            {
                "type": "Column",
                "children": [
                    {
                        "type": "Text",
                        "text": "First text"
                    },
                    {
                        "type": "Text",
                        "text": "Second text"
                    }
                ]
            },
            {
                "type": "Text",
                "text": "String to Row"
            },
            {
                "type": "Text",
                "text": "{\"type\": \"Row\",\"children\": [{\"type\": \"Text\",\"text\": \"First text\"}, {\"type\": \"Text\",\"text\": \"Second text\"}]}"
            },
            {
                "type": "Row",
                "children": [
                    {
                        "type": "Text",
                        "text": "First text"
                    },
                    {
                        "type": "Text",
                        "text": "Second text"
                    }
                ]
            },
            {
                "type": "Text",
                "text": "String to Box"
            },
            {
                "type": "Text",
                "text": "{\"type\": \"Box\",\"children\": [{\"type\": \"Text\",\"text\": \"First text\"}, {\"type\": \"Text\",\"text\": \"Second text\"}]}"
            },
            {
                "type": "Box",
                "children": [
                    {
                        "type": "Text",
                        "text": "First text"
                    },
                    {
                        "type": "Text",
                        "text": "Second text"
                    }
                ]
            }
        ]
    }
""".trimIndent()
```


## 🔧 Supported Components

- Text
- Column
- Row
- Box
- Button
- Much more comming soon...

## 📚 Documentation

WIP

## 🤝 Contributing

WIP

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📞 Contact

If you have questions or suggestions, feel free to:
- Open an [issue](https://github.com/jesusdmedinac/json-to-compose/issues/new)
- Follow me on Social network:
  - [YouTube](https://www.youtube.com/@jesusdmedinac/)
  - [LinkedIn](https://www.linkedin.com/in/jesusdmedinac/)
  - [TikTok](https://www.tiktok.com/@jesusdmedinac)
  - [Facebook](https://www.facebook.com/jesusdmedinac)
  - [Instagram](https://www.instagram.com/jesusdmedinac)
  - [X](https://x.com/JesusDMedinaC)
  - [Github](https://github.com/jesusdmedinac)
  - [Medium](https://blog.jesusdmedinac.com)
  - [Discord](https://discord.gg/v5jv8k5b)
  - [Twitch](https://www.twitch.tv/jesusdmedinac)
- Send me an email at [hi@jesusdmedinac.com]