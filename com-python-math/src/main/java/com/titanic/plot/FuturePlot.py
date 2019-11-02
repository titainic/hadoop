import pandas as pd
import json
import matplotlib.pyplot as plt
import seaborn as sns

biz_file = open('/home/titanic/soft/intellij_workspace/github-hadoop/com-python-math/src/main/resources/yelp_academic_dataset_business2.json')
biz_df = pd.DataFrame([json.loads(x) for x in biz_file.readline()])

sns.set_style('whitegrid')
fig,ax = plt.subplots()
biz_df['review_count'].hist(ax=ax,bins=100)
ax.set_yscale('log')
ax.tick_params(labelsize=14)
ax.set_xlabel('Review Count',fontdict=14)
ax.set_ylabel('Occurrence',fontdict=14)
biz_file.close()

