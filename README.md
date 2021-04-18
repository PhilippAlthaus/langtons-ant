# Langton's Ant

Langton's ant is a two-dimensional Turing machine. Squares on a grid are colored variously either black or white.
The ant can travel in any direction at each step it takes. The rules are as follows:

- At a white square, turn 90° clockwise, flip the color of the square, move forward one unit
- At a black square, turn 90° counter-clockwise, flip the color of the square, move forward one unit

This original version has been extended where instead of just two colors, more colors are used. The colors are modified in a cyclic fashion.
A simple naming scheme is used: for each of the successive colors, a letter "L" or "R" is used to indicate whether a left or right turn should be taken.
Langton's ant has the name "RL" in this naming scheme. This implementation currently supports up to 12 colors.