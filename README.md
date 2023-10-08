Adds a `/t toggle overclaimshield` command that when ran will add a boolean metadata field to the town representing their overclaim shield status.

When this command is run, the shield will be enabled or disabled depending on the previous state, if it is enabled there will be a confirmation explaining the cost that will be incurred for toggling it on and the subsequent costs at new days.

On each Towny new day all towns will be iterated over, if they have overclaim shield enabled, have chunks over the limit, and have the sufficient funds their town will be charged. If they do not have sufficient funds they will not be charged and their overclaim shield status will be removed.

Additionally, when the overclaim shield command is initially ran there will be a metadata field added to the town representing the current time, if the next new day is less than 24 hours after the initial toggling on of the shield there will be no extra charge for that day.

The cost to enable and maintain the overclaim shield each day is determined by two config settings, a cost and a grouping size. Any chunks over the limit will be divided by this grouping size and the resulting number of groups will be multiplied by the cost.

`price = (l/g) * c` where l is the number of townblocks over the limit, g is the grouping size and c is the cost per group.