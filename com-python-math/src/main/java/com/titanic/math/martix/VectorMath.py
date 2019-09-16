import numpy as np

u = np.array([[1, 2, 3]]).T
v = np.array([[5, 6, 7]]).T

'向量相加'
print(u + v)

'向量*数'
print(3 * u.T)

uv = np.array([3, 5, 2])
vv = np.array([1, 4, 7])

'向量內积'
print(np.dot(uv, vv))

uc = np.array([3, 5])
vc = np.array([1, 4])

'二维外积'
print(np.cross(uc,vc))


ux = np.array([[1,2,3]]).T
vx = np.array([[4,5,6]]).T
wx = np.array([[7,8,9]]).T

'向量的线性组合'
print(3*ux+4*vx+5*wx)


