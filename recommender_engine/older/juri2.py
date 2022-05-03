import pandas
import matplotlib.pyplot as plt
import numpy
import math
from keras.models import Sequential
from keras.layers import Dense
from keras.layers import LSTM
from sklearn.preprocessing import MinMaxScaler
from sklearn.metrics import mean_squared_error
from sklearn.compose import ColumnTransformer
from sklearn.preprocessing import OneHotEncoder, LabelEncoder
import numpy as np
from numpy import array


#ct.fit_transform(X)


def create_dataset(dataset, look_back=1):
	dataX, dataY = [], []
	for i in range(len(dataset)-look_back-1):
		a = dataset[i:(i+look_back), 0]
		dataX.append(a)
		dataY.append(dataset[i + look_back, 0])
	return numpy.array(dataX), numpy.array(dataY)

def split_sequence(sequence, n_steps):
	X, y = list(), list()
	for i in range(len(sequence)):
		# find the end of this pattern
		end_ix = i + n_steps
		# check if we are beyond the sequence
		if end_ix > len(sequence) - 1:
			break
		# gather input and output parts of the pattern
		seq_x, seq_y = sequence[i:end_ix], sequence[end_ix]
		X.append(seq_x)
		y.append(seq_y)
	return array(X), array(y)

#numpy.random.seed(7)
# Load dataset
dataframe = pandas.read_csv('op.csv', engine='python', header=None)

dataset = dataframe.to_numpy()

# scaler = MinMaxScaler(feature_range=(0, 1), )
# dataset2 = scaler.fit_transform(dataset)
n_steps = 2
n_features = 1
X, y = split_sequence(dataset, n_steps)
print(X.shape[1])
print(X.shape[1])
model = Sequential()
model.add(LSTM(50, activation='relu', input_shape=(X.shape[1], X.shape[2])))
model.add(Dense(y .shape[1]))
model.compile(optimizer='adam', loss='mse')
model.summary()
history = model.fit(X, y, epochs=10, batch_size=16, verbose=1, validation_split=0.1)

plt.plot(history.history['loss'], label='Training loss')
plt.plot(history.history['val_loss'], label='Validation loss')
plt.legend()

forecast = model.predict([[[1,2,3], [1,2,3]]])

print(forecast)

# x_input = array([[1., 0., 0., 0., 0., 0., 1., 0., 1., 0., 0., 0., 0.,],
#  [1., 0., 0., 0., 0., 0., 1., 0., 1., 0., 0., 0., 0.]])
# x_input = x_input.reshape((1, n_steps, n_features))
# yhat = model.predict(x_input, verbose=0)
# print(f"predicted value for {x_input} is {yhat}")

##PHUONG


