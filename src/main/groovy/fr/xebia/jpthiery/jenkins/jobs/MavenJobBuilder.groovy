package fr.xebia.jpthiery.jenkins.jobs

class MavenJobBuilder<T extends MavenJobBuilder> extends JobBuilder<T> {

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
                  |docker rm \${containerId}
                  """.stripMargin()
    }
    this
  }

  T lookupGithubBranch(String repository, String projectName) {
    this.parameters {
      ctx ->
        ctx.extensibleChoiceParameterDefinition {
          name('BRANCH')
          editable(false)
          description('Which branch to execute.')
          choiceListProvider {
            systemGroovyChoiceListProvider {
              usePredefinedVariables(true)
              defaultChoice('master')
              groovyScript {
                script(loadTemplate('list_github_branches.groovy').make(repository: repository, project: projectName).toString())
                sandbox(false)
              }
            }
          }
        }
    }
    this.branchName = '$BRANCH'
    this
  }


}

