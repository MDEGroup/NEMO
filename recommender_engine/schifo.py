import os

PAST = 5
FUTURE = 2
PATH = f"/Users/juri/development/operationsAssistant/recommender_engine/FINAL/F"
precision = []
recall = []
outputString = ""
for i in range(0,10):
    csvpath = os.path.join(PATH + str(i), "PrecisionRecall.csv")
    with open(csvpath,"r") as reader:
        lines = reader.readlines()
        for line in lines:
            prec, rec, id = line.split(",")
            precision.append(float(prec))
            recall.append(float(rec))
            outputString = outputString + f"{prec}, prec, F{str(i)} \n"
            outputString = outputString + f"{rec}, rec, F{str(i)} \n"

with open(f"F_C{str(PAST)}{str(FUTURE)}.csv", "w") as writer: writer.write(outputString)
import statistics
print(statistics.mean(precision))
print(statistics.mean(recall))