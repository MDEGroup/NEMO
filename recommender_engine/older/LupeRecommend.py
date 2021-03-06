# -*- coding: utf-8 -*-
"""LUPE_testing_time.ipynb

Automatically generated by Colaboratory.

Original file is located at
    https://colab.research.google.com/drive/1DroGX1fdEK0ekVupwsPGid07jCZs7A50
"""

import numpy as np
import tensorflow as tf
from tensorflow import keras
from keras.models import Model
from keras.layers import Input, LSTM, Dense
from keras.callbacks import ModelCheckpoint
from sklearn.metrics import classification_report
from tensorflow.keras.optimizers import SGD, RMSprop 
from tensorflow.keras.callbacks import LearningRateScheduler, ProgbarLogger, EarlyStopping, CSVLogger, TensorBoard

from os.path import join
#ROOT = '/content/drive/'
#DATA = 'My Drive/Colab Notebooks/PILOT/Dataset/' # You need to change this parameter according to your real path.
DATA_PATH = 'LUPEM'#join(ROOT, DATA)
#from google.colab import drive
#drive.mount(ROOT,force_remount=False)

batch_size = 500  # Batch size for training.
epochs = 10   # Number of epochs to train for.
latent_dim = 256
latent_dim = 300  # Latent dimensionality of the encoding space.

training_data = []
training_label = []

input_characters = set()
output_characters = set()

testing_data = []
testing_label = []


#a function to read data from file

def load_data(input_file, max_line=500000):
    data_X = []
    data_Y = []
    with open(input_file, "r", encoding="utf-8") as f:
        lines = f.read().split("\n")
    num_items = len(lines)
    cont = 0
    for line in lines[: ]:
        if line == "":
            break
        cont = cont+1
        if cont > max_line: break
        input_text, output_text = line.split("\t")
        # We use "tab" as the "start sequence" character
        # for the targets, and "\n" as "end sequence" character.
        output_text = "\t" + output_text + "\n"
        data_X.append(input_text)
        data_Y.append(output_text)
        for char in input_text:
            if char not in input_characters:
                print(char)
                input_characters.add(char)
        for char in output_text:
            if char not in output_characters:
                output_characters.add(char)
    return data_X, data_Y
MAX_LINE = 5000

input_file = join(DATA_PATH, 'big_step_2.csv')
input_data, input_label = load_data(input_file, max_line=MAX_LINE)
splitting = int((MAX_LINE / 10) * 9)
#split into two parts: one for training and one for testing
total_len = len(input_data)
print(total_len)

training_data = input_data[:splitting]
training_label = input_label[:splitting]
testing_data = input_data[splitting:]
testing_label = input_label[splitting:]
print(len(training_data))
print(len(testing_data))

input_characters = sorted(list(input_characters))
output_characters = sorted(list(output_characters))
num_encoder_tokens = len(input_characters)
num_decoder_tokens = len(output_characters)

max_encoder_seq_length = max([len(txt) for txt in training_data])
max_decoder_seq_length = max([len(txt) for txt in training_label])

print("Number of samples:", len(training_data))
print("Number of unique input tokens:", num_encoder_tokens)
print("Number of unique output tokens:", num_decoder_tokens)
print("Max sequence length for inputs:", max_encoder_seq_length)
print("Max sequence length for outputs:", max_decoder_seq_length)

input_token_index = dict([(char, i) for i, char in enumerate(input_characters)])
target_token_index = dict([(char, i) for i, char in enumerate(output_characters)])

#print(input_token_index)
#print(target_token_index)

encoder_input_data = np.zeros((len(training_data), max_encoder_seq_length, num_encoder_tokens), dtype="float32")
decoder_input_data = np.zeros((len(training_data), max_decoder_seq_length, num_decoder_tokens), dtype="float32")
decoder_target_data = np.zeros((len(training_data), max_decoder_seq_length, num_decoder_tokens), dtype="float32")

for i, (input_text, output_text) in enumerate(zip(training_data, training_label)):
    for t, char in enumerate(input_text):
        encoder_input_data[i, t, input_token_index[char]] = 1.0
    encoder_input_data[i, t + 1 :, input_token_index[" "]] = 1.0
    for t, char in enumerate(output_text):
        # decoder_target_data is ahead of decoder_input_data by one timestep
        decoder_input_data[i, t, target_token_index[char]] = 1.0
        if t > 0:
            # decoder_target_data will be ahead by one timestep
            # and will not include the start character.
            decoder_target_data[i, t - 1, target_token_index[char]] = 1.0
    decoder_input_data[i, t + 1 :, target_token_index[" "]] = 1.0
    decoder_target_data[i, t:, target_token_index[" "]] = 1.0



encoder_inputs = Input(shape=(None, num_encoder_tokens))
encoder = LSTM(latent_dim, return_state=True)
encoder_outputs, state_h, state_c = encoder(encoder_inputs)

# We discard `encoder_outputs` and only keep the states.
encoder_states = [state_h, state_c]

# Set up the decoder, using `encoder_states` as initial state.
decoder_inputs = Input(shape=(None, num_decoder_tokens))

# We set up our decoder to return full output sequences,
# and to return internal states as well. We don't use the
# return states in the training model, but we will use them in inference.
decoder_lstm = LSTM(latent_dim, return_sequences=True, return_state=True)
decoder_outputs, _, _ = decoder_lstm(decoder_inputs, initial_state=encoder_states)
decoder_dense = Dense(num_decoder_tokens, activation="softmax")
decoder_outputs = decoder_dense(decoder_outputs)

# Define the model that will turn
# `encoder_input_data` & `decoder_input_data` into `decoder_target_data`

model = Model([encoder_inputs, decoder_inputs], decoder_outputs)
model.compile(optimizer="rmsprop", loss="categorical_crossentropy", metrics=["accuracy"])


model.load_weights(join(DATA_PATH,'myModel.h5'))
model.summary()


#Define sampling models
encoder_model = Model(encoder_inputs, encoder_states)
decoder_state_input_h = Input(shape=(latent_dim,))
decoder_state_input_c = Input(shape=(latent_dim,))
decoder_states_inputs = [decoder_state_input_h, decoder_state_input_c]
decoder_outputs, state_h, state_c = decoder_lstm(decoder_inputs, initial_state=decoder_states_inputs)
decoder_states = [state_h, state_c]
decoder_outputs = decoder_dense(decoder_outputs)
decoder_model = Model([decoder_inputs] + decoder_states_inputs, [decoder_outputs] + decoder_states)

# Reverse-lookup token index to decode sequences back to
# something readable.
reverse_input_char_index = dict((i, char) for char, i in input_token_index.items())
reverse_target_char_index = dict((i, char) for char, i in target_token_index.items())


def decode_sequence(input_seq):
    # Encode the input as state vectors.
    states_value = encoder_model.predict(input_seq)

    # Generate empty target sequence of length 1.
    target_seq = np.zeros((1, 1, num_decoder_tokens))
    # Populate the first character of target sequence with the start character.
    target_seq[0, 0, target_token_index["\t"]] = 1.0

    # Sampling loop for a batch of sequences
    # (to simplify, here we assume a batch of size 1).
    stop_condition = False
    decoded_sentence = ""
    while not stop_condition:
        output_tokens, h, c = decoder_model.predict([target_seq] + states_value)

        # Sample a token
        sampled_token_index = np.argmax(output_tokens[0, -1, :])
        sampled_char = reverse_target_char_index[sampled_token_index]
        decoded_sentence += sampled_char

        # Exit condition: either hit max length
        # or find stop character.
        if sampled_char == "\n" or len(decoded_sentence) > max_decoder_seq_length:
            stop_condition = True

        # Update the target sequence (of length 1).
        target_seq = np.zeros((1, 1, num_decoder_tokens))
        target_seq[0, 0, sampled_token_index] = 1.0

        # Update states
        states_value = [h, c]
    return decoded_sentence

num_test = len(testing_data)
print(num_test)
encoder_input_testing_data = np.zeros((len(testing_data), max_encoder_seq_length, num_encoder_tokens), dtype="float32")

for i, (input_text) in enumerate(testing_data):
    for t, char in enumerate(input_text):
        encoder_input_testing_data[i, t, input_token_index[char]] = 1.0
    encoder_input_testing_data[i, t + 1 :, input_token_index[" "]] = 1.0

from datetime import datetime
startTime = datetime.now()
start_time = startTime.strftime("%H:%M:%S")
result_file = join(DATA_PATH,'results.csv')
with open(result_file, 'w+') as f1:
    for seq_index in range(num_test):
        # Take one sequence (part of the training set)
        # for trying out decoding.
        input_seq = encoder_input_testing_data[seq_index : seq_index + 1]
        decoded_sentence = decode_sequence(input_seq)
        #print("-")
        f1.write(testing_data[seq_index] + "\t" + decoded_sentence)
        print(testing_data[seq_index] + "\t" + decoded_sentence)
        #print(decoded_sentence)
endTime = datetime.now()
end_time = endTime.strftime("%H:%M:%S")
print("Start Time =", start_time)
print("End Time =", end_time)