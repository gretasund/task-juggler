<div align="center">
  <img src="img/logos/java.png" height="50px" style="display: inline-block; margin-left: auto; margin-right: auto;">
  <img src="img/logos/spring-boot.png" height="50px" style="display: inline-block; margin-left: auto; margin-right: auto;">
  <img src="img/logos/hibernate.png" height="50px" style="display: inline-block; margin-left: auto; margin-right: auto;">
  <img src="img/logos/html-css.png" height="60px" style="display: inline-block; margin-left: auto; margin-right: auto;">
  <img src="img/logos/thymeleaf-nontransparent.png" height="70px" style="display: inline-block; margin-left: auto; margin-right: auto;">

# Project Management Application
</div>

This is a Kanban / Scrum style project management tool which can be used to manage all kinds of tasks. 
Admins can create projects, assign managers and developers to them which can then suggest and accept different tasks, 
set a due dates, store time estimates in them and track how much time has already been spent on a certain task.

## User Role Concept
### Managers
Have a dashboard where they can see
- the current progress of their projects
- a list of all tasks sorted by due date
- tasks that their developers suggested
- tasks that are not scheduled yet or don't have an assignee

<img src="img/01_1_Manager.png" width="800">

Can have a closer look at their different projects
<img src="img/01_3_Manager.png" width="800" />

View a list of all tasks
<img src="img/01_4_Manager.png" width="800">

### Developers
Have a dashboard where they can see
- the current progress of their projects
- all tasks assigned to themselves
- tasks that they suggested to the manager
- open tasks that don't have an assignee yet
- 
<img src="img/02_1_Developer.png" width="800">

Can have a closer look at their different projects
<img src="img/02_2_Developer.png" width="800">

Suggest a new task from out of the project view and store a time estimate in it
<img src="img/02_3_Developer.png" width="800">

View a list of all tasks
<img src="img/02_4_Developer.png" width="800">

See the details of a task. This includes seeing other developers time estimates and edit it if necessary. Also it's possible to book times on a task.
<img src="img/02_5_Developer.png" width="800">

Edit a task, e.g. change the status (idea, planned, in progress, in test, done), the description or the title.
<img src="img/02_6_Developer.png" width="800">

Book times spent on a task (this booking applies to each task and developer, and a developer can book times several times if the processing takes several days, for example)
<img src="img/02_7_Developer.png" width="800">


### Admins
Have a dashboard where they can see all projects without members and all users without projects
<img src="img/03_1_Admin.png" width="800">

View all users
<img src="img/03_2_Admin.png" width="800">

Change the roles of other users: there is admin, developer and manager
<img src="img/03_3_Admin.png" width="800">

View all projects
<img src="img/03_4_Admin.png" width="800">

Edit projects and add / remove other users (of any role) to projects
<img src="img/03_5_Admin.png" width="800">

Create new projects
<img src="img/03_6_Admin.png" width="800">


### All Users
- Register and get the role "developer" by default,
- Change  their password


| Type           | Parameter           | Description                                 |
|----------------|---------------------|---------------------------------------------|
| DB config      | server              | Server                                      |
|                | port                | Port                                        |
|                | password            | Password                                    |
|                | database            | Name of the db schema                       |
|                | named_instance      | Boolean flag                                |
|                | named_instance_name | Name of the instance                        | 
|                |                     |                                             |
| Style config   | activation_style    | Name of the folder containing the css file  |
|                |                     |                                             |
| Printer config | printer_name        | Name of the printer                         |
|                | page_height         | Height of the page in floating point number |
|                | page_width          | Height of the page in floating point number |

## Requirements
### Java
Make sure you've got a Raspberry Pi or some other kind of armhf machine.
  ```sh
  sudo apt update
  ```

### Gradle
Make sure you've got a Raspberry Pi or some other kind of armhf machine.
  ```sh
  sudo apt update
  ```

### Database Setup
More information on the underlying database schema will be provided soon.

## Prepared Data To Get You Started
Just execute start.sh :)


https://www.iloveimg.com/download/31vjvvf0vw229jzxkc9chnf46587lbpg5fvnq49kr25gbp9gyt7th5jdwm1jfw8b7n8d9h6cf766p3w2bpm5nlbprwk76v717sf554w0dA5hd9c3cmpsfmpx02x4lv3qjhA13vyncblbxcvjth07t7zrmd90bw9pfzwxqz7v2fx0311msjt1/1