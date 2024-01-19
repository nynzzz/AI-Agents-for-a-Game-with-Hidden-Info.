import numpy as np
import pandas as pd

df = pd.read_csv("testdata.csv", header=None, names=["Game Number", "Board State", "Player to Move", "Visit Counts", "Move to Take", "Value", "Game Result"])

# Extract only the "Board State" and "Value" columns
selected_columns = ["Board State", "Value"]
df_selected = df[selected_columns]

# Extract values from the "Board State" and "Value" columns in df_selected
board_states = df_selected["Board State"].values
values = df_selected["Value"].values

for value in values:
    if isinstance(value, float) and np.isnan(value):
        print("The value is a NaN.")
    elif isinstance(value, str) and value.strip() == "":
    # Change the value to 0 if it's empty
        value = 0
        print("The value is empty, but now it's set to 0.")
    else:
        print("The value is:", value)

# Check the shapes of the training and validation data
print(values)