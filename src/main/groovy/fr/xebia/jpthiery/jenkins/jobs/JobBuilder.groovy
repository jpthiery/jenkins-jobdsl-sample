package fr.xebia.jpthiery.jenkins.jobs

import groovy.text.SimpleTemplateEngine
import groovy.text.Template
import groovy.text.TemplateEngine
import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job

class JobBuilder<T extends JobBuilder> {

  protected String jobName

  protected String labels

  protected String repositoryUrl

  protected String branchGroovySelector

  protected String branchName = 'master'

  protected boolean localBranch = false

  protected boolean useGitCrypt = false

  protected String buildName

  protected Closure jobParameters

  protected Closure logRotator

  protected List<Closure> jobsSteps = new ArrayList<>()

  protected List<Closure> publishers = new ArrayList<>()

  protected List<Closure> wrappers = new ArrayList<>()

  JobBuilder(String jobName) {
    this.jobName = jobName
  }

  T logRotator(Closure logRotator) {
    this.logRotator = logRotator
    this
  }

  T parameters(Closure parameters) {
    this.jobParameters = parameters
    this
  }

  T wrappers(Closure wrappers) {
    this.wrappers.add(wrappers)
    this
  }

  T repositoryUrl(String repositoryUrl) {
    this.repositoryUrl = repositoryUrl
    this
  }

  T branchName(String branchName) {
    this.branchName = branchName
    this
  }

  T labels(String labels) {
    this.labels = labels
    this
  }

  T localBranch(boolean localBranch) {
    this.localBranch = localBranch
    this
  }

  T useGitCrypt(boolean useGitCrypt) {
    this.useGitCrypt = useGitCrypt
    this
  }

  T branchGroovySelector(String branchGroovySelector) {
    this.branchGroovySelector = branchGroovySelector
    this
  }

  T addStep(Closure step) {
    jobsSteps.add(step)
    this
  }

  T addPublisher(Closure publisher) {
    publishers.add(publisher)
    this
  }

  T buildName(String buildName) {
    this.buildName = buildName
    this
  }

  Job build(DslFactory dslFactory) {
    def job = dslFactory.job(jobName)

    if (useGitCrypt) {
      job.steps {
        shell 'git-crypt unlock ~/git-crypt.key'
      }
    }

    jobsSteps.each { currentStep ->
      job.steps { currentStep(delegate) }
    }

    job.with {

      if (labels != null && labels.length() > 0) {
        label(labels)
      }
      if (jobParameters) {
        it.parameters {
          jobParameters.each { param ->
            param(delegate)
          }
        }
      }

      it.publishers {
        publishers.each { publisher ->
          publisher(delegate)
        }
      }

      if (repositoryUrl != null) {
        it.scm {
          git {
            remote {
              name('origin')
              url(repositoryUrl)
              credentials('gitscm')
              if (branchGroovySelector == null || branchGroovySelector.length() <= 0) {
                branch(branchName)
              }
            }
            extensions {
              if (localBranch) {
                localBranch()
              }
              wipeOutWorkspace()
            }
          }
        }
      }
      if (logRotator) {
        it.logRotator {
          logRotator(delegate)
        }
      }
      it.wrappers {
        timestamps()
        colorizeOutput()
        injectPasswords {
          injectGlobalPasswords()
        }
        if (buildName != null)
          buildName(buildName)
        wrappers.each {
          wrapper ->
            wrapper(delegate)
        }
      }
      it
    }
    job
  }

  private static final TemplateEngine ENGINE = new SimpleTemplateEngine()

  private static final Map<String, Template> TEMPLATE_CACHE = [:]

  static Template loadTemplate(String name) {
    if (!TEMPLATE_CACHE.containsKey(name)) {
      Template template = ENGINE.createTemplate(JobBuilder.getClassLoader().getResourceAsStream("templates/$name").text)
      TEMPLATE_CACHE.put(name, template)
    }
    TEMPLATE_CACHE.get(name)
  }
}
