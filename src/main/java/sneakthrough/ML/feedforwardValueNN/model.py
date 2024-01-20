import tensorflow as tf
from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import Dense

# Define the model
model = Sequential([
    Dense(64, input_shape=(64,), activation='relu'),  # Input layer: 64 inputs representing the board state
    Dense(64, activation='relu'),                     # First hidden layer
    Dense(64, activation='relu'),                     # Second hidden layer
    Dense(1, activation='sigmoid')                    # Output layer: single neuron with sigmoid activation
])

# Compile the model
model.compile(optimizer='adam', loss='mean_squared_error', metrics=['accuracy'])

# Model summary
model.summary()
