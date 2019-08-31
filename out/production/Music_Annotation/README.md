# Music_Annotation

By using different mouse dragging gesture, the user can paint the music tabs on the cumputer instead of choosing one note from the tool bar.

1. Music class is the main class for use.

2. There are 6 kinds of gestures defined in the codes,

   | Name  | Result                                                       |
   | ----- | ------------------------------------------------------------ |
   | E-E   | 1. Create one line of tab 2. Increase the flags or dots on a note. |
   | W-W   | Decrease the dots or flags on a note.                        |
   | S-S   | Create and change dividing line type.                        |
   | SW-SW | Create a dot note.                                           |
   | E-S   | Create a regular note.                                       |

3. You can train your own gestures based on your habit with these fixed names in ShapeTrainer class. 

   a. Press ```space``` to clear the name input.

   b. Type the gesture name you want above.

   c. Draw the line with one drag.

   d. Press ```Enter``` to confirm the gesture and continue the next.