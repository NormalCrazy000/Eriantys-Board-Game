# Eriantys Board Game

<img src="https://cf.geekdo-images.com/DzhJxVjMhGQadReXJmbIaQ__imagepage/img/v5nt1jOW9d5v_Hy0nUv81AIE8tk=/fit-in/900x600/filters:no_upscale():strip_icc()/pic6253341.jpg" width=200px height=200px align="right" />

Eriantys Board Game is the final test of **"Software Engineering"**, course of **"Computer Science Engineering"** held at Politecnico di Milano (2021/2022).

**Teacher** San Pietro Pierluigi

**Final Score**: 30

## The Team
* [Gabriele Giannotto](https://github.com/NormalCrazy000)
* [Christian Lisi](https://github.com/Belfagor99)
* [Maria Pia Marini](https://github.com/piamarini99)

## Project specification
The project consists of a Java version of the board game *Eriantys*, made by Cranio Creation.

You can find the full game [here](https://www.craniocreations.it/prodotto/eriantys/).

The final version includes:
* initial UML diagram;
* final UML diagram, generated from the code by automated tools;
* working game implementation, which has to be rules compliant;
* source code of the implementation;
* source code of unity tests.

## Implemented Functionalities
| Functionality       | Status |
|:--------------------|:------:|
| Basic rules         |   ✅    |
| Complete rules      |   ✅    |
| Socket              |   ✅    |
| GUI                 |   ✅    |
| CLI                 |   ✅    |
| Multiple games      |   ✅    |
| All Character cards |   ✅    |
| 4 player games      |   ⛔    |
| Persistence         |   ⛔    |


#### Legend
⛔ Not Implemented &nbsp;&nbsp;&nbsp;&nbsp;⚠️ Implementing&nbsp;&nbsp;&nbsp;&nbsp;✅ Implemented


## Test cases
All tests in model and controller has a classes' coverage at 100%.


| Package         | Class Criteria | Method Criteria | Line Criteria | 
|:----------------|:---------------|:---------------:|:-------------:|
| Model           | 27/27 (100%)   |  217/220 (95%)  | 738/749 (98%) |
| Controller      | 1/1 (100%)     |  20/20 (100%)   | 94/94 (100%)  |



## Compilation and packaging

The jar is located inside the `` deliveries / jar / '' folder, it can be downloaded from the repo and executed directly via terminal

Instead, to compile the jar yourself, go to the root of the project and execute the maven command:
```
mvn clean install
```

The compiled jar will be placed inside the folder ```target/```

## Execution
This project requires Java version 17 or higher to run correctly.
To run the jar, type in terminal the command :
```
java -jar <jar-name>.jar
```

At this point the application will ask the user if he wants to start a server or a client with CLI or GUI interface.




