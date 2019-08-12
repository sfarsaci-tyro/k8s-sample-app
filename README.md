# k8s-sample-app
Highly scalable Hello Word application.

This application requires Kubernetes + Istio. GraalVM and the native image modules are required to generate native app runner

## All things Kubernetes
## Setup
### Create namespaces
```
kubectl create -f greeter-app-ns.json
```

### Install app
```
kubectl apply -f deployment.yaml
kubectl apply -f service.yaml
kubectl apply -f mesh-routes.yaml
```

### Uninstall app
```
kubectl delete -f mesh-routes.yaml
kubectl delete -f service.yaml
kubectl delete -f deployment.yaml
```

### MisK
#### Install istio
Download istio from https://github.com/istio/istio/releases, unpack and cd to the folder
```
kubectl apply -f install/kubernetes/helm/helm-service-account.yaml
helm repo add istio.io https://storage.googleapis.com/istio-release/releases/1.2.2/charts/
helm init --service-account tiller
helm install install/kubernetes/helm/istio-init --name istio-init --namespace istio-system

helm install install/kubernetes/helm/istio --name istio --namespace istio-system \
    --values install/kubernetes/helm/istio/values-istio-demo.yaml
```

#### Use minikube docker daemon
```
eval $(minukube docker-env)
```

#### Port forward kiali
`kubectl port-forward svc/kiali 20001:20001 -n istio-system`

#### Hit the endpoints through the ingress gateway using the node port
```
export INGRESS_PORT=$(kubectl -n istio-system get service istio-ingressgateway -o jsonpath='{.spec.ports[?(@.name=="http2")].nodePort}')
export SECURE_INGRESS_PORT=$(kubectl -n istio-system get service istio-ingressgateway -o jsonpath='{.spec.ports[?(@.name=="https")].nodePort}')
export INGRESS_HOST=$(minikube ip)

curl http://$INGRESS_HOST:$INGRESS_PORT/hello
```

## Quarkus stuff
### Build the apps
#### Maven build
```
# Create uber jar to run in a jvm
./mvnw clean package
# Build a native app. Requires GralVM with the native-image module and GRAALVM_HOME to be set
./mvnw package -Pnative
```

#### Create a docker image
```
# Run inside a jvn
docker build -f src/main/docker/Dockerfile.jvm -t <app name>:0.0.1 .

# Run native
docker build -f src/main/docker/Dockerfile.native -t <app name>:0.0.1 .
```

#### Build a native runner and create a docker image without GraalVM installed on the host
Warning: This is slow a.f. Seriously, it's quicker to just install GraalVM and its native-image module.

However if you have 50 minutes to spare you can run a the multi-stage docker build.
```
docker build -f src/main/docker/Dockerfile.multistage -t <app name>:0.0.1 .
```

### Run the app
```
# Dev mode
./mvnw package quarkus:dev

# Run native app
./target/<app name>-runner

# Docker container
docker run -i --rm -p 8080:8080 k8s-first-app 0.0.5
```

### MisQ
#### Create a quarkus app
_Use mvn for this, mvnw for anything else_
```
mvn io.quarkus:quarkus-maven-plugin:0.19.1:create \
    -DprojectGroupId=com.tyro.sfarsaci.experiments \
    -DprojectArtifactId=greeter-app \
    -DclassName="com.tyro.sfarsaci.experiments.GreetingResource" \
    -Dextensions="kotlin,resteasy-jsonb" \
    -Dpath="/hello"
```

#### Manage quarkus extensions
Simply add and remove the maven dependencies or use the quarkus maven pluigin
```
./mvnw quarkus:add-extension -Dextensions="io.quarkus:resteasy-jsonb"
./mvnw quarkus:list-extensions
```

#### Goals of the maven quarkus plugin
```
./mvnw quarkus:help
```

#### Publish an image using the minikube docker daemon (required to deploy on minikube)
Run this in the shell where you'll build and tag the image, then follow the build section 
```
eval $(minikube docker-env)
```
