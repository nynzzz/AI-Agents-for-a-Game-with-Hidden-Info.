import pandas as pd
from sklearn.metrics import classification_report, confusion_matrix
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import OneHotEncoder, MinMaxScaler
import numpy as np
import torch
import torch.nn as nn       

def in_num(piece):
    piece_in_num = {'b': -1, '0': 0, 'w': 1}
    return piece_in_num.get(piece, 0)  

def parsing_board(board_string):
    board_string = str(board_string).replace('nan', '0')
    board = [in_num(piece) for piece in board_string]  
    return board

def preprocess(file_path):
    data = []
    df = pd.read_csv(file_path)  
    
    for index, row in df.iterrows():
        gameID = row['Game Number']
        board_state = parsing_board(row['Board State'])
        current_player = in_num(row['Player to Move'])
        winner = 1 if row['Game Result'] == 'w' else 0
        
        data.append((gameID, board_state, current_player, winner))
    
    return data

file_path = "/Users/nguyenhoanghai/Downloads/full.csv"
preprocessed_data = preprocess(file_path)
print(len(preprocessed_data))

#initialize neutral state for padding
pad_state = ([0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], 0, 0)
#initialize parameters
input_size = 64
hidden_size = 16
output_size = 2 #score a game state 
num_layers = 2
learning_rate = 0.0005
num_epochs = 500
batch_size = 64
sequence_length = 18

#setting up the lenght of the sequence to match the fixed lenght
def fix_length_of_sequence(game_sequence):
    #if shorter than the fixed lenght pad the sequence
    if len(game_sequence) < sequence_length :
        padding = [pad_state] * (sequence_length - len(game_sequence))
        game_sequence = padding + game_sequence
        
    #if longer then deduct 
    elif len(game_sequence) > sequence_length :
        game_sequence = game_sequence[-sequence_length:]
    return game_sequence


#group by gameID so we get sequence of plays
game_sequences = {}  
for game_id, board_state, current_player, result in preprocessed_data:
    if game_id not in game_sequences:
        game_sequences[game_id] = []  
    game_sequences[game_id].append((board_state, current_player, result))

#fix the length of each game sequence
fixed_length_game_sequences = {game_id: fix_length_of_sequence(seq) for game_id, seq in game_sequences.items()}

#combine datasets for splitting 
all_sequences = list(fixed_length_game_sequences.values())

#splitting into test and train
train_sequences, test_sequences = train_test_split(all_sequences, test_size=0.34, random_state=10)


#extract board and winners from game sequence
def extract_board_and_winners(game_sequences) :
    features = []
    labels = []
    
    for game in game_sequences :
        #extract board states
        game_features = [step[0] for step in game]  
        features.append(game_features)

        #extract winners 
        game_label = game[-1][2]  
        labels.append(game_label)

    return features, labels

train_features, train_labels = extract_board_and_winners(train_sequences)
test_features, test_labels = extract_board_and_winners(test_sequences)


#convert into torch
train_features = torch.tensor(train_features, dtype=torch.float32).view(-1, sequence_length, input_size) #shape it for LSTM
train_labels = torch.tensor(train_labels, dtype=torch.int64)

test_features = torch.tensor(test_features, dtype=torch.float32).view(-1, sequence_length, input_size) #shape it for LSTM
test_labels = torch.tensor(test_labels, dtype=torch.int64)
        

class LSTM(nn.Module):
    def __init__(self, input_size, hidden_size, output_size,  num_layers, dropout_p):
        super(LSTM, self).__init__()
        self.hidden_size = hidden_size
        
        #LSTM layer
        self.lstm = nn.LSTM(input_size, hidden_size, num_layers, batch_first=True, dropout = dropout_p) 

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
    
model = LSTM(input_size=input_size, hidden_size=hidden_size, output_size=output_size, num_layers=num_layers, dropout_p =0.3)

#optimizer and Loss function 
criterion = nn.CrossEntropyLoss()
optimizer = torch.optim.Adam(model.parameters(), lr=learning_rate) #adam optimizer

best_val_loss = float('inf')
patience = 50 
patience_counter = 0

train_losses = []
val_losses = []
train_accuracies = []
val_accuracies = []

for epoch in range(num_epochs):
    model.train()
    total_loss = 0
    correct = 0
    total = 0
    for i in range(0, len(train_features), batch_size):
        #batch data
        batch_features = train_features[i:i+batch_size]
        batch_labels = train_labels[i:i+batch_size]

        #forward pass
        outputs = model(batch_features)
        loss = criterion(outputs, batch_labels)

        #backward and optimize
        optimizer.zero_grad()
        loss.backward()
        optimizer.step()

        total_loss += loss.item()
        _, predicted = torch.max(outputs.data, 1)
        total += batch_labels.size(0)
        correct += (predicted == batch_labels).sum().item()

    train_losses.append(total_loss / len(train_features))
    train_accuracies.append(100 * correct / total)

    #validation phase
    model.eval()
    val_loss = 0
    correct = 0
    total = 0
    with torch.no_grad():
        for i in range(0, len(test_features), batch_size):
            batch_features = test_features[i:i+batch_size]
            batch_labels = test_labels[i:i+batch_size]

            outputs = model(batch_features)
            loss = criterion(outputs, batch_labels)
            val_loss += loss.item()
            _, predicted = torch.max(outputs.data, 1)
            total += batch_labels.size(0)
            correct += (predicted == batch_labels).sum().item()

    val_losses.append(val_loss / len(test_features))
    val_accuracies.append(100 * correct / total)

    print(f'Epoch [{epoch+1}/{num_epochs}], Train Loss: {train_losses[-1]}, Validation Loss: {val_losses[-1]}')
    print(f'Train Accuracy: {train_accuracies[-1]}, Validation Accuracy: {val_accuracies[-1]}')

    #early stopping
    if val_losses[-1] < best_val_loss:
        best_val_loss = val_losses[-1]
        patience_counter = 0 
        #torch.save(model.state_dict(), '/Users/nguyenhoanghai/Documents/GitHub/Project_2.1_06/src/main/resources/ML/LSTM/Rnn_checkpoint.pth')
    else:
        patience_counter += 1
        if patience_counter > patience:
            print('Early stopping!')
            break  

import matplotlib.pyplot as plt

#plotting the training and validation loss
plt.figure(figsize=(10, 5))
plt.title("Training and Validation Loss")
plt.plot(train_losses, label="Train Loss")
plt.plot(val_losses, label="Validation Loss")
plt.xlabel("Epochs")
plt.ylabel("Loss")
plt.legend()
plt.show()

#plotting the training and validation accuracy
plt.figure(figsize=(10, 5))
plt.title("Training and Validation Accuracy")
plt.plot(train_accuracies, label="Train Accuracy")
plt.plot(val_accuracies, label="Validation Accuracy")
plt.xlabel("Epochs")
plt.ylabel("Accuracy")
plt.legend()
plt.show()