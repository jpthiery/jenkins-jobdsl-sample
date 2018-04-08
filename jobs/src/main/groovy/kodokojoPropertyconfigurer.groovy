import fr.xebia.jpthiery.jenkins.jobs.JobBuilder

def dslFactory = this

def jobBuilder = new JobBuilder("property-configurer")
jobBuilder.repositoryUrl('https://github.com/kodokojo/property-configurer.git')
  .labels('jkmaster')
  .addStep {
  ctx ->
    ctx.shell 'docker run --rm -v /home/jpthiery/worskapce/xke/jenkins-job-dsl-sample/jenkins/workspace/property-configurer:/usr/src/mymaven -w /usr/src/mymaven maven:3-jdk-9-slim /bin/bash -c "mvn -B clean verify"'
}

jobBuilder.build(dslFactory)
