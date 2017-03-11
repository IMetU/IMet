# Group Project  - *IMet*

IMet is an app that help users to memorize the details of other people. User can store information of people they’ve met and quickly search through the data to find the name and info of the person standing in front of them, know that he/she has met somewhere before, but just can’t recall who the person is. Since the user is assumed to remember almost nothing about the person, being able to search by the data of appearance is critical. For example, the user should be able to search “female”, “about 165cm tall”, “fat”, “wears glasses” and find a list of people that match these criteria. Of course, if the user remembers other information about the person other than appearance, they can use them for searching as well, such as the event and location they’ve met, their relationship… etc. We hope that people who aim to build strong interpersonal relationships like community organizers and sales will benefit from IMet greatly.


## User Stories

Required (core) user stories:

Main_Activity (Person Item)
User can view all of the person items
User can tap on person item to view the person’s detail item

Detail_Activity (Person Detail)
User can edit person detail anytime she/he wants
User can store the information and characteristics of the people they meet, including:
   i. Personal Info: Name, email, phone number, relationship with the user, event/location that the user and the person have met for the first time, company/department/position that the person is working for, interest topic talked about…etc.
   ii. Appearance: Gender, approximate height, fat or skinny, hair color and length, glasses or no glasses, eyes shape, and “other appearance characteristics” (ex: hot looking, crooked teeth, pitchy voice)
   iii. Photos: Photo of business card, photo of the person taken secretly from afar, photo of the event that the user has met the person (note: even if it’s not a photo of the person, photo of the event/location helps to recall who they are)


Toolbar
[ADD]
User can click a "Add" icon to add person
[Filter] 
User can enter a search query to find a person by name and event
Use the ActionBar SearchView or custom layout as the query box
User can click on "filter" icon which allows selection of advanced search options to filter out the people that match this criteria
User can query limited information for fast and easy searching, such as being able to get the name and info of the person knowing only about the person’s appearance. Any other information can be used for searching as well, if the user remembers any. (included above)

User can remove "person item" ("person detail")
User can view more people items as they scroll with infinite pagination

User can open the app offline and see person item
Person information are persisted into sqlite and can be displayed from the local DB

Use FloatingActionButton  and CoordinatorLayout 
Store All of the data in cloud and set/get by restful API

Improve the UI / UX of our app


## License

    Copyright 2017 Jennifer Chan, TzuChun (Kelly) Chen, Quietus Chung

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
