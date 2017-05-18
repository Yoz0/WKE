# WikiSerachEngne
An humble web search engine using the wikipedia API. Avaible on (https://github.com/Yoz0/WSE).

# How to use
Be sure to install mongoDB server and be sure that is is running.

To launch it, run '''sudo service mongod start'''.

To make sure that it is runnnig, do '''tail /var/log/mongodb/mongod.log''' and
make sure it reads "waiting for connections on port 27017".

## Run the LinksGatherer
Compile and run `LinksGathererMain`

## Run the Pageranker
Make sure you have a properly wikipedia-page-id-titles.raw defined in WSE and a
mongodb database named `mydb` running with a collection `articles` filled.

Open a shell in the src/pageranker folder.

Type the following commands :

* `. ./.hadoop_classpath` This will set the `HADOOP\_CLASSPATH` environnment
variable.  You need to make sure you have the `JAVA\_HOME` variable defined first.

* `make` This will compile the pageranker and create the pageranker.jar

* `run` This will run the map-reduce

You can see the changes in the database, you can also see the new pagerank in
the WSE/src/pageranker/out/ folder

## Query the database
Run the wse\_package.Main classe, you'll see a prompt choose 2 to query the
database.

# Credits
This is a school project, done in 2017 at ENSIIE Strasbourg by :
* Pierre-Olivier Gendraud
* Clément Saintier
* Clément Floquet
* Stéphane Kastenbaum
