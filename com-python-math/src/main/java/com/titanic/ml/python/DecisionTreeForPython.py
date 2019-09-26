
def createDataSet():
    dataSet = [[1, 1, 'yes'],
               [1, 1, 'yes'],
               [1, 0, 'no'],
               [0, 1, 'no'],
               [0, 1, 'no']]
    labels = ['no surfacing', 'flippers']
    return dataSet, labels


dataset,label=createDataSet()
print(dataset)
print(label)


'''计算数据集的香农熵(信息期望值):熵越高表示混合数据越多，度量数据集无序程度'''
# def calcShannonEnt(dataSet):
#     # 需要对 list 中的大量计数时,可以直接使用Counter,不用新建字典来计数
#     label_count = Counter(data[-1] for data in dataSet) # 统计标签出现的次数
#     probs = [p[1] / len(dataSet) for p in label_count.items()] # 计算概率
#     shannonEnt = sum([-p * log(p, 2) for p in probs]) # 计算香农熵
#     print(Decimal(shannonEnt).quantize(Decimal('0.00000')))
#     return shannonEnt