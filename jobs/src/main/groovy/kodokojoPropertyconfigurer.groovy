

job('property-configurer') {
  scm {
    git {
      remote {
        name('origin')
        url('https://github.com/kodokojo/property-configurer.git')
        branch('master')
      }
    }
  }
  label('jkmaster')
  steps {
    shell 'docker run --rm -v "$(pwd)":/usr/src/mymaven -w /usr/src/mymaven maven:3-jdk-9-slim /bin/bash -c "mvn clean verify"'
  }
  logRotator(15, 10)
}
