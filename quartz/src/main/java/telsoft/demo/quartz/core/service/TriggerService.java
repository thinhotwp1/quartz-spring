package telsoft.demo.quartz.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import telsoft.demo.quartz.core.entity.Triggers;
import telsoft.demo.quartz.core.repository.TriggersRepository;

import java.util.List;
import java.util.Set;

@Service
public class TriggerService {

    @Autowired
    private TriggersRepository triggersRepository;

    public Set<Triggers> getTriggersListByJobName(String jobName){
        return triggersRepository.findTriggersByJobName(jobName);
    }

}
