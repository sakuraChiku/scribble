## Getting Started

Welcome to the VS Code Java world. Here is a guideline to help you get started to write Java code in Visual Studio Code.

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).

## Project Structure

gui/        Still learning 
 ├── cli/
 ├── gui/

controller/
 └── GameController   ← UI适配层（薄）

engine/
 └── GameEngine     the core  

logic/          started --yyc
 ├── BoardValidator
 ├── DictionaryValidator    important!
 ├── PlayerValidator
 └── ScoreCalculator        complicated logics

models/         almost done --yyc
 ├── Board
 ├── BonusType
 ├── Cell
 ├── Direction
 ├── GameState      not done
 ├── Move
 ├── Placement
 ├── Player
 ├── Tile
 └── TileBag

 save/      after learning I/O......
 
