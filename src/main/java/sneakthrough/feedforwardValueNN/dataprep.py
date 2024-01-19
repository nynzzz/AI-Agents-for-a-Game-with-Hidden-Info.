import numpy as np
import pandas as pd

# Read the data from the CSV file
df = pd.read_csv("testdata.csv", header=None, names=["Game Number", "Board State", "Player to Move", "Visit Counts", "Move to Take", "Value", "Game Result"])

# Extract only the "Board State" and "Value" columns
selected_columns = ["Board State", "Value"]
df_selected = df[selected_columns]

# Extract values from the "Board State" and "Value" columns in df_selected
board_states = df_selected["Board State"].values
values = df_selected["Value"].values

# Split data into training and validation sets
split_index = int(len(board_states) * 0.8)
train_states, val_states = board_states[:split_index], board_states[split_index:]
train_outcomes, val_outcomes = values[:split_index], values[split_index:]

# Check the shapes of the training and validation data
print(df_selected)
print("Training data shape:", train_states)
print("Validation data shape:", val_states)
