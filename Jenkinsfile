pipeline {
    agent any
    environment {
        DOCKER_CREDENTIALS = credentials('docker-credentials')  // Jenkins Credentials ID 사용
    }

    stages {
        stage('Checkout') {
            steps {
                checkout([
                    $class: 'GitSCM',
                    branches: [[name: '*/master']],
                    doGenerateSubmoduleConfigurations: false,
                    extensions: [],
                    userRemoteConfigs: [[url: 'https://github.com/fourroro/nolleogasil_backend.git', credentialsId: 'nolleogasil-jenkins-token']]
                ])
            }
        }
        stage('Build Spring Boot') {
            steps {
                script {
                    // Spring Boot Docker 이미지 빌드
                    sh '''
                    docker build -t nolleogasil_backend -f Dockerfile.spring .
                    '''
                }
            }
        }
        stage('Push Docker Images') {
            steps {
                script {
                    // Docker Hub 로그인
                    sh '''
                    echo $DOCKER_CREDENTIALS_PSW | docker login -u $DOCKER_CREDENTIALS_USR --password-stdin
                    '''

                    // Spring Boot 이미지 푸시
                    sh 'docker push $DOCKER_CREDENTIALS_USR/nolleogasil_backend'
                }
            }
        }
        stage('Deploy') {
            steps {
                withCredentials([
                    string(credentialsId: 'spring-rabbitmq-username', variable: 'SPRING_RABBITMQ_USERNAME'),
                    string(credentialsId: 'spring-rabbitmq-password', variable: 'SPRING_RABBITMQ_PASSWORD'),
                    string(credentialsId: 'spring-rabbitmq-host', variable: 'SPRING_RABBITMQ_HOST'),
                    string(credentialsId: 'spring-rabbitmq-port', variable: 'SPRING_RABBITMQ_PORT'),
                    string(credentialsId: 'database-url', variable: 'DATABASE_URL'),
                    string(credentialsId: 'database-username', variable: 'DATABASE_USERNAME'),
                    string(credentialsId: 'database-password', variable: 'DATABASE_PASSWORD'),
                    string(credentialsId: 'openai-api-key', variable: 'OPENAI_API_KEY'),
                    string(credentialsId: 'openai-api-url', variable: 'OPENAI_API_URL'),
                    string(credentialsId: 'openai-model', variable: 'OPENAI_MODEL'),
                    string(credentialsId: 'kakao-client-id', variable: 'KAKAO_CLIENT_ID'),
                    string(credentialsId: 'kakao-authorization-uri', variable: 'KAKAO_AUTHORIZATION_URI'),
                    string(credentialsId: 'kakao-user-info-uri', variable: 'KAKAO_USER_INFO_URI'),
                    string(credentialsId: 'kakao-redirect-uri', variable: 'KAKAO_REDIRECT_URI'),
                    string(credentialsId: 'kakao-api-key', variable: 'KAKAO_API_KEY')
                ]){
                    script {
                       sh '''
                       docker pull $DOCKER_CREDENTIALS_USR/nolleogasil_backend

                       # Docker에서 dangling 이미지 ID 목록 조회
                       dangling_images=$(docker images -f dangling=true -q)

                       # 결과가 비어 있지 않다면, 이미지 삭제
                       if [ -n "$dangling_images" ]; then
                           docker rmi -f $dangling_images
                       else
                           echo "No dangling images to remove."
                       fi

                       # spring-container가 이미 있으면, 중지하고 삭제
                       if [ $(docker ps -q -f name=spring-container) ]; then
                           echo "Stopping existing container..."
                           docker stop spring-container || true
                       fi

                       if [ $(docker ps -aq -f name=spring-container) ]; then
                           echo "Removing existing container..."
                           docker rm spring-container || true
                       fi

                       docker run -d -p 8080:8080 --name spring-container \
                           -e SPRING_RABBITMQ_USERNAME=$SPRING_RABBITMQ_USERNAME \
                           -e SPRING_RABBITMQ_PASSWORD=$SPRING_RABBITMQ_PASSWORD \
                           -e SPRING_RABBITMQ_HOST=$SPRING_RABBITMQ_HOST \
                           -e SPRING_RABBITMQ_PORT=$SPRING_RABBITMQ_PORT \
                           -e SPRING_DATASOURCE_URL=$DATABASE_URL \
                           -e SPRING_DATASOURCE_USERNAME=$DATABASE_USERNAME \
                           -e SPRING_DATASOURCE_PASSWORD=$DATABASE_PASSWORD \
                           -e OPENAI_API_KEY=$OPENAI_API_KEY \
                           -e OPENAI_API_URL=$OPENAI_API_URL \
                           -e OPENAI_MODEL=$OPENAI_MODEL \
                           -e SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_KAKAO_CLIENT_ID=$KAKAO_CLIENT_ID \
                           -e SPRING_SECURITY_OAUTH2_CLIENT_PROVIDER_KAKAO_CLIENT_AUTHORIZATION_URI=$KAKAO_AUTHORIZATION_URI \
                           -e SPRING_SECURITY_OAUTH2_CLIENT_PROVIDER_KAKAO_USER_INFO_URI=$KAKAO_USER_INFO_URI \
                           -e SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_KAKAO_REDIRECT_URI=$KAKAO_REDIRECT_URI \
                           -e KAKAO_API_KEY=$KAKAO_API_KEY \
                           $DOCKER_CREDENTIALS_USR/nolleogasil_backend
                       '''
                    }
                }
            }
        }
    }
    post {
            failure {
                script {
                    currentBuild.result = 'FAILURE'
                    echo "Nolleogasil_backend build failed with status: ${currentBuild.result}"
                }
            }
            always {
                echo 'Nolleogasil_backend build and deployment completed.'
            }
        }
}
