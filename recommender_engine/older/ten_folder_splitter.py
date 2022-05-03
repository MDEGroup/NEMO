import pandas
import numpy as np
import os
dataframe = pandas.read_csv('big_step_5.csv', engine='python', header=None)
print(len(dataframe))
NUM_OF_ITEMS = 50000
BASE_PATH = "ten_folder"
dataframe = dataframe.head(NUM_OF_ITEMS)

fold_size = len(dataframe)/10
folds = np.array_split(dataframe,10)
for i, fold in enumerate(folds):
    fold_path = os.path.join(BASE_PATH,f"F{i}")

    try:
        os.mkdir(fold_path)
    except FileExistsError:
        print("Folder already exists")

    with open(os.path.join(fold_path, "dataset.csv"), "w") as writer:
        for k,v in enumerate(folds):
            if(k!=i):
                for string in v[0].tolist():
                    writer.write(string + "\n")
        [writer.write(string + "\n") for string in fold[0].tolist()]


