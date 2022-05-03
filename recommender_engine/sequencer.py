import pandas
import numpy as np
import os
from numpy import array


def split_sequence(sequence, n_steps, n_future):
	X, y = list(), list()
	print(len(sequence))
	for i in range(len(sequence)):
		# find the end of this pattern
		end_ix = i + n_steps
		# check if we are beyond the sequence
		if end_ix + n_future > len(sequence) - 1:
			break
		# gather input and output parts of the pattern
		seq_x, seq_y = sequence[i:end_ix], sequence[end_ix:end_ix+n_future]
		X.append(seq_x)
		y.append(seq_y)
	return array(X), array(y)

def sequencer(dataset, output_file, n_steps, n_feature=1):
	dataframe = pandas.read_csv(dataset, engine='python', header=None)
	dataset = dataframe.to_numpy()
	X, y = split_sequence(dataset, n_steps, n_feature)
	s = ""
	print("Sequences computed")
	with open(output_file,"w")as writer:
		for i,e in enumerate(X):
			s = s +  " ".join([str(item) for sublist in e for item in sublist]) + "\t" +	" ".join(str(item) for v in y[i] for item in v)  + "\n"
			if(i%10000==0):
				writer.write(s)
				s = ""
				print(i)
		writer.write(s)

if __name__ == '__main__':
    # define input sequence
	n_steps = 5
	n_feature = 2
	BASE_PATH = f"FINAL"
	DATASET_NAME = "datasetBPMN2.csv"
	manipulated_dataset = f"{DATASET_NAME}_all_{n_steps}.csv"
	manipulated_dataset =os.path.join(BASE_PATH, manipulated_dataset)
	sequencer(DATASET_NAME, manipulated_dataset, n_steps, n_feature)
	dataframe = pandas.read_csv(manipulated_dataset, engine='python', header=None)
	print(len(dataframe))
	NUM_OF_ITEMS = 50000
	#dataframe = dataframe.head(NUM_OF_ITEMS)
	#dataframe = dataframe.iloc[590000:640000]
	dataframe = dataframe.iloc[-50000:]
	fold_size = len(dataframe) / 10
	folds = np.array_split(dataframe, 10)
	for i, fold in enumerate(folds):
		fold_path = os.path.join(BASE_PATH, f"F{i}")
		try:
			os.mkdir(fold_path)
		except FileExistsError:
			print("Folder already exists")
		with open(os.path.join(fold_path, "dataset.csv"), "w") as writer:
			for k, v in enumerate(folds):
				if (k != i):
					for string in v[0].tolist():
						writer.write(string + "\n")
			[writer.write(string + "\n") for string in fold[0].tolist()]







