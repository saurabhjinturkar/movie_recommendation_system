import csv

MEAN_CENTERING = False

with open("updated_dataset1.csv", "r") as csvfile:
    with open("dataset1_cleaned_average_division.csv", "w") as csvoutput:

        lines = csv.reader(csvfile)
        for line in lines:
            count = 0
            num = 0

            average = 0
            for parameter in line[1:]:
                number = int(parameter)
                count = count + number

                if number > 0:
                    num = num + 1

            # print(count)
            if num > 0:
                # print("Average is:", (count / num))
                average = count / num

            index = 0

            if MEAN_CENTERING:
                for parameter in line[1:]:
                    output = 0
                    if int(parameter) is not 0:
                        output = int(parameter) - average + 0.1
                        output = "{0:.4f}".format(output)
                    # print(line[0], index, output)
                    # print(line)

                    if output is not 0:
                        csvoutput.write(line[0] + "," + str(index) + "," + str(output) + "\n")
                    index = index + 1

            if not MEAN_CENTERING:
                for parameter in line[1:]:
                    output = 0
                    if int(parameter) is not 0 and average is not 0:
                        output = "{0:.4f}".format(int(parameter) / average)
                    # print(line[0], index, output)
                    # print(line)
                    if output is not 0:
                        csvoutput.write(line[0] + "," + str(index) + "," + str(output) + "\n")
                    index = index + 1
    csvoutput.close()
    print("Data written succesfully!")
csvfile.close()
