# maze_generator

This is a simple Maze generator that uses a randomized depth first search algorithm.

## How to run

```
  javac MazeGen.java
  java MazeGen 10  0 (Will Create a Maze 10 x 10 in size recursively)

  java MazeGen 10  1 (Will Create a Maze 10 x 10 in size Iiteratively) 
```

Mazes over 30 x 30 recursively tend to result in stack overflow issues.
