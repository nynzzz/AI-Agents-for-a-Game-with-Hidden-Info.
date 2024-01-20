from flask import Flask, request, jsonify
import numpy as np
import tensorflow as tf

app = Flask(__name__)

# Load model (adjust the path to your model)
model = tf.keras.models.load_model('model_epoch_50.h5')

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

def calculate_move_index(old_pos, new_pos):
    base_index = (old_pos[0] * 8 + old_pos[1]) * 3
    row_diff = new_pos[0] - old_pos[0]
    col_diff = new_pos[1] - old_pos[1]
    if row_diff == -1 or row_diff == 1:
        if col_diff == -1:
            return base_index  # Diagonal left
        elif col_diff == 0:
            return base_index + 1  # Straight
        elif col_diff == 1:
            return base_index + 2  # Diagonal right
    return -1  # Invalid move

def map_policy_to_moves(policy_vector, possible_moves):
    move_policy_dict = {}
    total_policy_value = sum(policy_vector)
    for move in possible_moves:
        old_pos, new_pos = move
        index = calculate_move_index(old_pos, new_pos)
        move_policy_dict[str(move)] = policy_vector[index] / total_policy_value
    return move_policy_dict

@app.route('/predict', methods=['POST'])
def predict():
    data = request.json
    board_state_str = data['board_state']
    possible_moves_str = data['possible_moves']

    possible_moves = [((int(move.split(':')[0].split(',')[0]), int(move.split(':')[0].split(',')[1])),
                       (int(move.split(':')[1].split(',')[0]), int(move.split(':')[1].split(',')[1])))
                      for move in possible_moves_str.split(';') if move]

    board_state = encode_board_state(board_state_str)
    board_state = np.expand_dims(board_state, axis=0)

    predictions = model.predict(board_state)
    policy_vector, value = predictions[0][0], predictions[1][0]

    # Convert numpy float32 to native Python float for JSON serialization
    policy_vector = policy_vector.astype(float)
    value = float(value[0])

    mapped_policy = map_policy_to_moves(policy_vector, possible_moves)

    return jsonify({'policy': mapped_policy, 'value': value})


if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0', port=5001)
