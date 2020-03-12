package cz.kul.snippets.spring._14_batch.example01_HelloWorld;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.transaction.PlatformTransactionManager;

import java.net.MalformedURLException;

/*

Job - it is just configuration of the batch task. Usually it is a collection of steps
      and contains some common configuration for its steps.

JobInstance - the logical job run. For example if the job runs once per day there are
  job instances like 1st_January, 2nd_January, ...

JobParameters - Parameters for the particular JobInstance. 
  We can say JobInstance = Job + JobParameters
  
JobExecution - one JobInstance can be executed more times. For example when the first execution
  fails. This instance represents the particular execution.
  So Job ->* JobInstance ->* JobExecution

Step - every Job is composed entirely of one or more steps.

StepExecution - particular execution of the step. So a JobExecution is related to N
  StepExecution. Notice there is no StepInstance.
  If a step fails to execute because the step before it fails, no execution is persisted for it.

JobRepository - repository which store batch metadata. That is jobs, steps, executions,
 params, contexts, ...
 Usually the implementation persists data to DB.
 
JobLauncher - can launch a job

JobExplorer - 

JobBuilderFactory - 

StepBuilderFactory - 


* Attempting to run the same JobInstance while another is already running results in a JobExecutionAlreadyRunningException being thrown


 */
public class ExampleHelloWorld {

    @Configuration
    @EnableBatchProcessing
    public class ContextConfiguration {

//        @Autowired
//        private JobBuilderFactory jobs;
//        
//        @Autowired
//        private StepBuilderFactory steps;
//
//        @Bean
//        public Job job() {
//            return jobs.get("myJob").start(step1()).next(step2()).build();
//        }
//
//        @Bean
//        protected Step step1() {
//        }
//
//        @Bean
//        protected Step step2() {
//        }
        
    }
    
}
