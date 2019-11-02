import numpy as np

xs = np.arange(-1,1,0.01)
ys =[((x **2 -1) ** 3+0.5) * np.sin(x*2) for x in xs]
ysl = []

for i in range(len(ys)):
    z = np.random.randint(low=-10,high=10)/100
    ysl.append(ys[i]+z)

y = np.array(ysl).reshape(200,1)
x = xs.reshape(200,1)
# y = ysl.reshape(200,1)
c = np.hstack((x,y))
print(c)
