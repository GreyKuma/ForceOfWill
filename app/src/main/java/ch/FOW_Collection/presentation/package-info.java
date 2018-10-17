/**
 * The presentation package contains all the view related things of this application, and therefore the majority of
 * code. It is organized by screens in the app, where each screen or tab gets its own package.
 * <p>
 * The {@link ch.beerpro.presentation.MainActivity} has three tabs, these correspond to the explore, ratings and
 * profile subpackages. The tabs are just fragments, not activits, and all share the same
 * {@link ch.beerpro.presentation.MainViewModel} view model.
 *
 * The details package is not directly reachabel from the main screen but from many other activities and fragments
 * and it thus deservers its own (presentation) top-level package.
 *
 * <p>
 * The splash package contains the splashscreen that also holds the login. So technically, the
 * {@link ch.beerpro.presentation.splash.SplashScreenActivity} is the entry point of the class, but logged in users get
 * redirected to the {@link ch.beerpro.presentation.MainActivity} immediately.
 */
package ch.beerpro.presentation;