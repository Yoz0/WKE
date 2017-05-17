all: pageranker.jar

pageranker.jar: pageranker.class
	mkdir -p classes
	jar -cvf $@ -C classes .

pageranker.class: Pageranker.java
	hadoop com.sun.tools.javac.Main -d classes Pageranker.java

clean:
	rm -rf classes
	rm -f pageranker.jar
