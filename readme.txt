# heruko postgres

https://devcenter.heroku.com/articles/heroku-postgresql
https://devcenter.heroku.com/articles/connecting-to-relational-databases-on-heroku-with-java

# I: system environments on windows

Variable: DATABASE_URL
Value: postgres://<username>:<password>@<host>:<port>/<dbname>
---
Variable: JDBC_DATABASE_URL
Value: jdbc:postgresql://<host>:<port>/<dbname>?user=<username>&password=<password>
---
Variable: JDBC_DATABASE_USERNAME
Value: <username>
---
Variable: JDBC_DATABASE_PASSWORD
Value: <password>
---
Variable: APP_SECRET
Value: <secret>
---
Variable: APP_ADMIN_PASSWORD
Value: <password>

# II: environments on linux (and subsystem on windows)

nano ~/.bashrc
export DATABASE_URL="postgres://<username>:<password>@<host>:<port>/<dbname>"
export JDBC_DATABASE_URL="jdbc:postgresql://<host>:<port>/<dbname>?user=<username>&password=<password>"                     
export JDBC_DATABASE_USERNAME="<username>"
export JDBC_DATABASE_PASSWORD="<password>"
export APP_SECRET="<secret>"
export APP_ADMIN_PASSWORD="<password>"
source ~/.bashrc

# III: on ubuntu (not subsystem on windows)

sudo -H gedit /etc/environment
DATABASE_URL="postgres://<username>:<password>@<host>:<port>/<dbname>"
JDBC_DATABASE_URL="jdbc:postgresql://<host>:<port>/<dbname>?user=<username>&password=<password>"                     
JDBC_DATABASE_USERNAME="<username>"
JDBC_DATABASE_PASSWORD="<password>"
APP_SECRET="<secret>"
APP_ADMIN_PASSWORD="<password>"

# IV heroku only
TZ=x/y (TZ=Europe/Berlin)

# V load data

# only for database initialization
# after initialization delete this environment
# use parallel only on high-end hardware!!!
INSERT_ALL=<parallel> or <normal>