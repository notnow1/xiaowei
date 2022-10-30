pipeline {
  agent {
    node {
      label 'maven'
    }

  }
  stages {
    stage('clone') {
      agent none
      steps {
        container('maven') {
          git(url: 'http://172.31.0.156/backend/qixiaowei-cloud.git', credentialsId: 'deploy', branch: 'main', changelog: true, poll: false)
        }

      }
    }

    stage('compile-test') {
      agent none
      steps {
        container('maven') {
          sh 'mvn clean package -Ptest -Dmaven.test.skip=true'
        }

      }
    }

    stage('default-2') {
      parallel {
        stage('build qixiaowei-gateway image') {
          agent none
          steps {
            container('maven') {
              sh 'docker build -t qixiaowei-gateway:latest -f Dockerfile  ./qixiaowei-gateway/'
            }

          }
        }

        stage('builid qixiaowei-auth image') {
          agent none
          steps {
            container('maven') {
              sh 'docker build -t qixiaowei-auth:latest -f Dockerfile  ./qixiaowei-auth/'
            }

          }
        }

        stage('builid qixiaowei-file image') {
          agent none
          steps {
            container('maven') {
              sh 'docker build -t qixiaowei-file:latest -f Dockerfile  ./qixiaowei-service/qixiaowei-file/'
            }

          }
        }

        stage('builid qixiaowei-system-manage image') {
          agent none
          steps {
            container('maven') {
              sh 'docker build -t qixiaowei-system-manage:latest -f Dockerfile  ./qixiaowei-service/qixiaowei-system-manage/'
            }

          }
        }

        stage('builid qixiaowei-operate-cloud image') {
          agent none
          steps {
            container('maven') {
              sh 'docker build -t qixiaowei-operate-cloud:latest -f Dockerfile  ./qixiaowei-service/qixiaowei-operate-cloud/'
            }

          }
        }

      }
    }

    stage('default-3') {
      parallel {
        stage('push qixiaowei-gateway image') {
          agent none
          steps {
            container('maven') {
              withCredentials([usernamePassword(credentialsId : 'deploy' ,passwordVariable : 'HARBOR_PWD_VAR' ,usernameVariable : 'HARBOR_USER_VAR' ,)]) {
                sh 'echo "$HARBOR_PWD_VAR" | docker login $REGISTRY -u "$HARBOR_USER_VAR" --password-stdin'
                sh 'docker tag qixiaowei-gateway:latest $REGISTRY/$HARBOR_PROJECT/qixiaowei-gateway:SNAPSHOT-$BUILD_NUMBER'
                sh 'docker push $REGISTRY/$HARBOR_PROJECT/qixiaowei-gateway:SNAPSHOT-$BUILD_NUMBER'
              }

            }

          }
        }

        stage('push qixiaowei-auth image') {
          agent none
          steps {
            container('maven') {
              withCredentials([usernamePassword(credentialsId : 'deploy' ,passwordVariable : 'HARBOR_PWD_VAR' ,usernameVariable : 'HARBOR_USER_VAR' ,)]) {
                sh 'echo "$HARBOR_PWD_VAR" | docker login $REGISTRY -u "$HARBOR_USER_VAR" --password-stdin'
                sh 'docker tag qixiaowei-auth:latest $REGISTRY/$HARBOR_PROJECT/qixiaowei-auth:SNAPSHOT-$BUILD_NUMBER'
                sh 'docker push $REGISTRY/$HARBOR_PROJECT/qixiaowei-auth:SNAPSHOT-$BUILD_NUMBER'
              }

            }

          }
        }

        stage('push qixiaowei-file image') {
          agent none
          steps {
            container('maven') {
              withCredentials([usernamePassword(credentialsId : 'deploy' ,passwordVariable : 'HARBOR_PWD_VAR' ,usernameVariable : 'HARBOR_USER_VAR' ,)]) {
                sh 'echo "$HARBOR_PWD_VAR" | docker login $REGISTRY -u "$HARBOR_USER_VAR" --password-stdin'
                sh 'docker tag qixiaowei-auth:latest $REGISTRY/$HARBOR_PROJECT/qixiaowei-file:SNAPSHOT-$BUILD_NUMBER'
                sh 'docker push $REGISTRY/$HARBOR_PROJECT/qixiaowei-file:SNAPSHOT-$BUILD_NUMBER'
              }

            }

          }
        }

        stage('push qixiaowei-system-manage image') {
          agent none
          steps {
            container('maven') {
              withCredentials([usernamePassword(credentialsId : 'deploy' ,passwordVariable : 'HARBOR_PWD_VAR' ,usernameVariable : 'HARBOR_USER_VAR' ,)]) {
                sh 'echo "$HARBOR_PWD_VAR" | docker login $REGISTRY -u "$HARBOR_USER_VAR" --password-stdin'
                sh 'docker tag qixiaowei-system-manage:latest $REGISTRY/$HARBOR_PROJECT/qixiaowei-system-manage:SNAPSHOT-$BUILD_NUMBER'
                sh 'docker push $REGISTRY/$HARBOR_PROJECT/qixiaowei-system-manage:SNAPSHOT-$BUILD_NUMBER'
              }

            }

          }
        }

        stage('push qixiaowei-operate-cloud image') {
          agent none
          steps {
            container('maven') {
              withCredentials([usernamePassword(credentialsId : 'deploy' ,passwordVariable : 'HARBOR_PWD_VAR' ,usernameVariable : 'HARBOR_USER_VAR' ,)]) {
                sh 'echo "$HARBOR_PWD_VAR" | docker login $REGISTRY -u "$HARBOR_USER_VAR" --password-stdin'
                sh 'docker tag qixiaowei-operate-cloud:latest $REGISTRY/$HARBOR_PROJECT/qixiaowei-operate-cloud:SNAPSHOT-$BUILD_NUMBER'
                sh 'docker push $REGISTRY/$HARBOR_PROJECT/qixiaowei-operate-cloud:SNAPSHOT-$BUILD_NUMBER'
              }

            }

          }
        }

      }
    }

    stage('default-4') {
      parallel {
        stage('deploy qixiaowei-gateway') {
          agent none
          steps {
            container('maven') {
              withCredentials([kubeconfigFile(credentialsId : 'kubeconfig-id' ,variable : 'KUBECONFIG' ,)]) {
                sh 'envsubst < qixiaowei-gateway/deploy/deploy.yaml | kubectl apply -f -'
              }

            }

          }
        }

        stage('deploy qixiaowei-auth') {
          agent none
          steps {
            container('maven') {
              withCredentials([kubeconfigFile(credentialsId : 'kubeconfig-id' ,variable : 'KUBECONFIG' ,)]) {
                sh 'envsubst < qixiaowei-auth/deploy/deploy.yaml | kubectl apply -f -'
              }

            }

          }
        }

        stage('deploy qixiaowei-file') {
          agent none
          steps {
            container('maven') {
              withCredentials([kubeconfigFile(credentialsId : 'kubeconfig-id' ,variable : 'KUBECONFIG' ,)]) {
                sh 'envsubst < qixiaowei-file/deploy/deploy.yaml | kubectl apply -f -'
              }

            }

          }
        }

        stage('deploy qixiaowei-system-manage') {
          agent none
          steps {
            container('maven') {
              withCredentials([kubeconfigFile(credentialsId : 'kubeconfig-id' ,variable : 'KUBECONFIG' ,)]) {
                sh 'envsubst < qixiaowei-system-manage/deploy/deploy.yaml | kubectl apply -f -'
              }

            }

          }
        }

        stage('deploy qixiaowei-operate-cloud') {
          agent none
          steps {
            container('maven') {
              withCredentials([kubeconfigFile(credentialsId : 'kubeconfig-id' ,variable : 'KUBECONFIG' ,)]) {
                sh 'envsubst < qixiaowei-operate-cloud/deploy/deploy.yaml | kubectl apply -f -'
              }

            }

          }
        }

      }
    }

  }
  environment {
    REGISTRY = 'harbor.qixiaowei.net'
    HARBOR_PROJECT = 'qixiaowei-cloud'
  }
}