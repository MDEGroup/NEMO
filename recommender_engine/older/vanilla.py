# univariate data preparation
from numpy import array
from keras.models import Sequential
from keras.layers import LSTM
from keras.layers import Dense
import os
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '2'
from tensorflow.python import tf2
# split a univariate sequence into samples
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





# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    # define input sequence
    raw_seq = [10, 20, 30, 40, 50, 60, 70, 80, 90]
    # choose a number of time steps
    n_steps = 3
    n_features = 1
    # split into samples

    X, y = split_sequence(raw_seq, n_steps)
    print (X)
    X = X.reshape(X.shape[0], X.shape[1], n_features)
    print(X)
    # summarize the data
    model = Sequential()
    model.add(LSTM(50, activation='relu', input_shape=(n_steps, n_features)))
    model.add(Dense(1))
    model.compile(optimizer='adam', loss='mse')
    model.fit(X, y, epochs=200, verbose=0)
    x_input = array([70, 80, 90])
    x_input = x_input.reshape((1, n_steps, n_features))
    yhat = model.predict(x_input, verbose=0)

    print(f"predicted value for {x_input} is {yhat}")