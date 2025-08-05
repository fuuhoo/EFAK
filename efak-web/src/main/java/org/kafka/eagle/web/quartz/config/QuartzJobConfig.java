package org.kafka.eagle.web.quartz.config;

import org.kafka.eagle.web.annotation.QuartzJob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class QuartzJobConfig {

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private ApplicationContext applicationContext;

    // 指定扫描的包路径
    private static final String SCAN_PACKAGE = "org.kafka.eagle.web.quartz.job";

    @PostConstruct
    public void registerJobs() throws Exception {
        Set<Class<?>> jobClasses = findClassesWithAnnotation(SCAN_PACKAGE, QuartzJob.class);
        for (Class<?> jobClass : jobClasses) {
            if (Job.class.isAssignableFrom(jobClass)) {
                registerJob((Class<? extends Job>) jobClass);
            }
        }
    }

    private void registerJob(Class<? extends Job> jobClass) throws SchedulerException {
        QuartzJob annotation = jobClass.getAnnotation(QuartzJob.class);
        String jobGroup = annotation.group();
        String cronExpression = annotation.cron();

        // 创建JobDetail
        JobDetail jobDetail = JobBuilder.newJob(jobClass)
                .withIdentity(jobClass.getSimpleName(), jobGroup)
                .storeDurably()
                .build();

        // 创建Trigger
        Trigger trigger = TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(jobClass.getSimpleName() + "Trigger", jobGroup)
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                .build();

        // 注册到Scheduler
        if (!scheduler.checkExists(jobDetail.getKey())) {
            scheduler.scheduleJob(jobDetail, trigger);
        }
    }

    // 扫描包下带有指定注解的类
    private Set<Class<?>> findClassesWithAnnotation(String basePackage, Class annotation) throws IOException, ClassNotFoundException {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(resolver);
        Set<Class<?>> classes = new HashSet<>();

        String packagePath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                ClassUtils.convertClassNameToResourcePath(basePackage) + "/**/*.class";

        Resource[] resources = resolver.getResources(packagePath);
        for (Resource resource : resources) {
            if (resource.isReadable()) {
                MetadataReader reader = readerFactory.getMetadataReader(resource);
                String className = reader.getClassMetadata().getClassName();
                Class<?> clazz = Class.forName(className);
                if (clazz.isAnnotationPresent(annotation)) {
                    classes.add(clazz);
                }
            }
        }
        return classes;
    }
}
