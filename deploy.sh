echo 'Build AUTH Container image & kubernetes apply'

echo "Docker Registry: $1" # dev localhost
echo "Environment: $2" # dev
echo "Version: $3"

chmod +x ./gradlew

./gradlew -b ./server/build.gradle jibDockerBuild
./gradlew -b ./api/build.gradle jibDockerBuild

docker image tag auth-$2-server:$3 $1/auth-$2-server:$3
docker image tag auth-$2-api:$3 $1/auth-$2-api:$3

docker push $1/auth-$2-server:$3
docker push $1/auth-$2-api:$3

kubectl apply -f ./k8s/app-$2.yaml
kubectl apply -f ./k8s/deployment-$2.yaml