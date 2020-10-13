# About
This webscraper will pull data (title, username, votes, comment count) from the front page of reddit (or any other subreddit). The data is then saved as a json file, and sent to a postgreSQL database to be used for further analysis

# Setup:

Install postgreSQL (***https://www.postgresql.org/docs/9.0/tutorial-createdb.html***) and initialize a database named: ***redditData***

On OSX:
```
brew install postgresql
brew cask install dbeaver-community
```

(Optional) Configure OSX to start Postgres on boot.
```
pg_ctl -D /usr/local/var/postgres start
```

Start Postgres:
```
brew services start postgresql
```

Create the required `redditData` database:
```
createdb redditData
```

Start up the postgres terminal, connecting to the default `postgres` database:
```
psql postgres
```

Create required postgres role. Give it default permissions:
NOTE: replace pmotard with your username. Where username is the output of `echo $USER` in your terminal.
```
psql postgres
  psql (12.4)
  Type "help" for help.

postgres=# create role greggushurst with superuser login;
postgres=# grant pmotard to greggushurst
```

Restore the `redditData` table from backup:
NOTE: Run this command at the root of the project.
```
psql -U greggushurst redditData < db/redditData.dmp
```

## Configuration
The application requires configuration. There is an example configuration file called `config.properties.example`. Copy this file to `config.properties` in the root of the project, and fill in the values before running the project.

# Running

The main entrypoint to the application is `App.java`. To run the example of using the Reddit API, run `Example.java`.

# Planned features
- Reddit api support
- AWS integration
- Timed autorun cycle
- Full docker support
