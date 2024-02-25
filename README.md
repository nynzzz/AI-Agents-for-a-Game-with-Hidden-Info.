## AI Agents for a Game with Hidden Information

# Description:

Sneakthrough is a chess variation created specifically for Ludii. It shares similarities with BreakThrough, but in
Sneakthrough, there's a unique twist involving the opponent's pieces.

# Rules:

- When a piece is captured, it becomes visible to the opponent.
- If you attempt to make an orthogonal move to a square containing an opponent's hidden piece, the move is halted, and the enemy piece is uncovered.
- The objective is to advance to the opposite side of the board, similar to the goal in Breakthrough.

# To run the code:

- Open the executable JAR `Project_2.1_06.jar`
- JAR is located at: `out/artifacts/Project_2_1_06_jar/Project_2.1_06.jar`

Or

- build the project
- run `src/main/java/sneakthrough/Main.java`


### If you want to use AlphaISMCTS, you will first need to set up the eviroment:

1. Open terminal from project directory
2. Create a Python virtual environment:`python3 -m venv virtualPy`. Make sure the python is the one instaled on your machine (python3 in this case).
3. Activate the newly created virtual environment:
   1. On Unix or MacOS:`source virtualPy/bin/activate`
   2. On Windows:`virtualPy\Scripts\activate`
4. Install the necessary Python packages within your virtual environment: `pip install flask numpy tensorflow`
5. Modify the Python Script to Load the Model:
   1. Edit `model_server.py` to update the path to your model file.
   2. the model can be trained and saved by running `Prep_and_Model`.
6. Navigate to the directory containing `model_server.py` and run: `python3 model_server.py`. This launches a server that will host the model.
7. You can now use AlphaISMCTS
