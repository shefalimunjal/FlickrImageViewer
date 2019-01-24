The app heavily uses Android architecture components library which encapsulates the best
practices and design principles for writing Android apps, and is recommended by Google.
The architecture components consists of:

* LiveData: Android’s alternative of RxJava observables which is also lifecycle aware.
This makes writing a lot of interfaces/callbacks redundant since the data can just be
observed and also avoid memory leakages by default since the observables respect
the lifecycle of the observer. No more AsyncTasks leaking activities!
* ViewModel: Promotes Model-View-ViewModel (MVVM) design pattern. Since
ViewModels survive screen rotations and outlive the lifecycle of an activity, it helps us
avoid a lot of conventional issues developing on Android.
* Room: An object mapping library persistence library build on top of SQLite. Room
lets a caller observe the database for any changes (by returning LiveData) and
therefore the UI is always in sync with the latest data in database. It takes away a lot
of complex logic of keeping the UI consistent with database. A room database is
always a single source of truth for the data, and everybody can just observe the
database for any changes in the data. In this app, the main activity observes the
“image” table and therefore when new data is fetched and saved into the database,
UI automatically gets notified of the new data.
* Paging Library: The Paging Library makes it easier for us to load data gradually and
gracefully within a RecyclerView. This library makes it super easy to load pages of
data from Room database into a RecyclerView.

Further, the app also uses some of the standard design patterns:
* Dependency injection using Dagger 2
* Single Responsibility Principle
* Model-View-ViewModel (MVVM)
* Singletons
