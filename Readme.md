Baches Andrei solution for AirtouchMedia interview exercise

The app uses OkHttp to interact with the web service and send asynchronous request for the required data.
The data is parsed and processed on a background thread and is later sent to the UIThread in order to update the interface and let the user interact with the app.
For determining the conversion rates from every currency to any other one, the app uses a breadth search like algorithm to find the possible conversions and to calculate the corresponding rates.

Instead of using floating numbers, the money values are stored as string and converted to BigDecimal when computations like adding and multiplying are necessary. After each operation, the resulting value is rounded to two decimal places using .scale() method.
