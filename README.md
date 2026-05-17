# Scribble

An adaption from traditional game "scrabble".

## Overview

Scribble, a game adapted from a traditional word spelling game "scrabble", is designed for people who suffer from loneliness or want to train themselves for scrabble competition.

This programme is written based on Java, with gradlew framework, swing for implementing GUI and Zhipu API for allocating an AI model.

## Features

- Galgame style GUI
- Various game configs
- AI with different difficulties
- AI companion
- Additional operations(shuffle, skip)
- Windows support
- MacOS support

## Installation

Before installation, make sure that you have installed **Java runtime** and **JDK 8** or above!

### Method 1

* Use the link <https://github.com/sakuraChiku/scribble.git> to clone the source code to your desktop.  
* Open the source code in your editor.  
* Find ``Main.java`` and run this file.

### Method 2

* Go to the release page and download ``scribble.jar``.
* Double click to run the file.

## Project Structure

```
scribble
├─ README.md
└─ app
   └─ src
      ├─ main
      │  ├─ java
      │  │  └─ com
      │  │     └─ kumoasobi
      │  │        └─ scribble
      │  │           ├─ Main.java
      │  │           ├─ ai
      │  │           │  ├─ AIDifficulty.java
      │  │           │  ├─ AIMove.java
      │  │           │  ├─ AIPlayer.java
      │  │           │  ├─ ScribbleAI.java
      │  │           │  └─ ZhipuChatClient.java
      │  │           ├─ controller
      │  │           │  ├─ GameController.java
      │  │           │  └─ MenuController.java
      │  │           ├─ exceptions
      │  │           │  ├─ CellOccupiedException.java
      │  │           │  ├─ CellOutOfBoundException.java
      │  │           │  ├─ EmptyMoveException.java
      │  │           │  ├─ FirstMoveNotThroughCenter.java
      │  │           │  ├─ FirstMoveOnlyOneWordException.java
      │  │           │  ├─ GameException.java
      │  │           │  ├─ MoveNotContinuousException.java
      │  │           │  ├─ MoveNotInLineException.java
      │  │           │  ├─ ReachMaxRefreshTimes.java
      │  │           │  ├─ TileNotEnoughException.java
      │  │           │  ├─ WordNotConnectedToExistingTile.java
      │  │           │  └─ WordNotValidException.java
      │  │           ├─ gui
      │  │           │  ├─ BoardPanel.java
      │  │           │  ├─ CharacterChatPanel.java
      │  │           │  ├─ ConfigUI.java
      │  │           │  ├─ ControlPanel.java
      │  │           │  ├─ GameWindow.java
      │  │           │  ├─ IntroductionDialog.java
      │  │           │  ├─ MenuUI.java
      │  │           │  └─ RackPanel.java
      │  │           ├─ models
      │  │           │  ├─ Board.java
      │  │           │  ├─ BonusType.java
      │  │           │  ├─ Cell.java
      │  │           │  ├─ Direction.java
      │  │           │  ├─ GameState.java
      │  │           │  ├─ Move.java
      │  │           │  ├─ MoveResult.java
      │  │           │  ├─ Placement.java
      │  │           │  ├─ Player.java
      │  │           │  ├─ Tile.java
      │  │           │  ├─ TileBag.java
      │  │           │  └─ WordInfo.java
      │  │           ├─ rules
      │  │           │  ├─ config
      │  │           │  │  ├─ DrawMode.java
      │  │           │  │  ├─ EndMode.java
      │  │           │  │  ├─ GameConfig.java
      │  │           │  │  ├─ GameConfigFactory.java
      │  │           │  │  └─ GameConfigRequest.java
      │  │           │  ├─ scanner
      │  │           │  │  └─ WordScanner.java
      │  │           │  ├─ strategy
      │  │           │  │  ├─ DrawStrategy.java
      │  │           │  │  ├─ GameEndStrategy.java
      │  │           │  │  ├─ LimitedDrawStrategy.java
      │  │           │  │  ├─ LimitedScoreGameEndStrategy.java
      │  │           │  │  ├─ LimitedTileGameEndStrategy.java
      │  │           │  │  ├─ LimitedTimeGameEndStrategy.java
      │  │           │  │  ├─ LimitedTurnGameEndStrategy.java
      │  │           │  │  └─ UnlimitedDrawStrategy.java
      │  │           │  └─ validator
      │  │           │     ├─ BoardValidator.java
      │  │           │     ├─ DictValidator.java
      │  │           │     └─ PlayerValidator.java
      │  │           ├─ save
      │  │           │  ├─ LoadManager.java
      │  │           │  └─ SaveManager.java
      │  │           └─ util
      │  │              ├─ DictionaryLoader.java
      │  │              └─ SoundManager.java
      │  └─ resources
      │     └─ assets
      │        ├─ dict
      │        │  ├─ full_wordlist.dat
      │        │  └─ stan_dict.txt
      │        ├─ img
      │        │  └─ gui
      │        │     ├─ title_background.png
      │        │     ├─ title_charall.png
      │        │     ├─ title_head.png
      │        │     ├─ title_headline.png
      │        │     └─ title_logo.png
      │        └─ sound
      │           ├─ yuzu_button_cancel.wav
      │           ├─ yuzu_button_change.wav
      │           ├─ yuzu_button_decide.wav
      │           ├─ yuzu_button_select.wav
      │           ├─ yuzu_button_start.wav
      │           ├─ yuzu_button_success.wav
      │           ├─ yuzu_game_music.wav
      │           ├─ yuzu_title_button_instruction.wav
      │           ├─ yuzu_title_button_quit.wav
      │           ├─ yuzu_title_button_select_save.wav
      │           ├─ yuzu_title_music.wav
      │           └─ yuzu_title_senren.wav
      └─ test
         └─ java
```

## Roadmap

- LAN-or-bluetooth-based online game
- Neural-network-based AI
- iOS/Android support
- Deeply immersive AI character
- Settings option

## Contributing

Pull requests are welcome.
For major changes, please open an issue first.