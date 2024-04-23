# Music Visualiser Project

Name: Glenn Collins

Student Number: C22457612

Name: Luis Perez

Student Number: C22734141

## Instructions
- Fork this repository and use it a starter project for your assignment
- Create a new package named your student number and put all your code in this package.
- You should start by creating a subclass of ie.tudublin.Visual
- There is an example visualiser called MyVisual in the example package
- Check out the WaveForm and AudioBandsVisual for examples of how to call the Processing functions from other classes that are not subclasses of PApplet

# Description of the assignment
This project uses geometric multicoloured shapes to create an audio visualizer. It begins with a 3D retro videogame intro and transitions into a 3D audio visualizer 
that uses shapes such as cubes,pyramids,diamonds sphere's etc that react to audio. This visualizer has custom features that allows you to interact with the visualizer as the music plays, making certain shapes more or less reactive, increasing the hue, inverting the colour scheme etc.

# Instructions
- Use SPACE to start the visualizer (indicated by start screen)
- After the intro plays press ENTER to proceed to visualizer
- In visualizer, use UP and DOWN arrow keys to control main shape speed.
- Use LEFT and RIGHT arrow keys to control small cube speed
- Use 'p' or 'P' to pause the visualizer
- Use 'e' or 'E' to turn on extreme colours mode
- Use 'f' or 'F' to turn on fill mode (inverted mode)
- Use 'm' or 'M' to turn on fill mode for the main shape (inverted mode)
- Click on left cube on far left in between pyramids to swap to diamond shape
- Click on right cube on far right in between pyramids to swap back to cube shape

# How it works
# Intro
The visualizer begins in the intro screen, prompting the user to press space to begin the intro animation as a sphere visual spins in the background.
The animation plays, drawing two mirroring pyramids rotating away from the diamond.
At a certain distance, each pyramid gains another mirroring pyramid as it loses its colour and it gains soundwaves on the peak of its shape.
When the pyramids gain their soundwaves, a circle fades in and neon text fades in as the User is prompted to press Enter.
When the User presses Enter, the circle dissapears and a new rainbow wave circle appears, reacting to the audio sound effect played in that moment.
As this happens a timer counts down that fades the objects on screen out of sight.
# Visualizer
When the fade is finished, the visualizer is loaded, the pyramids move out further, a small cube will be inside each pyramid, the soundwaves will be 
larger and the sphere will be drawn again.
The visualizer is made up of soundwaves, pyramids,diamonds,spheres and cubes that all react in different ways to the music played.
The cubes, including the big one, change angle depending on the mid,bass and treble of the song, so every song will actually give slightly different rotations for the 
cubes. The small cubes are less reactive at first in comparison to the big one but that can be changed.
The pyramids change hue depending on the song and the soundwaves react to the song being played too.
The visualizer takes a wide range of controls that effect many different parts of the visualizer (see Instructions), including:
- Controls for main centre shape 
- Controls for small cubes
- Mouse clicks to swap main shapes(diamond and cube)
- Controls to change direction of diamond
- Extreme Colour mode toggle
- Fill mode (inverted mode) toggle

These controls and toggles can be used to visualize different parts of the song and make them more harmonious

# What I am most proud of in the assignment

# Markdown Tutorial

This is *emphasis*

This is a bulleted list

- Item
- Item

This is a numbered list

1. Item
1. Item

This is a [hyperlink](http://bryanduggan.org)

# Headings
## Headings
#### Headings
##### Headings

This is code:

```Java
public void render()
{
	ui.noFill();
	ui.stroke(255);
	ui.rect(x, y, width, height);
	ui.textAlign(PApplet.CENTER, PApplet.CENTER);
	ui.text(text, x + width * 0.5f, y + height * 0.5f);
}
```

So is this without specifying the language:

```
public void render()
{
	ui.noFill();
	ui.stroke(255);
	ui.rect(x, y, width, height);
	ui.textAlign(PApplet.CENTER, PApplet.CENTER);
	ui.text(text, x + width * 0.5f, y + height * 0.5f);
}
```

This is an image using a relative URL:

![An image](images/p8.png)

This is an image using an absolute URL:

![A different image](https://bryanduggandotorg.files.wordpress.com/2019/02/infinite-forms-00045.png?w=595&h=&zoom=2)

This is a youtube video:

[![YouTube](http://img.youtube.com/vi/J2kHSSFA4NU/0.jpg)](https://www.youtube.com/watch?v=J2kHSSFA4NU)

This is a table:

| Heading 1 | Heading 2 |
|-----------|-----------|
|Some stuff | Some more stuff in this column |
|Some stuff | Some more stuff in this column |
|Some stuff | Some more stuff in this column |
|Some stuff | Some more stuff in this column |

