package br.com.pedromonteiro.backend;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig {
    
    private PlatformTransactionManager transactionManager;
    private JobRepository jobRepository;

    public BatchConfig(PlatformTransactionManager transactionManager, JobRepository jobRepository) {
        this.transactionManager = transactionManager;
        this.jobRepository = jobRepository;
    }

    @Bean
    Job job(Step step) {

        return new JobBuilder("job", jobRepository)
        .start(step)
        .incrementer(new RunIdIncrementer())
        .build();
    }
    
    @Bean
    Step step(ItemReader<TransacaoCNAB> reader, ItemProcessor<TransacaoCNAB, Transacao> processor, ItemWriter<Transacao> writer) {
        return new StepBuilder("step", jobRepository)
        .<TransacaoCNAB,Transacao>chunk(1000, transactionManager)
        .reader(reader)
        .processor(processor)
        .writer(writer)
        .build();
    }

    @Bean
    FlatFileItemReader<TransacaoCNAB> reader() {
        return new FlatFileItemReaderBuilder<TransacaoCNAB>()
        .name("reader")
        .resource(new FileSystemResource("files/CNAB.txt"))
        .fixedLength()
        .columns(
            new Range(1,1), new Range(2, 9),
            new Range(1,1), new Range(2, 9),
            new Range(10,19), new Range(43, 48),
            new Range(1,1), new Range(63, 80)
        )
        .names("tipo", "data", "valor", "cpf", "cartao", "hora", "donoDaLoja", "nomeDaLoja")
        .targetType(TransacaoCNAB.class)
        .build();
    }
}   