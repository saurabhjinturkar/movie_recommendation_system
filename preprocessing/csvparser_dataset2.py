import csv

with open("updated_dataset2.csv", "r") as csvfile:
    with open("dataset2_cleaned.csv", "w") as csvoutput:
        lines = csv.reader(csvfile)

        for line in lines:
            index = 0

            for parameter in line[1:]:
                if int(parameter) is not 0:
                    csvoutput.write(line[0] + "," + str(index) + "," + parameter + "\n")
                index = index + 1

csvoutput.close()
print("Data written succesfully!")
csvfile.close()
