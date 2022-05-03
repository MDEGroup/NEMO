import pandas as pd
from sklearn.compose import make_column_selector as selector
from sklearn.preprocessing import OneHotEncoder
from sklearn.compose import ColumnTransformer
import numpy as np
data = pd.read_csv("data.csv", header=None)
categorical_columns_selector = selector(dtype_include=object)
categorical_columns = categorical_columns_selector(data)
print(categorical_columns)

categorical_preprocessor = OneHotEncoder(handle_unknown="ignore")
preprocessor = ColumnTransformer([
    ('one-hot-encoder', categorical_preprocessor, categorical_columns)])
dataset = np.asarray(preprocessor.fit_transform(data))
print(dataset)