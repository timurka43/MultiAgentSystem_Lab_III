# PrisonersDilemmaSimulator
This project is the third in a series of three labs designed to introduce Multi-Agent Systems (MAS) as a foundation for understanding Machine Learning (ML). While these labs do not include ML techniques, they focus on the principles of MAS and basic strategy adjustments based on communication between agents. I completed this project as part of my Object-Oriented Programming, Data Structures, and Algorithms class.

## Description
The simulator consists of two main components:

## Part 1: Network Creation (part1.java)
This program provides a menu-driven interface for users to create four types of networks:

1. Predefined Networks (2 types):

   * Based on examples from Albert-László Barabási's [Network Science](https://networksciencebook.com/chapter/3).

   * These networks demonstrate real-world structural principles, such as scale-free networks.
2. Random Network, where users can specify:
   * The number of nodes.
   * The probability of connection between any two nodes.

3. Lattice Network:
   * Each node is connected to exactly four neighbors, forming a regular lattice structure.

## Output Format:
The generated networks are written to a text file in the following format:

       1 3 // 1 and 3 connected
      
       1 5 //1 and 5 connected
       
       ...
       
       4 -1 //4 is not connected to anything






## Part 2: Game Simulation (part2.java)
This program simulates a modified version of the Prisoner's Dilemma game using a multi-agent framework.

1. User Interaction:
   * A menu allows the user to choose from six predefined game modes or define a custom game mode for simulation.
2. Game Behavior:
   * Agents adapt strategies based on interactions and limited communication, demonstrating fundamental MAS concepts.
