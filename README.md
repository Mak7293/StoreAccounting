# Store Accounting
Store accounting is a simple, practical and free persian application for managing stores and small businesses.
In this application, the user can enter the goods in his store in this application and register the sale of the imported goods.
Also, the user can edit, delete and view the history of the goods of this application.

In the sales section, the user can view sales transactions in a specific time period with filtering these sales transactions by time.

Other features of this application are include entering credit card details of different people along with required financial details and printing invoices in pdf format.

This application has two beautiful night and day themes, also it is possible to create a backup version of the database. The possibility of creating a backup copy of the database
allows the user to easily transfer the information to other Android devices or inorder to prevent the loss of information when uninstalling and reinstalling this application.

--------------------------------------------------------------------------------------------------------------------------------------------------------------
|    Icon    |      Item    |
| :-------- | :------- |
|    📺    |      [Preview](https://github.com/Mak7293/StoreAccounting/blob/master/README.md#preview)    |
|     📱    |    [Compatibility](https://github.com/Mak7293/StoreAccounting/blob/master/README.md#compatibility) |
|    🎁    |   [Download](https://github.com/Mak7293/StoreAccounting/blob/master/README.md#download) |
|    📣    |   [Description](https://github.com/Mak7293/StoreAccounting/blob/master/README.md#description) |
|    💻    |  [Programing Feature](https://github.com/Mak7293/StoreAccounting/blob/master/README.md#programing-feature) |
--------------------------------------------------------------------------------------------------------------------------------------------------------------
# Preview 
This app has dark and light theme.

<img src="https://github.com/Mak7293/StoreAccounting/blob/master/screen_shots/Screenshot_20230404_144021.png" width=18% height=18%> <img
 src="https://github.com/Mak7293/StoreAccounting/blob/master/screen_shots/Screenshot_20230404_144358.png" width=18% height=18%> <img
 src="https://github.com/Mak7293/StoreAccounting/blob/master/screen_shots/Screenshot_20230404_144916.png" width=18% height=18%>  <img
 src="https://github.com/Mak7293/StoreAccounting/blob/master/screen_shots/Screenshot_20230404_161025.png" width=18% height=18%> <img
 src="https://github.com/Mak7293/StoreAccounting/blob/master/screen_shots/Screenshot_20230404_154925.png" width=18% height=18%>
 
 


for More screen shot click [here](https://github.com/Mak7293/StoreAccounting/tree/master/screen_shots).

--------------------------------------------------------------------------------------------------------------------------------------------------------------
# Compatibility 
Minimum android version **SDK24+** or **Android 7.0+**.

--------------------------------------------------------------------------------------------------------------------------------------------------------------
# Download
- You can download this app from [CafeBazar](https://cafebazaar.ir/app/com.example.storeaccounting?l=en).

--------------------------------------------------------------------------------------------------------------------------------------------------------------
# Description
- This app is using Jetpack Compose, latest amazing approach in developing Ui of android application in order to design beautiful and effective Ui.
- This app is using Mvvm Artichecture design pattern Along with Dagger Hilt android library for dependency Injection.
- This app is using Room Database in order to persistent storage. one to many realations are used the most inorder to design structure of database. one good can
can have multiple history like create,edit and sale but one history can belong to only one good. also this app for limited storage use datastore preferences.
- This app is using Junit framework for some unit-test, integration-test and end to end test.

--------------------------------------------------------------------------------------------------------------------------------------------------------------
# Programing Feature
- Jetpack compose for designing UI.
- [Vico library](https://github.com/patrykandpatrick/vico) for creating sale return graph.
- [Coil library](https://coil-kt.github.io/coil/compose) for loading sign Image in invoices part.
- [Persian date formatter](https://github.com/samanzamani/PersianDate).
- Junit framework for some unit-test, integration-test and end to end test.
- Android Room Database Library.
- Mvvm Artichecture.
- Dagger Hilt.
- Kotlin coroutine for asynchronous task.
- Kotlin shared-flow for live data.
- Compose state for holding state of composable functions.
- etc



