# Backend-Java

## Table of contents

- **[Overview](#overview)**<br>
- **[API Endpoints](#api-endpoints)**<br>

## <a name="overview"></a>Overview
### Water My Plants
Ensuring that all your plants are consistently watered is actually pretty difficult. Water My Plants is an app that helps to solve those problems. 

With an easy to use interface for creating a plant watering schedule tailored to each individual plant, WaterMyPlants will remind users when it's time to feed that foliage and quench your plants' thirst.

## <a name="api-endpoints"></a>API endpoints

### Server: https://jren-watermyplants.herokuapp.com/

### Detailed Endpoints Doc: https://jren-watermyplants.herokuapp.com/swagger-ui.html#/
### ⬆⬆⬆⬆⬆  Belows are only some basic endpoints, check out for more endpoints   ⬆⬆⬆⬆⬆

### **_Authentication_**

| Method        | Endpoint           | Body (required)                       | Comments        | Notes                                             |
| ------------- | ------------------ | ------------------------------------- | --------------- | ------------------------------------------------- |
| register POST | /api/auth/register |  username, password, phonenumber      |             | Creates a new user in the database.        |
| login POST    | /api/auth/login    |  username, password                   | axios.post('https://jren-watermyplants.herokuapp.com/api/auth/login', <br>`grant_type=password&username=${form.username}&password=${form.password}`, {<br>headers: <br>{`Basic ${btoa('lambda-client:lambda-secret')}`,<br>'Content-Type': 'application/x-www-form-urlencoded}| Returns a access token. (res.data.access_token) |
| logout GET | /api/auth/logout |       | AxiosWithAuth (required)            | Revokes the token of current user       |


### NOTE: ➡➡ To access endpoints for users and plants, MUST include a request header as follows ⬅⬅
 
&nbsp;headers: {
       &nbsp;&nbsp;Authorization: `Bearer ${token}`
      &nbsp;}
<br />

### **_EndPoints for Users_**

| Method                        | Endpoint                         | Body (required)                        | Comments                           |Notes                            |
| ----------------------------- | -------------------------------- | -------------------------------------- | ---------------------------------- | --------------------------------|
| get current user info GET     | /api/users/info                  |                                        |                                    | Returns info of current user    |
| update user PUT               | /api/users/:id                   | username, password, phonenumber        |                                    | Updates the user with user id   |
| delete user DELETE            | /api/users/:id                   |                                        |                                    | Deletes the user with user id    |

### **_Endpoints for Plants_**

| Method                        | Endpoint                           | Body (required)                              | Comments        | Notes                      |
| ----------------------------- | ---------------------------------- | -------------------------------------------- | --------------- | -------------------------  |
| get plants  GET               | /api/plants                        |                                              |                 | Fetches all plants of current user |
| add plant POST                | /api/plants                        | nickname, species, h2ofrequency, image       |                 | add plant to current user's plants  |
| update plant PUT              | /api/plants/:id                    | nickname, species, h2ofrequency, image       |                 | Update plant with plant id    |
| delete plant by Id DELETE     | /api/plants/:id                    |                                              |                 | Delete Plant by Id |
