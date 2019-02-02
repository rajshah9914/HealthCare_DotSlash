import matplotlib as plt
year = [1960, 1970, 1980, 1990, 2000, 2010]
health_values = [44.91, 58.09, 78.07, 107.7, 138.5, 170.6]
plt.plot(year, health_values, color='g')
plt.xlabel('Time')
plt.ylabel('Health')
plt.title('Patient Health Tracking')
plt.show()