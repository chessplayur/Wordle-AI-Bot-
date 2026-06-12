# WordSolver

A command-line Wordle solver written in Java. Given feedback from each guess, it narrows down candidates and suggests the best next word to play.

## How It Works

1. The solver starts with a fixed opening guess (`SLATE`).
2. You enter the color-coded feedback Wordle gives you after each guess.
3. The solver filters its word list to only the words still consistent with all feedback received.
4. It scores the remaining candidates by letter frequency and suggests the highest-scoring one next.
5. Repeat until the word is found (`GGGGG`).

## Setup

**Requirements:** Java 8 or later.

1. Clone or download this repository.
2. Place a `words.txt` file in the same directory as the compiled class. Each line should contain one word; only 5-letter words are used.
3. Compile and run:

```bash
javac WordSolver.java
java WordSolver
```

## Usage

```
=====================================
          WORDLE SOLVER
=====================================

Feedback format:
G = Green  (correct letter, correct position)
Y = Yellow (correct letter, wrong position)
B = Gray   (letter not in the word)

Example: BBGYB

-------------------------------------
Round #1
Suggested Guess: SLATE
-------------------------------------
Enter feedback: BYBBB
```

- Type the 5-character feedback string (e.g. `BGYBB`) and press Enter.
- If 20 or fewer candidates remain, they are printed so you can see your options.
- Enter `GGGGG` when the puzzle is solved.

## Algorithm

**Filtering** — After each guess, every remaining candidate is tested by simulating what feedback that word *would have* produced. Any word that wouldn't produce the exact feedback you received is eliminated.

**Scoring** — The next guess is chosen from the surviving candidates by letter-frequency scoring: each unique letter in a word earns points equal to how many candidates contain that letter, rewarding guesses that are most likely to give useful information.

## Project Structure

```
WordSolver.java   # All solver logic
words.txt         # Word list (you supply this)
README.md         # This file
```

## Customization

| Change | Where |
|---|---|
| Opening guess | `FIRST_GUESS` constant at the top of `WordSolver.java` |
| Word list | Replace `words.txt` with any newline-separated word file |
| Candidate display threshold | The `<= 20` check in `main()` |
