# AutoMessage

![AutoMessage](https://img.shields.io/badge/Minecraft-1.16%2B-blue) ![License](https://img.shields.io/badge/License-MIT-green) ![Platform](https://img.shields.io/badge/Platform-Paper%20%7C%20Spigot-lightgrey)

AutoMessage is a lightweight and highly customizable plugin that allows you to send automated chat messages on your Minecraft server at configurable intervals. Perfect for server announcements, tips, or important information!

## 📜 Features
- ✅ Fully configurable messages in `config.yml`
- ✅ Custom time intervals for each message
- ✅ Enable or disable specific messages
- ✅ Supports color codes (`&` formatting)
- ✅ Optimized for performance

## 📥 Installation
1. Download the latest version of AutoMessage.
2. Place the `.jar` file into your `plugins/` folder.
3. Restart or reload your server.
4. Configure messages in `config.yml`.

## ⚙️ Configuration
Example `config.yml`:
```yaml
send-messages:
  welcome:
    enabled: true
    interval: 300
    messages:
      - "&6Welcome to our server!"
      - "&bEnjoy your stay and follow the rules!"
  tips:
    enabled: true
    interval: 600
    messages:
      - "&aTip: Use /spawn to return to the main hub."
      - "&eTip: Need help? Ask staff members!"
```

## 📜 License
This project is licensed under the **MIT License**. You are free to use, modify, and distribute it with proper attribution.

## 📧 Support
If you have any issues or suggestions, feel free to open an issue on GitHub!

