import random

from locust import HttpLocust, TaskSet, task


SG_CENTER_LAT = 1.352083
SG_CENTER_LON = 103.819836
LAT_RANGE = 0.1
LON_RANGE = 0.2


class SearchDriversBehavior(TaskSet):
    def on_start(self):
        pass

    @task
    def search_drivers(self):
        obj = {
            'latitude': SG_CENTER_LAT + random.uniform(-LAT_RANGE, LAT_RANGE),
            'longitude': SG_CENTER_LON + random.uniform(-LON_RANGE, LON_RANGE),
        }
        self.client.get('/drivers', params=obj)


class ServerLoadTest(HttpLocust):
    task_set = SearchDriversBehavior
    min_wait = 0
    max_wait = 0
