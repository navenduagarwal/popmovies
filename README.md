# Popular Movies
Code for Udacity Popular Movies App Implementation

## Stage 1: Main Discovery Screen, A Details View, and Settings

In this stage built the core experience for movies app.

## User Experience ##

App will
 
- upon launch, present the user with an grid arrangement of movie posters.
- Allow your user to change sort order via a setting:
	- The sort order can be by most popular, or by top rated
- Allow the user to tap on a movie poster and transition to a details screen with additional information such as:
	- original title
	- movie poster image thumbnail
	- A plot synopsis (called overview in the api)
	- user rating (called vote_average in the api)
	- release date

Update your API KEY by generating a new one here at TheMoviesDB [Website](https://www.themoviedb.org/account/signup "Website") and updating it in build.gradel(Module:app)


## Stage 2: Trailers, Reviews, and Favorites

- We added more information to your movie details view:
- We allow users to view and play trailers ( either in the youtube app or a web browser).
- We allow users to read reviews of a selected movie.
- We also allow users to mark a movie as a favorite in the details view by tapping a button(star). This is for a local movies collection that you will maintain and does not require an API request*.
- We modified the existing sorting criteria for the main view to include an additional pivot to show their favorites collection.