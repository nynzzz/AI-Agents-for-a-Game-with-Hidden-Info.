from flask import Flask, request, jsonify
import torch
import torch.nn as nn
import numpy as np
from scipy.special import softmax

app = Flask(__name__)

class LSTM(nn.Module):
    def __init__(self, input_size, hidden_size, output_size,  num_layers):
        super(LSTM, self).__init__()
        self.hidden_size = hidden_size
        
        #LSTM layer
        self.lstm = nn.LSTM(input_size, hidden_size, num_layers, batch_first=True) 
        
        #fully connected layer
        self.fc = nn.Linear(hidden_size, output_size)
        
    def forward(self, x):
        #forward pass through LSTM layer
        lstm_out, _ = self.lstm(x)
        
        #get last output step
        last_output = lstm_out[:, -1, :]
        
        #forward pass through fully connected layer
        output = self.fc(last_output)
        
        return output

input_size = 64
hidden_size = 32
output_size = 2 #score a game state 
num_layers = 2

model = LSTM(input_size=input_size, hidden_size=hidden_size, output_size=output_size, num_layers=num_layers)

checkpoint_path = '/Users/nguyenhoanghai/Documents/GitHub/Project_2.1_06/src/main/resources/ML/LSTM/RNN_checkpoint.pth'
model.load_state_dict(torch.load(checkpoint_path))

model.eval()

def encode_board_state(board_state, sequence_length=18, input_size=64):
    #convert the single board state to numerical representation
    single_state_num = [convert_char_to_num(char) for char in board_state]

    #repeat the single board state to fill the sequence
    repeated_states = single_state_num * sequence_length

    if len(repeated_states) > sequence_length * input_size:
        repeated_states = repeated_states[:sequence_length * input_size]

    #convert to tensor and reshape
    board_tensor = torch.tensor(repeated_states, dtype=torch.float32).view(1, sequence_length, input_size)

    return board_tensor


def convert_char_to_num(char):
    if char == '0':
        return 0
    elif char == 'b':
        return -1
    elif char == 'w':
        return 1
    
    return 0 

@app.route('/evaluate', methods=['POST'])
def evaluate():
    data = request.json
    board_state = data['board_state'] 

    encoded_state = encode_board_state(board_state)

    with torch.no_grad():
        logits = model(encoded_state)
        probabilities = softmax(logits.numpy(), axis=1)[0]  

    return jsonify({'winProbability': probabilities[1].item(), 'lossProbability': probabilities[0].item()})



if __name__ == '__main__':
    app.run(debug=True, host='localhost', port=5000)

