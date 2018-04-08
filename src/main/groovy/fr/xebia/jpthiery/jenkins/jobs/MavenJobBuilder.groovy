package fr.xebia.jpthiery.jenkins.jobs

class MavenJobBuilder<T extends MavenJobBuilder>  extends JobBuilder<T>  {

  MavenJobBuilder(String jobName) {
    super(jobName)
  }

  T mvnCmd(String mvnCmd) {
    addStep {
      ctx ->
        ctx.shell """
                  |rm -f target || true
                  |mkdir target
                  |chmod -R 777 target
                  |containerId=\$(docker create -w /usr/src/mymaven maven:3-jdk-9-slim /bin/bash -c "${mvnCmd}")
                  |docker cp /var/jenkins_home/workspace/${jobName}/. \${containerId}:/usr/src/mymaven/
                  |docker start -a \${containerId}
                  |docker cp \${containerId}:/usr/src/mymaven/target/. target
                  """.stripMargin()
    }
    this
  }

}

