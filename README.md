# Anki Auto

A web application that receives kanji input and automatically creates Anki cards with hiragana readings, meanings, and example sentences. Uses dictionary APIs to fetch data and AnkiConnect to insert cards into Anki.  

## Features  
- Accepts kanji input from the user.  
- Fetches readings and meanings from an external dictionary API.  
- Retrieves example sentences using API sources like Tatoeba.  
- Sends the formatted data to Anki via AnkiConnect.  

## Tech Stack  
- **Backend:** Java
- **Frontend:** JavaFX
- **APIs Used:**  
  - [AnkiConnect API](https://foosoft.net/projects/anki-connect/) for Anki integration  