# smartTODO
In today's fast-paced world, “efficiency” is a motto adopted by many people. As the tasks that need to be accomplished within a day grow in number, 
and the amount of time in a day to accomplish such tasks seem to decrease, it becomes more and more important to efficiently manage one's time and plan
the tasks that need to be accomplished within that time in a practical and organized manner.

The project we have chosen is SMART-TODO, a location-based reminder application. It is an application for alerting a user of an item(s) on a to-do list
if the user is detected to be close to the item's performance location.

We have made use of the Google maps APIs which perform automatic detection of the mobile terminal when the automobile is in motion. Using that, the mobile
application periodically compares the position of the user with to-do list item locations to determine whether the user will pass within a pre-defined proximity
metric(50m) of the listed location. The proximity criteria are based on numerous factors, such as the user's current position and the user's destination.
The proximity criteria may also vary from task to task. If the proximity criterion is based on the user's current position, the criterion is satisfied if
the distance between a detected current location and a performance address is within a pre-determined proximity metric (i.e.50m).
