# Latte
[![Build and test](https://github.com/flatscrew/latte/actions/workflows/build-and-test.yml/badge.svg)](https://github.com/flatscrew/latte/actions/workflows/build-and-test.yml) [![Maven Central](https://img.shields.io/maven-central/v/org.flatscrew/latte-tui)](https://central.sonatype.com/artifact/org.flatscrew/latte-tui)

<img src="images/latte-logo.png" width="250">

> Artwork by [m.art.i](https://www.instagram.com/m.art.i)

Latte tends to be a Java port of Golang's [Bubble Tea](https://github.com/charmbracelet/bubbletea),
[The Elm Architecture](https://guide.elm-lang.org/architecture/) driven TUI framework.

![Demo](latte-tui-examples/demo-tape.gif "Demo")

## Porting status
Check out [this page](STATUS.md) to see porting status.

## Tutorial
For this tutorial, we're making a coffee ordering application (by the way, the non-annotated source code for this program is available [on GitHub](https://github.com/flatscrew/latte/tree/main/latte-tui-examples/src/main/java/org/flatscrew/latte/examples/demo)).

Everything starts with a **model** - an implementation of `org.flatscrew.latte.Model` interface, that describes the application state and three simple methods on that model:

- **init**, a method that returns an initial command for the applicatin to run,
- **update**, a method that handles incoming events and updates theh model accordingly,
- **view**, that renders the UI based on the data in the model.
  
### The Model
So let's start by deifning our model which will store our application's state. It can be any time imlementing `org.flatscrew.latte.Model` interface.

```java
import org.flatscrew.latte.Model;

public class Demo implements Model {
    private final static String[] CHOICES = {"Espresso", "Americano", "Latte"};

    private int cursor;
    private String choice;
}
```

### Initialization
Next, we'll define our application's initial state. In this case, we're defining a variable pointing to model instance, we could just as easily define a method to return our initial model, too.

```java
Demo demoModel = new Demo();
```

Next, we implement the `init` method. It can return a `Command` that could perform some initial I/O. For now, we don't need to do any I/O, so for the command, we'll just return `null`, which traslates to "no command".

```java
@Override
public Command init() {
    return null;
}
```

### The update method
Next up is the update method. It is called when "things happen". Its job is to look at what has happened and return an updated model in response. It can also return a `Command` to make more things happem, but for now don't worry about that part.

In our case, whwn a user presses the down arrow, `update`'s job is to notice that the down arrow was pressed and move the cursor accordingly (or not).

The "somethning happened" comes ni the form of a `Message`, which can be any type that implements `org.flatscrew.latte.Message` interface. Messages are the result of some I/O that took place, such as keypress, timer tick, or a response from a server.

We usually figure out which type of `Message` we received with a regular if but you can obviously come up with more sophisticated switch.

For now, we'll just deal with `KeyPressMessage` messages, which are automatically sent to the update function when keys are pressed.

```java
@Override
public UpdateResult<? extends Model> update(Message msg) {
    // is this a key press?
    if (msg instanceof KeyPressMessage keyPressMessage) {
        // cool, what whas the actual key pressed?
        return switch (keyPressMessage.key()) {
            // the "up" and "k" keys move the cursor up
            case 'k', 'K', 65 -> new UpdateResult<>(this.moveUp(), null);

            // the "down" and "j" keys move the cursor down
            case 'j', 'J', 66 -> new UpdateResult<>(this.moveDown(), null);
            
            // the "enter" and the spacebar (a literal space) toggle
            // the selected state for the item that the cursor is pointing at.
            case 13, ' ' -> new UpdateResult<>(this.makeChoice(), QuitMessage::new);
            
            // this key should exit the program
            case 'q', 'Q' -> new UpdateResult<>(this, QuitMessage::new);
            default -> new UpdateResult<>(this, null);
        };
    }

    // return the updated model to Latte for processing
    return new UpdateResult<>(this, null);
}
```
You may have noticed that `q` above returns `QuitMessage` command with the model. That's a special message which intructs the Latte runtime to quit, exiting the program.

### The view method
At last, it's time to render our UI. Of all the methods, the view is the simplest. We look at the model in its current state and use it to return a `String`. That String is our UI!

Because the view describes the entire UI of rour application, you don't have to worry about redrawing logic and stuff like that. Latte takes care of it for you.

```java
@Override
public String view() {
    StringBuilder buffer = new StringBuilder();
    buffer.append("What kind of Coffee would you like to order?\n\n");

    for (int index = 0; index < CHOICES.length; index++) {
        if (cursor == index) {
            buffer.append(SELECTION.render("[•]", CHOICES[index]));
        } else {
            buffer.append("[ ] ").append(CHOICES[index]);
        }
        buffer.append("\n");
    }
    buffer.append("\n(press q to quit)");
    return buffer.toString();
}
```

### All together now
The last setp is to simply run our program. We pass oir initial model as an argument for new instance of `org.flatscrew.latte.Program` and call `run` method.

```java
public static void main(String[] args) {
    Demo demoModel = new Demo();
    Program program = new Program(demoModel);
    program.run();

    if (demoModel.getChoice() == null) {
        return;
    }
    System.out.printf("\n---\nYou chose: %s!\n", demoModel.getChoice());
}
```