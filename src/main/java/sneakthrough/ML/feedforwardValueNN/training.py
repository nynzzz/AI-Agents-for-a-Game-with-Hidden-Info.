from model import model
from dataprep import preprocess_data
import numpy as np
from tensorflow.keras.optimizers import Adam
import os


# Function to load and preprocess data
def load_and_preprocess_data(file_path):
    processed_data = preprocess_data(file_path)

    # Extract features and labels
    X = np.array(processed_data['boardstate'].tolist())  # Convert list of arrays to numpy array
    y = processed_data['value'].values

    return X, y


script_dir = os.path.dirname(__file__)
relative_path = '../../main/resources/NN training data/mergedTrainingData.csv'
file_path = os.path.abspath(os.path.join(script_dir, relative_path))
file_path = file_path.replace('\\', '/')
print(file_path)

# Load data from CSV files
X_train, y_train = load_and_preprocess_data('D:/project 2-1 sneakthrough/src/main/resources/NN training data/mergedTrainingData.csv')
# Similarly, load validation and test data if available

# Compile the model
model.compile(optimizer=Adam(learning_rate=0.001), loss='mean_squared_error', metrics=['accuracy'])

# Train the model
model.fit(X_train, y_train, epochs=200, batch_size=64)  # Adjust these parameters as needed

# Evaluate the model
# model.evaluate(X_test, y_test)  # If you have a test set

# Save the model
model.save('../../../sneakthrough/ML/feedforwardValueNN')
