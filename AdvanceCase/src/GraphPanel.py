import matplotlib.pyplot as plt
from com.simulation.lift.api.CostDict import cost_dict

fig = plt.figure()
fig.canvas.set_window_title('Advance Case Graph')
fig.suptitle("""Cost
in function of 
Lift Capacity, Floor Number and Passenger Number""")

ax = fig.add_subplot(111, projection='3d')

x = []
y = []
z = []
c = []

for lift_capacity in cost_dict:
    for floor_number in cost_dict[lift_capacity]:
        for passenger_number in cost_dict[lift_capacity][floor_number]:
            x.append(lift_capacity)
            y.append(floor_number)
            z.append(passenger_number)
            c.append(cost_dict[lift_capacity][floor_number][passenger_number]["cost"])

img = ax.scatter(x, y, z, c=c, cmap=plt.hot())
cbar = fig.colorbar(img)
cbar.ax.get_yaxis().labelpad = 15; cbar.ax.set_ylabel("Cost", rotation = 270);
ax.set_xlabel('Lift Capacity')
ax.set_ylabel('Floor Number')
ax.set_zlabel('Passenger Number')

plt.show()
