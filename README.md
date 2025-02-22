# ToruYomi (透読)

ToruYomi is a Java-based desktop application designed to help Japanese learners easily create Anki flashcards from kanji input. The app fetches readings, meanings, and example sentences from dictionary APIs and sends the formatted data to Anki through the AnkiConnect extension.

## Features
- Accepts kanji input from the user.
- Fetches readings and meanings from [Jisho](jisho.org).
- Retrieves example sentences from [Tatoeba](tatoeba.org).
- Allows users to edit data before sending to Anki.
- Sends formatted flashcards directly to Anki using [AnkiConnect](https://ankiweb.net/shared/info/2055492159).
- Works without Anki for word lookup; Anki is only required for adding cards.
- Automatically creates the "ToruYomi" deck and model if they don’t exist.

## Tech Stack and Tools
- **Java 23** — Core language for the application backend.
- **JavaFX 23** — For the desktop graphical user interface.
- **Maven** — Dependency management and project build.
- **AnkiConnect API** — Integration for sending flashcards to Anki.

## Requirements
- **Java 23 Runtime Environment** (if using the JAR version)
- **Anki** with the [AnkiConnect Extension](https://ankiweb.net/shared/info/2055492159) (only required for adding cards)

## Installation and Usage
### Using the Executable (.exe)
- Download the latest `.exe` release from the [Releases](https://github.com/anri-kot/ToruYomi/releases) page.
- Double-click the `.exe` file to launch ToruYomi — no additional setup required.

### Using the JAR File
- Ensure Java 23 or later is installed.
- Download the `.jar` release.
- Remember to run with Java Runtime 23.
#### Running through command line/script
- On **Windows**, run the following command from the terminal or create a `.bat` script:

```cmd
java -jar ToruYomi.jar
```

- On **Linux**, run the following command from the terminal or create a `.sh` script:

```bash
java -jar ToruYomi.jar 
```

## Deck and Model Setup
- By default, ToruYomi uses a deck and model both named **"ToruYomi"**.
- If they don’t exist, the app creates them automatically.
- For now, deck customization is limited — if you switch to a different deck, it must:
  - Have a model with the **same name as the deck**.
  - Contain the following fields:
    - `word` (Front)
    - `readings`, `meanings`, `examples` (Back)

## Future Improvements
- Enhanced deck and model customization.
- More API options for word and sentence lookup.
- Better UI/UX improvements.

## Contributing
Feel free to submit pull requests or report issues! Any suggestions for improvement are welcome.

## License
MIT License
