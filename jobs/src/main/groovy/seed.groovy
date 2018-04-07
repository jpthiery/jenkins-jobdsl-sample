job('seed') {
  scm {
    git {
      remote {
        name('origin')
        url('https://github.com/jpthiery/jenkins-jobdsl-sample.git')
        branch('${COMMIT_REFERENCE}')
      }
    }
  }
  parameters {
    choiceParam(
      "COMMIT_REFERENCE",
      ['master', 'seedv3'],
      'Commit reference to use to run this job.'
    )
  }
  label('jkmaster')
  steps {
    shell './gradlew cleanAll build libs'
  }
  steps {
    dsl {
      external('jobs/src/main/groovy/*.groovy')
      additionalClasspath(['build/libs/*.jar', 'build/*.jar'].join('\n'))
      removeAction('DELETE')
      removeViewAction('DELETE')
    }
  }
  logRotator(15, 10)
}
