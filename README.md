# ReacT
ReacT helps you find the perfect reaction gif. Share the text you receive with the app and it will suggest a gif based on its emotion.

## Features
These are the main features
- Show suggested gifs
- Gifs based on keywords and emotion
- Share from the app

## Intended user
This app is intended for anyone who wishes to express their reaction through gifs rather than text and emoji. Target audience includes students and meme browsers alike.

## Key Considerations

### Handling data persistence
The app uses a local SQL database for storing favourite gifs. This is implemented using a ContentProvider. 
A CursorLoader is used to load the data to the views.

### Corner cases in the UX
When the user directly types in the search bar, it performs a lookup verbatim. When text is
shared to the app via an intent, it analyzes it for emotion/keywords and displays the result.

### Libraries used
- Synesketch for emotion detection
- Glide to handle image loading
- Giphy API for obtaining gifs.
