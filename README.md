[Pic Me] is an Android application that uses Clarifai's API for photo recognition. 

How it works:

	1. Our application generates random adjectives for the users to match via their phone gallery or by taking a picture
	2. Using Clarifai's API, we compare the current adjective to the list of tags in the photos
	3. Users are given a percentage score based on how their adjective ranked on the list of tags, with number 1 being the highest

Other Features:
	
	1. Refresh Button so the users can switch their word up
	2. Definition Button [WIP] in case users need to know what certain words mean
	3. Login [WIP], to make the interface more friendly


Development Team [All first-year hackathon-ers]

	1. Yulong Tan --> Created most of the Android UI and implemented activities / functions, including the camera request
	2. Alan Tan --> Wrote the Java code that generates random adjectives and the program that generates definitions from Dictionary.com
	3. Allen Tran --> Implemented the Clarifai API into our application and worked on the scaling of widgets, photos, and created access to the Android System's gallery 
	4. Alex Poquiz --> Worked mainly on the Firebase end [to implement logins] for the opening interface

Working with Clarifai

	It was rather hard to implement at first, because our team had little experience working with APIs and other network based functions. Clarifai's short yet interesting presentation gave us a good idea of a simple app that we wanted to create and we had a lot of fun reading into their documentation, trying to figure out how it worked. Once we did that, it was a long and hard journey, but we thoroughly learned a lot from this experience. 