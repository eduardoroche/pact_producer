#!groovy
pipeline {

  agent any

  environment {
    BRANCH_NAME=env.GIT_BRANCH.replace("origin/", "")
  }

  parameters {
    string(name: 'pactConsumerTags', defaultValue: 'master')
  }

  tools {
	maven 'maven'
  }

  stages {
    stage ('Build') {
      steps {
		sh "mvn clean verify -Dpact.provider.version=${GIT_COMMIT} -Dpact.verifier.publishResults=true  -Dpactbroker.tags=prod,${params.pactConsumerTags}"
      }
    }
    stage('Check Pact Verifications') {
      steps {
        sh 'curl -LO https://github.com/pact-foundation/pact-ruby-standalone/releases/download/v1.61.1/pact-1.61.1-linux-x86_64.tar.gz'
        sh 'tar xzf pact-1.61.1-linux-x86_64.tar.gz'
        dir('pact/bin') {
            // --to prod -- set it in case you want to deploy the PROD
          sh "./pact-broker can-i-deploy -a user-service -b http://pact_broker -e ${GIT_COMMIT}"
        }
      }
    }
    stage('Deploy') {
      when {
        branch 'master'
      }
      steps {
        echo 'Deploying to prod now...'
      }
    }
   /* stage('Tag Pact') {
      steps {
        dir('pact/bin') {
          sh "./pact-broker create-version-tag -a user-service -b http://pact_broker -e ${GIT_COMMIT} -t prod"
        }
      }
    }*/
  }

  /**stages {
    stage('Get Latest Prod Version From Pact Broker') {
      steps {
        sh 'curl -LO https://github.com/pact-foundation/pact-ruby-standalone/releases/download/v1.61.1/pact-1.61.1-linux-x86_64.tar.gz'
        sh 'tar xzf pact-1.61.1-linux-x86_64.tar.gz'
        dir('pact/bin') {
          script {
            env.PROD_VERSION = sh(script: "./pact-broker describe-version -a user-service -b http://pact_broker -l prod | tail -1 | cut -f 1 -d \\|", returnStdout: true).trim()
          }
        }
        echo "Current prod version: " + PROD_VERSION
      }
    }
    stage("Checkout Latest Prod Version") {
      steps {
        sh "git checkout ${PROD_VERSION}"
      }
    }

    stage('Run Contract Tests') {
      steps {
        sh "mvn clean test " +
                "-Pcontract-tests " +
                "-Dpact.provider.version=${PROD_VERSION} " +
                "-Dpact.verifier.publishResults=true " +
                "-Dpactbroker.tags=prod,${params.pactConsumerTags}"
      }
    }
  }**/

}