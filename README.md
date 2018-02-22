find_driver_demo
=============================================

[![Build Status](https://travis-ci.org/franklingu/find_driver_demo.svg?branch=master)](https://travis-ci.org/franklingu/find_driver_demo)

## Intro
This repo contains implementation for a basic HTTP rest server that can accepts drivers' updating position and accepts users search for nearby drivers based on current drivers' positions.

The project is implemented in Java and built on top of Spring framework. The database is PostgreSQL. For setup instructions, see below.

## Setup
Requires Java 8 installed and gradle 4. For database the recommended setup is via PostgreSQL and for easier setup docker and docker-compose (must supports docker-compose version 3 yaml) is recommended:

In one shell:

~~~
docker-compose up
~~~

In another shell:

~~~
./gradlew build && java -jar build/libs/find_driver-1.0.0.jar
~~~

Once built is done, tests pass the application server will be started.


## Explanation

I have been working with Python mostly and this time I want to choose something with static typing and out of the choices, I will simply rule out the choices of "Go" as I have no experience in it. (I worked with Ruby before and Ruby on Rails is good but it is another dynamic language)

There are many frameworks in Java ecosystem. For example, Play, Grails, JSF and based on my initial quick Google researches Spring is clearly the winner in popularity. [One example article](https://zeroturnaround.com/rebellabs/java-web-frameworks-index-by-rebellabs/)

As for the choice of database, it did not make any difference here as use of hibernate can enable us to quickly switch to MySQL.

## Design

This project is mainly following the Spring-Boot recommendation: building a MVC pattern web server. There is no "View" actually because we are simply returning JSON responses. Model manages data representation and storage and Controller handles application logic.

## Load Test

LocustIO is chosen for benchemarking. Scripts are in scripts folder.

Example command:

~~~
locust -f scripts/search_drivers_test.py --csv=stats_2 --no-web -r20 -c20 -n1000 -H http://localhost:8888
~~~

#### Notes

I did not complete benchmarking for "update_position_test.py" -- that would require too much file descriptors on my machine. Instead, I simulated with Apache "ab" sending 500000 requests in the format of `ab -n 500000 -c 100 -u put_file.txt -m PUT http://localhost:8888/drivers/2025/localstion` to 10 different driver ids and get a mean rate of 290 * 10 requests/s with search_drivers_test 39 requests/s at the same time.

However, talking about benchmarking without server specification is just pointless. I run server and benchmarked on my own computer. My computer has 4 cores@2GHz and 8GB of memory (though it is 6 years old and was running other applications as well)

## What should have been improved / What else can be tried

1. During implementation, I researched both hibernate-spatial and hibernate-search. Hibernae-spatial looks very promissing as well. I just read through documents of hibernate-search and found that its grid idea is close to geohash and based on my previous knowledge its performance should be very good.
2. In production, the schema should not be simply DriverPosition -- at least Driver entity should be created to store meta information of a driver. And in that case verifying existence of driver id should be a call to DB/cache to check driver with specified id exists or not.
3. Historical DriverPosition should be saved -- for offline analysis or other purposes.
4. Settings for production development should be separate -- information such as DB password should be kept secrect -- I did not create production application.properties due to time limit.
5. Docker compose is in the repo. It should not be in the repo or it should be modified locally.
6. No form of cache is used to speed up responses.
