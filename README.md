# SBCRPayroll

SBCR Payroll automates workhours calculations for Santa Barbara Chicken Ranch. The program requires 9 .csv files generated 
from the Toast POS system.  For each of the 3 restaurants and 2 week pay period,there will be a report for the first week,
second week, and a summary of both.  Employees from each location are merged and sorted, adding any new employees and
removing any that are inactive.  Using Apache's POI API, each employees tabulated hours are inserted into a accountant
prepared Excel sheet.

This application both saves time and prevents user error in data entry.


SBCR Payroll uses core Java and runs in the Java Runtime Environment.
