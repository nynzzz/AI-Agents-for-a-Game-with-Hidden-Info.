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
