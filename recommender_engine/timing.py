import os
import re
import scipy.stats as stats

from datetime import datetime
from datetime import timedelta

base_path = "/Users/juri/development/operationsAssistant/recommender_engine"
FMT = '%H:%M:%S'
train_time = []
op_size = []
pred_op = []
testing_time = []
for past in [2, 5, 10]:
    for future in [1, 2, 5]:
        base_path_ex = f"PAST{str(past)}_FUTURE{str(future)}"
        base_path_ex = os.path.join(base_path,base_path_ex)
        print(f"==============={base_path_ex}=================")
        total = 0
        fold = 0
        base_path_ex_fold = os.path.join(base_path_ex, f"F{str(fold)}")
        filename = os.path.join(base_path_ex_fold,f"fold_run{str(fold)}")
        if not os.path.exists(filename):
            print(f"F{str(fold)} does not exist")
        else:
            start_pattern = r"Start Time = ([\d][\d]):([\d][\d]):([\d][\d])"
            end_pattern = r"End Time = ([\d][\d]):([\d][\d]):([\d][\d])"
            start_patternL = r"Start TimeL = ([\d][\d]):([\d][\d]):([\d][\d])"
            end_patternL = r"End TimeL = ([\d][\d]):([\d][\d]):([\d][\d])"

            start = ""
            end = ""
            startL = ""
            endL = ""
            for i, line in enumerate(open(filename)):
                start_resultL = re.match(start_patternL, line)
                end_resultL = re.match(end_patternL, line)
                start_result= re.match(start_pattern, line)
                end_result = re.match(end_pattern,line)

                if end_result:
                    end = f"{end_result.group(1)}:{end_result.group(2)}:{end_result.group(3)}"
                if start_result:
                    start = f"{start_result.group(1)}:{start_result.group(2)}:{start_result.group(3)}"

                if end_resultL:
                    endL = f"{end_resultL.group(1)}:{end_resultL.group(2)}:{end_resultL.group(3)}"
                if start_resultL:
                    startL = f"{start_resultL.group(1)}:{start_resultL.group(2)}:{start_resultL.group(3)}"

            trainindDelta = datetime.strptime(end, FMT) - datetime.strptime(start, FMT)
            if(startL!="" and endL!=""):
                testingDelta = datetime.strptime(endL, FMT) - datetime.strptime(startL, FMT)
            if trainindDelta.days < 0:
                trainindDelta = timedelta(days=0, seconds=trainindDelta.seconds, microseconds=trainindDelta.microseconds)
            if (startL != "" and endL != ""):
                if testingDelta.days < 0:
                    testingDelta = timedelta(days=0, seconds=testingDelta.seconds, microseconds=testingDelta.microseconds)

            print(f"Testing F{str(fold)} - {trainindDelta.seconds}")
            train_time.append(trainindDelta.seconds)
            op_size.append(past + future)
            pred_op.append(future)
            if (startL == "" or endL == ""):
                print(f"Training is missing")
            else:
                print(f"Training F{str(fold)} - {testingDelta.seconds}")
            testing_time.append(testingDelta.seconds)




print("===")
print(train_time)
tau, p_value = stats.spearmanr(pred_op, train_time)
print(f"tau = {tau} , p-value = {p_value}")


print(op_size)
print("===")
print(testing_time)


tau, p_value = stats.spearmanr(op_size, testing_time)
print(f"tau = {tau} , p-value = {p_value}")
# Note: for Python 2.7 compatibility, use ur"" to prefix the regex and u"" to prefix the test string and substitution.