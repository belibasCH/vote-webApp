# Installation

## Running application as standalone app

This is a React App. Build and start the production app locally with:

```shell
$ yarn build
```

## Running application in docker container

**IMPORTANT**: To be able to access the server in the docker environment, the path property `REACT_APP_HOSTNAME` to the server
must be set in file `.env`. These settings will be read by `create-react-app` in the production build process, started by `yarn build`.

### Create the docker image

Build the docker image.

```shell
$ docker build -t kvanc_voting-system_riaclient .
@ docker images
REPOSITORY                                              TAG                 IMAGE ID            ...
kvanc_voting-system_riaclient                           latest              5580ecee64c1        ...
...
```

Run the docker image locally, using the tagged image and in detached mode, use this:

```shell
$ docker run -d -p 8085:80 -t kvanc_voting-system_riaclient
$ docker ps
CONTAINER ID    ...
aaf162aaeb35    ...
...
$ docker logs -f aaf162aaeb35
```

Open app with url [localhost:8080](http://localhost:8085)

### Upload the docker image into the gitlab repository

To upload the image into gitlab, follow the instructions found in the [Registry Section](https://gitlab.fhnw.ch/kvanc/labs/voting-system/server/container_registry). For authentication use the usual username/password credentials.

To push image:

```shell
$ docker build -t cr.gitlab.fhnw.ch/kvanc/labs/voting-system/riaclient .
$ docker push cr.gitlab.fhnw.ch/kvanc/labs/voting-system/riaclient
```

### Download the docker image onto the server

Pull the image from the gitlab repository with:

```shell
$ docker login cr.gitlab.fhnw.ch
$ docker pull cr.gitlab.fhnw.ch/kvanc/labs/voting-system/riaclient
```
