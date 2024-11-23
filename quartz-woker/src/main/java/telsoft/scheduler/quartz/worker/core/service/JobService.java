package telsoft.scheduler.quartz.worker.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import telsoft.scheduler.quartz.worker.core.enums.ParamType;
import telsoft.scheduler.quartz.worker.core.repository.JobDetailRepository;
import telsoft.scheduler.quartz.worker.core.repository.JobHistoryRepository;
import telsoft.scheduler.quartz.worker.core.repository.JobParamRepository;
import telsoft.scheduler.quartz.worker.core.dto.ParamJobDTO;
import telsoft.scheduler.quartz.worker.core.entity.JobDetail;
import telsoft.scheduler.quartz.worker.core.entity.JobHistory;
import telsoft.scheduler.quartz.worker.core.entity.JobParam;
import telsoft.scheduler.quartz.worker.core.exception.NotFoundException;
import telsoft.scheduler.quartz.worker.core.repository.*;

import java.util.*;

@Service
public class JobService {

    @Autowired
    TriggerService triggerService;

    @Autowired
    JobDetailRepository jobDetailRepository;

    @Autowired
    JobParamRepository jobParamRepository;

    @Autowired
    JobHistoryRepository jobHistoryRepository;


    private JobParam buildJobParam(String jobId, ParamType paramType, String paramName, String defaultValue) {
        return JobParam.builder()
                .jobName(jobId)
                .paramName(paramName)
                .paramType(paramType)
                .paramValue(defaultValue)
                .build();
    }


    public Map<String, Object> getMapParamsByJobId(String jobId) {
        Map<String, Object> map = new HashMap<>();

        List<JobParam> jobParams = jobParamRepository.findAllByJobName(jobId);

        // Add params to map
        for (JobParam param : jobParams) {
            map.put(param.getParamName(), param.getParamValue());
        }
        return map;
    }

    public void saveJobHistory(JobHistory jobHistory) throws Exception {
        jobHistoryRepository.saveAndFlush(jobHistory);
    }

    public void updateParamJob(String jobId, ParamType paramType, String key, Object value) {
        JobParam jobParam = JobParam
                .builder()
                .jobName(jobId)
                .paramType(paramType)
                .paramName(key)
                .paramValue(value.toString())
                .build();
        jobParamRepository.saveAndFlush(jobParam);
    }

    public void updateListParamJob(String jobId, List<ParamJobDTO> paramJobList) {
        List<JobParam> jobParams = new ArrayList<>();

        paramJobList.forEach(jobParam ->
                jobParams.add(buildJobParam(jobId, jobParam.getParamType(), jobParam.getParamName(), jobParam.getParamValue().toString())));

        jobParamRepository.saveAllAndFlush(jobParams);
    }

    public JobDetail getJobDetail(String jobId) {
        Optional<JobDetail> jobDetailDatabase = jobDetailRepository.findById(jobId);

        if (jobDetailDatabase.isEmpty())
            throw new NotFoundException("Not found job detail with jobId: " + jobId);

        return getJobDetails(jobDetailDatabase.get());
    }private JobDetail getJobDetails(JobDetail jobDetailDatabase) {
        // Set params for job
        List<JobParam> jobParams = jobParamRepository.findAll();
        jobDetailDatabase.setJobParams(jobParams);

        // Set triggers for job
        Set<telsoft.scheduler.quartz.worker.core.entity.Trigger> triggers = triggerService.getTriggersDetail(jobDetailDatabase.getJobName());
        jobDetailDatabase.setTriggers(triggers);

        return jobDetailDatabase;
    }

}
