import random

from locust import HttpLocust, TaskSet, task


SG_CENTER_LAT = 1.352083
SG_CENTER_LON = 103.819836
LAT_RANGE = 0.1
LON_RANGE = 0.2


class UpdatePositionBehavior(TaskSet):
    def on_start(self):
        pass

    @task
    def update_position(self):
        obj = {
            'latitude': SG_CENTER_LAT + random.uniform(-LAT_RANGE, LAT_RANGE),
            'longitude': SG_CENTER_LON + random.uniform(-LON_RANGE, LON_RANGE),
            'accuracy': random.random(),
        }
        driver_id = random.randint(1, 50000)
        self.client.put(
            '/drivers/{}/location'.format(driver_id), json=obj
        )


class ServerLoadTest(HttpLocust):
    # simulate 1000 requests per second
    task_set = UpdatePositionBehavior
    min_wait = 0
    max_wait = 0
