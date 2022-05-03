import numpy as np
import pandas as pd
import os
import seaborn as sb
import pandas as pd
import matplotlib.pyplot as plt

BASE_PATH = "PAST5_FUTURE5"
pr_collection = []
for i in range(0,10):
    #if (i != 1 and i != 6):
        pr_collection_fold = []
        with open(os.path.join(BASE_PATH, f"F{str(i)}", "PrecisionRecall.csv")) as reader:
            for row in reader:
                pr,rec,freq = row.strip().split(",")
                pr_collection_fold.append((pr, rec, freq))
        pr_collection.append(pr_collection_fold)
print(len(pr_collection))

pl = []
rl = []
fl = []
mt = []
for i, prs in enumerate(pr_collection):
    for pr,rec,fre in prs:
        pl.append(float(pr))
        if float(pr) > 1: print(pr)
        mt.append("Precision")
        fl.append(str(i))
        pl.append(float(rec))
        if float(rec) > 1: print(rec)
        mt.append("Recall")

        fl.append(str(i))
sb.set_theme(style="whitegrid")
dataframe = pd.DataFrame({'fold':fl, 'precision':pl, 'type':mt})
ax = sb.violinplot(x='fold', y="precision", hue="type", data=dataframe, palette="muted")
ax.set_ylabel("")
plt.savefig(os.path.join(BASE_PATH,"PR_c105.pdf"))