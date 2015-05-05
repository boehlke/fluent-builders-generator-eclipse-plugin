<table cellpadding='4' align='right' border='0'><tr><td align='center'>
<img src='http://fluent-builders-generator-eclipse-plugin.googlecode.com/svn/wiki/images/GeekPark_logo_s.JPG' width='120' /><br />
<font size='2'>Project started at<br /><a href='http://www.sabre.pl'>Sabre Polska's</a> HackDay</font>
</td></tr></table>

A plugin that is going to change your way of creating Java objects, by leveraging the idea of [fluent interfaces](http://www.martinfowler.com/bliki/FluentInterface.html). By using it, You get:
  * ease of creating objects in one **clean and readable** method-chain
  * clever collections support
  * creation of complex object trees empowered by IDE's code completion

## What is the idea of Fluent builders ? ##


[Fluent Builder](http://refactormycode.com/codes/194-fluent-builder) is a variant of the [Builder pattern](http://en.wikipedia.org/wiki/Builder_pattern) that uses [Fluent interface](http://www.martinfowler.com/bliki/FluentInterface.html).

Fluent builders are especially handy when instantiating data objects within unit tests. To see the benefit, compare following two examples:

1. This code sample is a JUnit test method that uses fluent builder:

```
@Test public void shouldFindByActorWithFluentBuilder() {
    // given
    List<Movie> movies = Arrays.asList(
            MovieBuilder.movie().withTitle("Blade Runner")       // <- here's the builder used
                                .withAddedActor("Harrison Ford")
                                .withAddedActor("Rutger Hauer")
                        .build(),
            MovieBuilder.movie().withTitle("Star Wars")          // <- ... and also here
                                .withAddedActor("Carrie Fisher")
                                .withAddedActor("Harrison Ford")
                        .build());

    // when
    List<Movie> found = movieFinder.findByActor(movies, "Carrie Fisher");

    // then
    assertEquals(1, found.size());
    assertEquals("Star Wars", found.get(0).getTitle());
}
```

2. The same test using plain setters would be longer and much less readable:

```
@Test public void shouldFindByActorWithSetterObjectConstruction() {
    // given
    Movie movieBladeRunner = new Movie();

    movieBladeRunner.setTitle("Blade Runner");
    movieBladeRunner.setActors(Arrays.asList("Harrison Ford", "Rutger Hauer"));

    Movie movieStarWars = new Movie();

    movieStarWars.setTitle("Star Wars");
    movieStarWars.setActors(Arrays.asList("Carrie Fisher", "Harrison Ford"));

    List<Movie> movies = Arrays.asList(movieBladeRunner, movieStarWars);

    // when
    List<Movie> found = movieFinder.findByActor(movies, "Carrie Fisher");

    // then
    assertEquals(1, found.size());
    assertEquals("Star Wars", found.get(0).getTitle());
}
```

To see more advanced examples go to [Examples](Examples.md) page.

## Installation instructions ##

### Update site (recommended) ###
The latest version of the plugin can be found here:

http://fluent-builders-generator-eclipse-plugin.googlecode.com/hg/site/

### Manual installation ###
Just get the latest [plugin jar](http://fluent-builders-generator-eclipse-plugin.googlecode.com/hg/site/plugins/) and put it into the `dropins/` subdirectory of your Eclipse installation.

## Plugin usage ##

The builder class is generated based on your Java class obeying the Java Bean conventions for getters and setters.

In order to generate the builder, open the source for your class and from the context menu choose `Source -> Generate Fluent Builder...`

![http://fluent-builders-generator-eclipse-plugin.googlecode.com/svn/wiki/images/usage1.png](http://fluent-builders-generator-eclipse-plugin.googlecode.com/svn/wiki/images/usage1.png)

Then fill the wizard form and click the `Finish` button

![http://fluent-builders-generator-eclipse-plugin.googlecode.com/svn/wiki/images/usage2.png](http://fluent-builders-generator-eclipse-plugin.googlecode.com/svn/wiki/images/usage2.png)