import pandas as pd
import numpy as np


def encode_board_state(board_state_str):
    board_array = np.zeros((8, 8, 3))
    for i, char in enumerate(board_state_str):
        row, col = divmod(i, 8)
        if char == 'w':
            board_array[row, col, 0] = 1
        elif char == 'b':
            board_array[row, col, 1] = 1
        else:
            board_array[row, col, 2] = 1
    return board_array


def backpropagate_values_revised(data):
    for game_id in data['Game Number'].unique():
        game_data = data[data['Game Number'] == game_id]
        terminal_state = game_data.iloc[-1]
        game_result = terminal_state['Game Result']

        if pd.isna(game_result):
            continue

        for idx in game_data.index[:-1]:  # Exclude the terminal state
            current_player = game_data.at[idx, 'Player to Move']
            value = 1 if current_player == game_result else -1
            data.at[idx, 'value'] = value

    data['value'].fillna(0, inplace=True)
    return data


def preprocess_data(file_path):
    data = pd.read_csv(file_path)

    # First backpropagate values for the entire DataFrame
    data = backpropagate_values_revised(data)

    # Then encode 'Board State'
    data['boardstate'] = data['Board State'].apply(encode_board_state)

    # Select only the relevant columns
    final_data = data[['boardstate', 'value']]
    return final_data


# file_path = '../../main/resources/NN training data/mergedTrainingData.csv'
# processed_data = preprocess_data(file_path)
# print(processed_data)
