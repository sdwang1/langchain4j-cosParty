# Eureka Amphibious

## Service

Call api with a `passkey` mapped to a certain task handler, to create task and query its async result.

## Create Task

URL: https://ai.leqee.com/amphibious/create-task

Request Body:
* passkey: STRING
* task_meta: JSON OBJECT

Response contains a task id.

## Query Task

URL: https://ai.leqee.com/amphibious/query-task

Request Body:
* passkey: STRING
* task_id: LONG

Response contains the task result, get feedback is task status is DONE.

## Daemon

Just run in background.