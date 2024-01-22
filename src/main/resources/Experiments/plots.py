import pandas as pd
import matplotlib.pyplot as plt
import csv

df = pd.read_csv("RandomExp/random.csv", header=None, names=["Game_ID", "Winner"])

black_wins = df[df["Winner"] == "black"]["Game_ID"].count()
white_wins = df[df["Winner"] == "white"]["Game_ID"].count()

# pie chart
labels = ['Black Wins', 'White Wins']
sizes = [black_wins, white_wins]
colors = ['lightcoral', 'lightskyblue']
explode = (0.1, 0)

plt.pie(sizes, explode=explode, labels=labels, colors=colors, autopct='%1.1f%%', shadow=True, startangle=140)
plt.axis('equal')

plt.title("Win Distribution Between Two Random Players")
plt.savefig("random_pie_chart.png")
plt.show()


#%%
#  bar chart
players = ['Black', 'White']
win_counts = [black_wins, white_wins]

plt.bar(players, win_counts, color=['lightcoral', 'lightskyblue'])
plt.xlabel("Random Player")
plt.ylabel("Number of Wins")
plt.title("Number of Wins for Each Random Player")
plt.savefig("random_bar_chart.png")
plt.show()
#%%
import pandas as pd
import matplotlib.pyplot as plt

# Read the CSV files
df1 = pd.read_csv('ismctsExp/csv/ismcts_w_VSrandom.csv')
df2 = pd.read_csv('ismctsExp/csv/ismcts_b_VSrandom.csv')

#  Merge the DataFrames
merged_df = pd.concat([df1, df2])

# Plotting the data
# Assuming 'winner' is the column that indicates the winner of the game
winner_counts = merged_df.iloc[:, 1].value_counts()

plt.figure(figsize=(8, 6))
winner_counts.plot(kind='bar')
plt.title('Distribution of Game Winners, ISMCTS vs Random')
plt.xlabel('Winner')
plt.ylabel('Number of Wins')
plt.xticks(rotation=0)
plt.show()
#%%
df1 = pd.read_csv('ismctsExp/csv/ismcts_w_VSrandom.csv')
df2 = pd.read_csv('ismctsExp/csv/ismcts_b_VSrandom.csv')

# Merge the DataFrames
merged_df = pd.concat([df1, df2])

# Count the number of wins for each type
winner_counts = merged_df.iloc[:, 1].value_counts()

# Plotting the data in a pie chart
plt.figure(figsize=(8, 8))
plt.pie(winner_counts, labels=winner_counts.index, autopct='%1.1f%%', startangle=140)
plt.title('Distribution of Game Winners, ISMCTS vs Random')
plt.show()
#%%
import pandas as pd
import matplotlib.pyplot as plt
# Read the CSV files
df = pd.read_csv('alpha_ismctsExp/formatted_a0_vs_ismcts.csv')

# Plotting the data
# Assuming 'winner' is the column that indicates the winner of the game
winner_counts = df.iloc[:, 1].value_counts()

plt.figure(figsize=(8, 6))
winner_counts.plot(kind='bar')
plt.title('Distribution of Game Winners, AlphaISMCTS vs ISMCTS')
plt.xlabel('Winner')
plt.ylabel('Number of Wins')
plt.xticks(rotation=0)
plt.show()
#%%
# Read the CSV files
df = pd.read_csv('alpha_ismctsExp/formatted_a0_vs_ismcts.csv')

# Plotting the data
# Assuming 'winner' is the column that indicates the winner of the game
winner_counts = df.iloc[:, 1].value_counts()
# Plotting the data in a pie chart
plt.figure(figsize=(8, 8))
plt.pie(winner_counts, labels=winner_counts.index, autopct='%1.1f%%', startangle=140)
plt.title('Distribution of Game Winners, AlphaISMCTS vs ISMCTS')
plt.show()
#%%
import pandas as pd
import matplotlib.pyplot as plt

# File paths
file_paths = [
    'ismctsExp/iter50.csv',
    'ismctsExp/iter100.csv',
    'ismctsExp/iter500.csv',
    'ismctsExp/iter1000.csv',
    'ismctsExp/iter5000.csv'
]

# Labels for iterations
iter_labels = ['50', '100', '500', '1000', '5000']

# Store the counts of ISMCTS wins for each iteration setting
ismcts_wins = []

for file_path in file_paths:
    df = pd.read_csv(file_path)
    # Assuming the second column contains the winner ("ismcts" or "random")
    count = df.iloc[:, 1].value_counts().get('ismcts', 0)
    ismcts_wins.append(count)

# Plotting with the number of wins on the bars
plt.figure(figsize=(10, 6))
bars = plt.bar(iter_labels, ismcts_wins, color='blue')

# Adding the number of wins on top of each bar
for bar in bars:
    yval = bar.get_height()
    plt.text(bar.get_x() + bar.get_width()/2, yval, f'{yval}', ha='center', va='bottom')

plt.xlabel('Max Iterations')
plt.ylabel('Number of ISMCTS Wins')
plt.title('ISMCTS Wins at Different Iteration Settings vs Random')
plt.show()
#%%
import pandas as pd
import matplotlib.pyplot as plt

# Load the data
file_path = 'ismctsExp/csv/explore.csv'
df = pd.read_csv(file_path)

# Unique exploration rates
exploration_rates = df['EXPL'].unique()

# Sort the exploration rates to ensure they are in order
exploration_rates.sort()

# Store the counts of ISMCTS wins for each exploration rate
ismcts_wins = []

for rate in exploration_rates:
    # Filter the DataFrame for each exploration rate and count ISMCTS wins
    count = df[df['EXPL'] == rate]['Winner'].value_counts().get('ismcts', 0)
    ismcts_wins.append(count)

# Plotting with the number of wins on the bars
plt.figure(figsize=(10, 6))
bars = plt.bar([str(rate) for rate in exploration_rates], ismcts_wins, color='blue')

# Adding the number of wins on top of each bar
for bar in bars:
    yval = bar.get_height()
    plt.text(bar.get_x() + bar.get_width()/2, yval, f'{yval}', ha='center', va='bottom')

plt.xlabel('Exploration Rate (EXPL)')
plt.ylabel('Number of ISMCTS Wins')
plt.title('ISMCTS Wins at Different Exploration Rates vs Random')
plt.xticks(rotation=45)
plt.show()
