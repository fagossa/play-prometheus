# Web application + prometheus + grafana

Example of how to monitor an application using:
- the prometheus client lib
- prometheus
- alermanager
- grafana
- cAdvisor

## Prerequisites

To run everything you just need `docker-compose`.

## How do I starting playing?

> Before running this command be aware that the docker files are quite heavy, so this could take a while

```
docker-compose up -d
```

## More information

I wrote several implementation using different frameworks. Feel free to used the one that suites you more.

I wrote some blog articles covering _prometheus_ and monitoring:

* The [first one](http://blog.xebia.fr/2017/07/28/superviser-mon-application-play-avec-prometheus) covers basic concepts and terminology.

* The [second one](https://en.fabernovel.com/insights/tech-en/alerting-in-prometheus-or-how-i-can-sleep-well-at-night) handles mostly alerting.
