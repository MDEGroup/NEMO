import pandas as pd
import numpy as np
from matplotlib import pyplot as plt
DATASET_NAME = "datasetBPMN2.csv"

df = pd.read_csv(DATASET_NAME, engine='python', header=None)
df[3] = True
print (len(df))
dfi = df.head(50000)
dfm = df.iloc[590000:620000]
dff = df.iloc[-50000:]
# print(len(df))
vi = dfi.groupby([0]).count()
vm = dfm.groupby([0]).count()
vf = dff.groupby([0]).count()

#print(dataframe.nunique(axis=1))
print("======")
mi = {}
mm = {}
mf = {}
for index, row in vi.iterrows():
    mi[row.name] = row.values[0]

for index, row in vm.iterrows():
    mm[row.name] = row.values[0]

for index, row in vf.iterrows():
    mf[row.name] = row.values[0]

values = [1,4,6,15,45,46,47,49,50]

li = []
lm = []
lf = []
lv = ["create", "setAtt", "addRef", "setRef", "rmRef", "unsRef", "delete", "mvRef", "unsAtt"]
for value in values:
    li.append(0 if not value in mi else mi[value])
    lm.append(0 if not value in mm else mm[value])
    lf.append(0 if not value in mf else mf[value])
#    lv.append(value)
df2 = pd.DataFrame({ "Di": li, "Dm": lm, "Df": lf}, index = lv)
# df2.plot(kind = "bar", stacked =True)
#
# plt.ylabel("Operations", fontsize=20)
# plt.xlabel("Operation type", fontsize=20)
# plt.xticks(fontsize=16)
# plt.yticks(fontsize=16)
# plt.legend(prop={'size': 20})
# plt.show()
# print("juri")

plt.barh(lv,df2["Di"])
plt.barh(lv,df2["Dm"],left=df2["Di"])
plt.barh(lv,df2["Df"], left=df2["Di"] + df2["Dm"])
plt.xlabel("Operations", fontsize=20)
plt.ylabel("Operation type", fontsize=20)
plt.xticks(fontsize=16)
plt.yticks(fontsize=16)
plt.legend(["Di","Dm","Df"],prop={'size': 20})
plt.show()